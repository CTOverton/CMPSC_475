package edu.psu.cto5068.hw_2;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView fieldEmail;
    TextView fieldEmailSubject;

    Spinner spinnerStructure;
    CheckBox checkGetMin;
    CheckBox checkInsert;
    CheckBox checkSearch;
    RadioButton radioAvg;

    TextView textTo;
    TextView textSubject;
    TextView textBody;

    Boolean composeToggle = true;
    String toAddress;
    String subject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fieldEmail = findViewById(R.id.editEmail);
        fieldEmailSubject = findViewById(R.id.editSubject);

        spinnerStructure = findViewById(R.id.dataStructureSpinner);
        checkGetMin = findViewById(R.id.checkBoxGetMin);
        checkInsert = findViewById(R.id.checkBoxInsert);
        checkSearch = findViewById(R.id.checkBoxSearch);
        radioAvg = findViewById(R.id.radioButtonAverageCase);

        textTo = findViewById(R.id.lblTo);
        textSubject = findViewById(R.id.lblSubject);
        textBody = findViewById(R.id.lblBody);

        fieldEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                textTo.setText(String.format("To: %s", s));
                subject = String.format("%s",s);
            }
        });

        fieldEmailSubject.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                textSubject.setText(String.format("Subject: %s", s));
                toAddress = String.format("%s",s);
            }
        });

        spinnerStructure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateBody(spinnerStructure);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    @Override
    protected void onPause() {
        Log.d("life_cycle", "onPause invoked");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("life_cycle", "onStop invoked");
        super.onStop();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("life_cycle", "onRestoreInstanceState invoked");
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("life_cycle", "onSaveInstanceState invoked");

    }

    public String getBody(View view) {
        try {
            String structure = spinnerStructure.getSelectedItem().toString();
            Boolean min = checkGetMin.isChecked();
            Boolean insert = checkInsert.isChecked();
            Boolean search = checkSearch.isChecked();
            Boolean avgCase = radioAvg.isChecked();

            String body = (avgCase ? "Average Case " : "Worst Case ") + "Time Complexity for " + structure + "\n";

            InputStream is = getResources().openRawResource(R.raw.datastructuredata);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);

            if (min) {
                body += jsonObject.getJSONObject("structures").getJSONObject(structure).getJSONObject(avgCase ? "avg" : "worst").getString("Get Min") + "\n";
            }
            if (insert) {
                body += jsonObject.getJSONObject("structures").getJSONObject(structure).getJSONObject(avgCase ? "avg" : "worst").getString("Insert") + "\n";
            }
            if (search) {
                body += jsonObject.getJSONObject("structures").getJSONObject(structure).getJSONObject(avgCase ? "avg" : "worst").getString("Search") + "\n";
            }

            return body;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void updateBody(View view) {
        textBody.setText(getBody(view));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        View view = findViewById(R.id.mainLayout);

        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_compose:

                if (composeToggle) {
                    composeToggle = !composeToggle;
                    updateBody(view);
                    Snackbar.make(view, "Message Composed! ",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    item.setIcon(R.drawable.ic_mail_24px);
                } else {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{toAddress});
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, getBody(view));

                    item.setIcon(R.drawable.ic_create_24px);
                    composeToggle = !composeToggle;
                    startActivity(intent);
                }


                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
