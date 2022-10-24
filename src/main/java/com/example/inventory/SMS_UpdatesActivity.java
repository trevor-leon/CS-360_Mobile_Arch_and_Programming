package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMS_UpdatesActivity extends AppCompatActivity {

    public static final int SMS_RESULT_CODE = 0;
    public static final String PHONE_NO_RESULT = "com.example.inventory.phone_no";
    public static final String LOW_INVENTORY_NUM = "com.example.inventory.low_inventory_number";
    private int lowInventoryInt;
    private String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_updates);

        Button getUpdatesButton = findViewById(R.id.get_updates_button);
        getUpdatesButton.setOnClickListener(view -> {

            EditText lowInventoryText = findViewById(R.id.low_inventory_number);
            EditText phoneNoText = findViewById(R.id.phoneNumberText);
            Intent intent = new Intent();
            try {
                // Populate and set the intent to send to the InventoryActivity
                lowInventoryInt = Integer.parseInt(lowInventoryText.getText().toString());
                intent.putExtra(LOW_INVENTORY_NUM, lowInventoryInt);
                phoneNo = phoneNoText.getText().toString();
                intent.putExtra(PHONE_NO_RESULT, phoneNo);
                setResult(SMS_RESULT_CODE, intent);
            } catch (NullPointerException e) {
                // If one of the EditTexts are null, let them know what went wrong.
                Toast.makeText(getApplicationContext(),
                        R.string.null_inventory_update, Toast.LENGTH_SHORT).show();
            } finally {
                finish();
                SMS_UpdatesActivity.super.onBackPressed(); // Not sure if required
            }

        });
    }

}