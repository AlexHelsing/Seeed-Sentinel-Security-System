package com.example.androidapp.MQTT;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.androidapp.Activities.AlarmStatusActivity;

import com.example.androidapp.ViewModels.AlarmViewModel;
import com.example.androidapp.R;
import com.example.androidapp.ViewModels.UserViewModel;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;


public class BrokerConnection extends AppCompatActivity {

    public static final String SUB_TOPIC = "/SeeedSentinel/AlarmOnOff";
    private static final String MQTT_SERVER = "tcp://10.0.2.2:1883";
    public static final String CLIENT_ID = "SeeedSentinel1";
    public static final int QOS = 1;
    private boolean isConnected = false;
    private MqttClient mqttClient;
    Context context;
    TextView connectionMessage;

    // view model that handles the alarm status state
    AlarmViewModel alarmViewModel = new AlarmViewModel();


    public BrokerConnection(Context context) {
        // use singleton pattern to ensure only one instance of mqtt client
        if (mqttClient == null) {
            mqttClient = new MqttClient(context, MQTT_SERVER, CLIENT_ID);
        }
        this.context = context;
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
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;

                    final String connectionLost = "Connection to MQTT broker lost";
                    Log.w(CLIENT_ID, connectionLost);
                }
                /**
                 *  Method that retrieve the message inside a topic
                 *
                 *  Note: change to a switch instead for better structure
                 */
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals(SUB_TOPIC)) {
                        String mqttMessage = new String(message.getPayload());

                        switch (mqttMessage) {
                            case "AlarmOff":
                                // set alarm status to false
                                alarmViewModel.setAlarmStatus("AlarmOff");
                                break;
                            case "AlarmOn":
                                // set alarm status to true
                                alarmViewModel.setAlarmStatus("AlarmOn");
                                break;
                            case "AlarmIntruder":
                                alarmViewModel.setAlarmStatus("AlarmIntruder");
                                UserViewModel.createBreakin(new Date());
                                sendIntruderNotification();
                                break;

                        }
                    }
                    else {
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
    public void publishMqttMessage(String topic, String message, String actionDescription) {
        if (!isConnected) {
            final String notConnected = "Not connected (yet)";
            Log.e(CLIENT_ID, notConnected);
            return;
        }
        mqttClient.publish(topic, message, 1, new IMqttActionListener() {
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

    public void sendIntruderNotification() {

        //channel 1
        Intent intent = new Intent(context, AlarmStatusActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "AlarmStatus");
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle("INTRUDER ALERT");
        builder.setContentText("Call popo");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(10, builder.build());

        //channel2
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:112"));
        PendingIntent actionIntent = PendingIntent.getActivity(context, 0, dialIntent, 0);

        builder = new NotificationCompat.Builder(context, "CallPolice");
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle("Alarm has been activated");
        builder.setContentText("Press notification to call the police");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentIntent(pendingIntent);
        builder.addAction(R.mipmap.ic_launcher, "Call the police", actionIntent);
        builder.setAutoCancel(true);

        notificationManager.notify(5, builder.build());

        //channel3
        String emergencyContactN = UserViewModel.getEmergencyContactNumber();
        Intent intent3 = new Intent(Intent.ACTION_DIAL);
                intent3.setData(Uri.parse("tel:" + emergencyContactN));
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent3, 0);

                builder = new NotificationCompat.Builder(context, "EmergencyContact");
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setContentTitle("Alarm has been activated");
                builder.setContentText("Press notification to call emergency contact");
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setContentIntent(pendingIntent);
                builder.addAction(R.mipmap.ic_launcher, "Call emergency contact", pendingIntent3);
                builder.setAutoCancel(true);

               notificationManager.notify(1, builder.build());

}}