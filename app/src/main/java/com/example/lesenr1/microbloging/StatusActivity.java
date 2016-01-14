package com.example.lesenr1.microbloging;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity; // anciennement StatusActivity heritait de CompAppActivity et je ne pouvais pas mettre le fullscreen
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import winterwell.jtwitter.Twitter;


public class StatusActivity extends Activity {

    Twitter twitter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set Jtwitter API
        twitter = new Twitter("student", "password");
        twitter.setAPIRootUrl("http://www.yamba.marakana.com/api");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Text field handling
        EditText editTextField = (EditText) findViewById(R.id.TextField);
        editTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String inputText = textView.getText().toString();

                    // Show Toast for input debug
                    //Toast.makeText(StatusActivity.this, "Vous avez tapez: " + inputText, Toast.LENGTH_SHORT).show();

                    // Hide keyboard after
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    // Launch the network thread (twitter)
                    PublishMessage Sending = new PublishMessage();
                    Sending.execute(inputText);

                    handled = true;
                }
                return handled;
            }
        });

    }


    // classe ASYNCTASK (generique)
    //La définition de cette classe indique bien que les trois types sont Pram, Progress et Report, dans cet ordre
    //Les prototypes des méthodes que l'on pourra utiliser avec se serviront donc de ces termees, il
    //suffira donc de se référer à l'ordre en question...
    class PublishMessage extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            // String... contains a array of String because you can pass infinite parameters to this function (like printf)
            String Message = params[0];

            // TODO : Check Message lenght to be > 1 and < 200 characters
            twitter.setStatus(Message);

            return 0;
        }

        @Override
        protected void onPostExecute(Integer x) {
            // This will be executed after doInBackground method

            // Show Toast for debug
            Toast.makeText(StatusActivity.this, "Post sent! :)", Toast.LENGTH_SHORT).show();
        }
    }

}
