package io.tactx.wss.wasserschischule;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {


    private MqttAndroidClient mMqttAndroidClient;


    private final String mServerUri = "tcp://iot.eclipse.org:1883";
    private String mClientID = "wss-client";
    private final String mSubTopic = "tactx/unterach/wss/water-temperatures";


    private TextView tvSensors[] = new TextView[4];

    ViewPager mViewPager;
    PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);


        //       tvSensors[0] = (TextView)findViewById(R.id.sensor1);
        //      tvSensors[1] = (TextView)findViewById(R.id.sensor2);
        ///     tvSensors[2] = (TextView)findViewById(R.id.sensor3);
        //   tvSensors[3] = (TextView)findViewById(R.id.sensor4);

        mClientID = mClientID + System.currentTimeMillis();

        mMqttAndroidClient = new MqttAndroidClient(getApplicationContext(), mServerUri, mClientID);
        mMqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    addToHistory("Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                } else {
                    addToHistory("Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, final MqttMessage message) throws Exception {
                addToHistory("Incoming message: " + new String(message.getPayload()));
                //              runOnUiThread(new Runnable() {
                //                  @Override
                //                  public void run() {

                String m = new String(message.getPayload());
                try {
                    JSONArray a = new JSONArray(m);

                    //     tvSensors[0].setText("sdfds");
                    //     tvSensors[1].setText(a.getString(1));
                    //     tvSensors[2].setText(a.getString(2));
                    //     tvSensors[3].setText(a.getString(3));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ///               }
                //          });
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);


        try {
            //addToHistory("Connecting to " + serverUri);
            mMqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mMqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to connect to: " + mServerUri);
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void subscribeToTopic() {
        try {
            mMqttAndroidClient.subscribe(mSubTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    addToHistory("Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to subscribe");
                }
            });

            // THIS DOES NOT WORK!
            mMqttAndroidClient.subscribe(mSubTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, final MqttMessage message) throws Exception {
                    // message Arrived!
                    System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String m = new String(message.getPayload());
                            try {
                                JSONArray a = new JSONArray(m);
                                Double av = 0.0;
                                for (int i = 0; i < 4; i++)
                                    av += Double.parseDouble(a.getString(i + 1));

                                av /= 4;

                                DecimalFormat df = new DecimalFormat("##.##");


                                mPagerAdapter.mainFragment.setValues(a.getString(0), df.format(av));
                                mPagerAdapter.detailFragment.setValues(
                                        a.getString(1),
                                        a.getString(2),
                                        a.getString(3),
                                        a.getString(4)
                                );

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    private void addToHistory(String s) {
        Log.d("MQTT-Main", s);
    }


    public static class PagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        MainFragment mainFragment = MainFragment.newInstance(0, "Page # 1");
        DetailFragment detailFragment = DetailFragment.newInstance(0, "Page # 1");
        AboutFragment aboutFragment = AboutFragment.newInstance(0, "Page # 1");


        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return mainFragment;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return detailFragment;
                case 2: // Fragment # 1 - This will show SecondFragment
                    return aboutFragment;
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
}
