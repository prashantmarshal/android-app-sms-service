package com.example.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URL;

/**
 * Created by Prashant on 3/24/2015.
 */
public class IncomingSms extends BroadcastReceiver {
    // Getting the object of Sms manager
    final SmsManager smsManager = SmsManager.getDefault();
    static String phoneNumber = null;
    static String senderNumber = null;
    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();

        // here intent.getAction() would have "android.provider.Telephony.SMS_RECEIVED"
        // check should be put for debugging


        try{
            if(bundle != null){
                // PDU : industry format for an SMS message
                final Object[] pdusObject = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObject.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObject[i]);
                    phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    // senderNumber to reply back the desired output
                    senderNumber = phoneNumber;

                    // message text in message variable
                    String incoming_msg = currentMessage.getDisplayMessageBody();
                    // String incoming_msg = "iitrpr sgpa 5 2012csb1032";
                    Log.i("SmsReceiver", "senderNum: " + senderNumber + "; message: " + incoming_msg);
                    // Toast is to alert on screen
                    Toast toast = Toast.makeText(context,
                            "Sender Number: " + senderNumber + ", Message text: " + incoming_msg, Toast.LENGTH_LONG);
                    toast.show();

                    // parsing the incoming message
                    String valid, attribute, sem, student_id, incoming_msg_2, URL;
                    incoming_msg_2 = incoming_msg;
                    int space = incoming_msg_2.indexOf(' ');
                    valid = incoming_msg_2.substring(0, space);
                    valid = valid.toLowerCase();
                    incoming_msg_2 = incoming_msg_2.substring(space+1, incoming_msg_2.length());

                    if(valid.equals("iitrpr")){ // implying message is for service

                        URL = "http://54.69.149.147:8080/SMSService?type=";

                        space = incoming_msg_2.indexOf(' ');
                        attribute = incoming_msg_2.substring(0, space); // either cgpa, sgpa, credits
                        attribute = attribute.toLowerCase();
                        URL += attribute;
                        incoming_msg_2 = incoming_msg_2.substring(space+1, incoming_msg_2.length());
                        if(attribute.equals("sgpa")){ // we got to have number now

                            space = incoming_msg_2.indexOf(' ');
                            sem = incoming_msg_2.substring(0, space);
                            incoming_msg_2 = incoming_msg_2.substring(space+1, incoming_msg_2.length());
                            URL += "&sem=" + sem;
                        }
                        student_id = incoming_msg_2.substring(0, incoming_msg_2.length());
                        URL += "&entryno=" + student_id;

                        Log.i("URL is : ", URL);
                        // this method calls the doInBackground function of DataRetrieve class
                        new DataRetrieve().execute(URL);
                        abortBroadcast();

                    }else{
                        return;
                    }
                }
            }
        }catch (Exception e){
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }

    }
}
