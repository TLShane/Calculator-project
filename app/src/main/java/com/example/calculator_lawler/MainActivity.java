package com.example.calculator_lawler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    //wow factor - this registers the apps notification channel so i can send notifications

    //this is the array that holds all the grid values
    String[] threexfour = {"1", "2", "3", "/", "4", "5", "6", "*", "7", "8", "9", "-", "0", ".", "C", "+"};
    //oldvalue to allow for multiple arithmetic equations
    private double oldvalue = 0;
    //int
    private int value;
    private boolean edecimal = false;
    //which math will be done on the numbers
    private String symbol;
    //value that is entered on keyboard, only needs to be int
    private int newvalue;
    //what is pulled from the the array above to be parsed
    String parse;
    //stores current val once oldvalue is no longer usable
    String currentval = " ";
    String newval = "0";
    //this boolean tells the program when it can change values or if it is just doing a sign tranfer
    boolean canEdit = true;
    //different android stuff
    private CustomAdapter adapternum;
    private GridView gridviewnum;
    private TextView text;
    private Button calcb;
    private Button CEB;
    private Button notificationB;
    //wow factor: all under here is for sending notifications
    final String CHANNEL_ID = "notification id";
    //wow factor- i added a option to turn on notifications and the program will send a notification if the option is enabled
    boolean notificationOn;
    String notiftitle = "null";

    //wow factor: this builds the notification
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createNotificationChannel();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridviewnum = (GridView) findViewById(R.id.GridView);

        adapternum = new CustomAdapter(this, threexfour);
        //find views
        text = (TextView) findViewById(R.id.textView2);
        calcb = (Button) findViewById(R.id.calcb);
        CEB = (Button) findViewById(R.id.CEB);
        notificationB = (Button) findViewById(R.id.NotificationB);
        //set customadapter
        gridviewnum.setAdapter(adapternum);
        //notification enable button
        notificationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationOn) {
                    notificationOn = false;
                    notificationB.setText("OFF");
                }
                else {
                    notificationOn = true;
                    notificationB.setText("ON");
                }
            }
        });
        //CE button code
        CEB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbol = null;
                currentval = " ";
                newval = " ";
                oldvalue = 0;
                text.setText("0");
                edecimal = false;
            }
        });

        //this is the code for the calculate button
        calcb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (symbol != null && !currentval.equals(" ")) {
                    //TODO currently this is not errasable with c, possibly make c remove one character from the string
                    if (symbol != null) {
                        calculate();
                        currentval = "0";
                        text.setText(Double.toString(oldvalue));
                        if(notificationOn)
                        sendNotification();
                    } else
                        Toast.makeText(MainActivity.this, "Specify a symbol", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gridviewnum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parse = String.valueOf(adapternum.getItem(position));
                //3,7,11,13,14,15 are special and are therefore handled differently for each
                //division
                if (position == 3) {
                    //this allows for the user to enter the same
                    if (symbol != null && symbol.equals("/")) {
                        calculate();
                    }
                    symbol = parse;
                    canEdit = false;
                    //multiplication
                } else if (position == 7) {
                    if (symbol != null && symbol.equals("*")) {
                        calculate();
                    }
                    symbol = parse;
                    canEdit = false;
                    //subtraction
                } else if (position == 11) {
                    if (symbol != null && symbol.equals("-")) {
                        calculate();
                    }
                    symbol = parse;
                    canEdit = false;
                    //addition
                } else if (position == 15) {
                    if (symbol != null && symbol.equals("+")) {
                        calculate();
                    }
                    symbol = parse;
                    canEdit = false;
                    //decimal
                } else if (position == 13) {
                    //places a decimal mark if a decimal is not already there
                    if (symbol != null && currentval != " ")
                        currentval += ".";
                    else
                        newval += ".";
                    canEdit = false;
                } else if (position == 14) {
                    if (symbol == null) {
                        oldvalue = 0;
                        newval = "0";
                    }
                    if (currentval.equals("0") || currentval.equals(" ")) {
                        symbol = null;
                        currentval = " ";
                    } else {
                        currentval = "0";
                    }
                    canEdit = false;
                    //everything else
                } else {
                    newvalue = Integer.parseInt(parse);
                }
                if (!newval.equals("0") && !newval.equals(" ") && symbol != null) {
                    if (currentval.equals(" "))
                        currentval = "0";
                    else if (currentval.equals("0") && canEdit) {
                        currentval = Integer.toString(newvalue);
                    } else if (canEdit)
                        currentval = currentval + Integer.toString((int) newvalue);
                    text.setText(newval + " " + symbol + " " + currentval);
                    notiftitle = newval + " " + symbol + " " + currentval;
                    canEdit = true;
                } else {
                    if (newval.equals("0") && canEdit) {
                        newval = Integer.toString(newvalue);
                    } else if (canEdit) {
                        newval = newval + Integer.toString((int) newvalue);
                        oldvalue = Double.parseDouble(newval);
                    }
                    text.setText(newval);
                    notiftitle = newval;
                    canEdit = true;
                }
            }
        });
    }

    //makes it slightly easier to use this repeated code
    public void calculate() {
        if (symbol != null && symbol.equals("+")) {
            oldvalue = Double.parseDouble(newval) + Double.parseDouble(currentval);
            newval = Double.toString(oldvalue);
            currentval = " ";
        }
        if (symbol != null && symbol.equals("-")) {
            oldvalue = Double.parseDouble(newval) - Double.parseDouble(currentval);
            newval = Double.toString(oldvalue);
            currentval = " ";
        }
        if (symbol != null && symbol.equals("*")) {
            oldvalue = Double.parseDouble(newval) * Double.parseDouble(currentval);
            newval = Double.toString(oldvalue);
            currentval = " ";
        }
        if (symbol != null && symbol.equals("/")) {
            oldvalue = Double.parseDouble(newval) / Double.parseDouble(currentval);
            newval = Double.toString(oldvalue);
            currentval = " ";
        }
    }

    protected void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(notiftitle + " =")
                .setContentText(Double.toString(oldvalue))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(2,builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}