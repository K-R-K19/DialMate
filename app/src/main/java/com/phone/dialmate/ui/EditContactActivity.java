package com.phone.dialmate.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.phone.dialmate.R;

public class EditContactActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        String name = getIntent().getStringExtra("contact_name");
        String number = getIntent().getStringExtra("contact_number");

        ((TextView) findViewById(R.id.text_contact_name)).setText(name == null ? "" : name);
        ((TextView) findViewById(R.id.text_contact_number)).setText(number == null ? "" : number);
    }
}
