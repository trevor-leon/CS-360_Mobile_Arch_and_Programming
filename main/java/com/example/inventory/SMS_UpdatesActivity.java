package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMS_UpdatesActivity extends AppCompatActivity {

    private final int SMS_RESULT_CODE = 0;
    private int lowInventoryInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_updates);

        Button getUpdatesButton = findViewById(R.id.get_updates_button);
        getUpdatesButton.setOnClickListener(view -> {
            EditText lowInventoryText = findViewById(R.id.low_inventory_number);
            Intent intent = new Intent();
            try {
                lowInventoryInt = Integer.parseInt(lowInventoryText.getText().toString());
                intent.putExtra("result", lowInventoryInt);
                setResult(SMS_RESULT_CODE, intent);
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(),
                        R.string.null_inventory_update, Toast.LENGTH_SHORT).show();
            } finally {
                finish();
                SMS_UpdatesActivity.super.onBackPressed(); // Not sure if required
            }

        });
    }

}