package com.example.androidapp.MQTT;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.AlarmStatusActivity;

import com.example.androidapp.AlarmViewModel;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;



public class BrokerConnection extends AppCompatActivity {

    public static final String SUB_TOPIC = "/SeeedSentinel/AlarmOnOff";
    private static final String MQTT_SERVER = "tcp://broker.hivemq.com:1883";
    public static final String CLIENT_ID = "SeeedSentinel";
    public static final int QOS = 1;

    private boolean isConnected = false;
    private MqttClient mqttClient;
    Context context;
    TextView connectionMessage;

    // view model that handles the alarm status state
    AlarmViewModel alarmViewModel = new AlarmViewModel();

    public BrokerConnection(Context context){
        this.context = context;
        mqttClient = new MqttClient(context, MQTT_SERVER, CLIENT_ID);
    }

    public void connectToMqttBroker() {
        if (!isConnected) {
            mqttClient.connect(CLIENT_ID, "", new IMqttActionListener() {
                /**
                 *  Add below the topic that the app subscribe to
                 *  and add the method for that topic in messageArrived(...)
                 */
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    final String successfulConnection = "Connected to MQTT broker";
                    Log.i(CLIENT_ID, successfulConnection);

                    Toast.makeText(context, successfulConnection, Toast.LENGTH_LONG).show();
                    mqttClient.subscribe(SUB_TOPIC, QOS, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            System.out.println("Successfully subscribed to " + SUB_TOPIC);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                        }
                    });
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    final String failedConnection = "Failed to connect to MQTT broker";
                    Log.e(CLIENT_ID, failedConnection);
                    Toast.makeText(context, failedConnection, Toast.LENGTH_SHORT).show();
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;

                    final String connectionLost = "Connection to MQTT broker lost";
                    Log.w(CLIENT_ID, connectionLost);
                    Toast.makeText(context, connectionLost, Toast.LENGTH_SHORT).show();
                }
                /**
                 *  Method that retrieve the message inside a topic
                 *
                 *  Note: change to a switch instead for better structure
                 */
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if(topic.equals(SUB_TOPIC)){
                        String mqttMessage = new String(message.getPayload());
                        if(mqttMessage.equals("AlarmOff")){
                            // set alarm status to false
                            alarmViewModel.setAlarmStatus("AlarmOff");

                        }
                        else if(mqttMessage.equals("AlarmOn")){
                            // set alarm status to true
                            alarmViewModel.setAlarmStatus("AlarmOn");
                        }
                        else if(mqttMessage.equals("AlarmIntruder")){
                            alarmViewModel.setAlarmStatus("AlarmIntruder");
                        }
                    }
                    else{
                        Log.i("BROKER: ", "[MQTT] Topic: " + topic + " | Message: " + message.toString());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(CLIENT_ID, "Message delivered");
                }
            });
        }
    }

    public void setConnectionMessage(TextView textView) {
        this.connectionMessage = textView;
    }

    /**
     * This method is use to create the message that will be publish
     * add the mMqttClient.publish(<topic>, <message >, QOS, null);
     * to specify the topic as in ControlPad or Joystick Class
     *
     * @param message - the message that we send to the broker
     * @param actionDescription - the action description that will be printed
     */
    public void publishMqttMessage(String message, String actionDescription) {
        if (!isConnected) {
            final String notConnected = "Not connected (yet)";
            Log.e(CLIENT_ID, notConnected);
            Toast.makeText(context, notConnected, Toast.LENGTH_SHORT).show();
            return;
        }
        mqttClient.publish(SUB_TOPIC, message, 1, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Successfully sent");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
        Log.i(CLIENT_ID, actionDescription);
    }


    public TextView getMessage() {
        return this.connectionMessage;
    }
    public MqttClient getMqttClient() {
        return mqttClient;
    }
}