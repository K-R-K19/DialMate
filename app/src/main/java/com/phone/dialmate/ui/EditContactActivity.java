package com.phone.dialmate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.phone.dialmate.R;

public class EditContactActivity extends AppCompatActivity {
    private static final String TAG = "DialMate/EditContactAct";
    public static final String EXTRA_CONTACT_NAME = "contact_name";
    public static final String EXTRA_CONTACT_NUMBER = "contact_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_edit_contact);

        Intent intent = getIntent();
        String name = intent != null ? intent.getStringExtra(EXTRA_CONTACT_NAME) : "";
        String number = intent != null ? intent.getStringExtra(EXTRA_CONTACT_NUMBER) : "";

        Log.d(TAG, "Received: name=" + name + ", number=" + number);

        TextView nameText = findViewById(R.id.text_contact_name);
        TextView numberText = findViewById(R.id.text_contact_number);
        if (nameText != null) nameText.setText(name == null ? "" : name);
        if (numberText != null) numberText.setText(number == null ? "" : number);
    }
}
