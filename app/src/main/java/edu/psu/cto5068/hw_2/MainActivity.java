package edu.psu.cto5068.hw_2;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    FloatingActionButton fab;

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

    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);

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


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Email Sent!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Log.d("Sending email", "Body: \n" + email);
            }
        });


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

    public void updateBody(View view) {
        try {
            String structure = spinnerStructure.getSelectedItem().toString();
            Boolean min = checkGetMin.isChecked();
            Boolean insert = checkInsert.isChecked();
            Boolean search = checkSearch.isChecked();
            Boolean avgCase = radioAvg.isChecked();

            String output = (avgCase ? "Average Case " : "Worst Case ") + "Time Complexity for " + structure + "\n";

            InputStream is = getResources().openRawResource(R.raw.datastructuredata);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);

            if (min) {
                output += jsonObject.getJSONObject("structures").getJSONObject(structure).getJSONObject(avgCase ? "avg" : "worst").getString("Get Min") + "\n";
            }
            if (insert) {
                output += jsonObject.getJSONObject("structures").getJSONObject(structure).getJSONObject(avgCase ? "avg" : "worst").getString("Insert") + "\n";
            }
            if (search) {
                output += jsonObject.getJSONObject("structures").getJSONObject(structure).getJSONObject(avgCase ? "avg" : "worst").getString("Search") + "\n";
            }

            textBody.setText(output);
            email = output;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
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
}
