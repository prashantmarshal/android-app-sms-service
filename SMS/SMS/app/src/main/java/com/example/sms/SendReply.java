package com.example.sms;

import android.app.Activity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Prashant on 3/25/2015.
 */
public class SendReply extends Activity {

    protected void sendMessage(String responseMessage){
        Log.e("Final response", responseMessage);
        String phoneNumber = new IncomingSms().senderNumber;
        Log.i("Person's phone number", phoneNumber);
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, responseMessage, null, null);
            Toast.makeText(getApplicationContext(), "Response SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(),
//                    "Response SMS sending failed, please try again.",
//                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

}
