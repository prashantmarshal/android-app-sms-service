package com.example.sms;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Prashant on 3/25/2015.
 */
public class DataRetrieve extends AsyncTask <String, String, String> {
    @Override
    protected String doInBackground(String... params) {
        // Create http client object to send request to server

        // now we have to send this information to the web page via HttpGet request
        // easiest way to do is use apache http client bundled with android

        HttpClient httpclient = new DefaultHttpClient();
        String responseString = null;
        try {
            String URL = params[0];

            // Create request to server and get response
            HttpGet httpGet = new HttpGet(URL);
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // rest code is to get the response from the server and taking it into string
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else {
                httpResponse.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }catch (ClientProtocolException e) {
                e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // the following string is sent to the onPostExecute function
        return responseString;
    }

    protected void onPostExecute(String responseString){
        if(responseString == null){
            Log.e("Response String status", "Response String is null");
        }else{
            // this method calls the sendMessage function of the sendReply class to send the response string via text message
            new SendReply().sendMessage(responseString);
        }

    }
}
