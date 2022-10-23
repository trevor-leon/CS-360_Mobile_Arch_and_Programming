package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
//import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    // Controller

    private InventoryDatabase itemDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemDB = InventoryDatabase.getInstance(getApplicationContext());

        EditText itemNameText = findViewById(R.id.itemName);
        ImageButton itemImageButton = findViewById(R.id.imageButton);
        EditText itemQuantityNumber = findViewById(R.id.quantityNumber);
        EditText itemDescriptionText = findViewById(R.id.descriptionText);

        // Get the item extras from Inventory to populate the details
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // If an item on the grid was clicked (extras != null), then populate the text boxes.
            itemNameText.setText(extras.getString("itemName"));
            itemImageButton.setImageResource(extras.getInt("itemImage"));
            //Toast.makeText(getApplicationContext(), "" + extras.getInt("itemQuantity")))
            itemQuantityNumber.setText(String.valueOf(extras.getInt("itemQuantity")));
            itemDescriptionText.setText(extras.getString("itemDescription"));
        }

        Button addItemButton = findViewById(R.id.addButton);
        Button updateItemButton = findViewById(R.id.updateButton);
        Button deleteItemButton = findViewById(R.id.deleteButton);

        addItemButton.setEnabled(false);
        updateItemButton.setEnabled(false);
        updateItemButton.setEnabled(false);

        itemNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                If the new itemName CharSequence's trimmed length is greater than 0 and the other
                textBoxes have text in them, enable the appropriate buttons.
                 */

                /*
                If all of the text boxes have data in them, and the DB does not contains the item
                already, then the update button should be enabled.
                */
                boolean allTextFull = s.toString().trim().length() > 0 &&
                        itemQuantityNumber.getText().toString().trim().length() > 0 &&
                        itemDescriptionText.getText().toString().trim().length() > 0;

                addItemButton.setEnabled(allTextFull &&
                        !itemDB.contains(itemNameText.getText().toString()));

                /*
                If all of the text boxes have data in them, and the DB contains the item already,
                then the update button should be enabled.
                 */
                updateItemButton.setEnabled(allTextFull &&
                        itemDB.contains(itemNameText.getText().toString()));

                // If the itemNameText has text AND the DB has item, set the delete button enabled.
                deleteItemButton.setEnabled(s.toString().trim().length() > 0 &&
                        itemDB.contains(itemNameText.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        itemQuantityNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If the quantity number is valid (>= 0)
                if (s.toString().trim().length() > 0 && Integer.parseInt(s.toString()) >= 0) {
                    /*
                    If the itemName text box has text; itemDesc has text, and the DB doesn't cont-
                    ain the itemName, then the add button should be enabled.
                     */
                    boolean allTextFull = itemNameText.getText().toString().trim().length() > 0 &&
                            itemDescriptionText.getText().toString().trim().length() > 0;


                    addItemButton.setEnabled(allTextFull &&
                            !itemDB.contains(itemNameText.getText().toString()));

                    /*
                    If the itemName text box has text; itemDesc has text, and the DB has the given
                    itemName, then the update button should be enabled.
                     */
                    updateItemButton.setEnabled(allTextFull &&
                            itemDB.contains(itemNameText.getText().toString()));
                } else {
                    itemQuantityNumber.setText(0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Make sure the buttons are updated accordingly when the description text changes.
        itemDescriptionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                If the new itemDesc CharSequence's trimmed length is greater than 0 and the other
                textBoxes have text in them, enable the add and update buttons.
                 */
                boolean allTextFull = s.toString().trim().length() > 0 &&
                        itemNameText.getText().toString().trim().length() > 0 &&
                        itemQuantityNumber.getText().toString().trim().length() > 0;

                addItemButton.setEnabled(allTextFull &&
                        !itemDB.contains(itemNameText.getText().toString()));

                updateItemButton.setEnabled(allTextFull &&
                        itemDB.contains(itemNameText.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final int REQUEST_CODE_DETAILS_ADD = 1;

        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("nameResult", itemNameText.getText().toString());
            intent.putExtra("imageResult", R.drawable.ic_baseline_inventory_2_24);
            intent.putExtra("quantityResult", itemQuantityNumber.getText().toString());
            intent.putExtra("descResult", itemDescriptionText.getText().toString());

            setResult(REQUEST_CODE_DETAILS_ADD, intent);
        });

        final int REQUEST_CODE_DETAILS_UPDATE = 2;

        updateItemButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("nameResult", itemNameText.getText().toString());
            intent.putExtra("imageResult", R.drawable.ic_baseline_inventory_2_24);
            intent.putExtra("quantityResult", itemQuantityNumber.getText().toString());
            intent.putExtra("descResult", itemDescriptionText.getText().toString());

            setResult(REQUEST_CODE_DETAILS_UPDATE, intent);
        });

        final int REQUEST_CODE_DETAILS_DELETE = 3;

        deleteItemButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("deleteName", itemNameText.getText().toString());

            setResult(REQUEST_CODE_DETAILS_DELETE, intent);
        });

        finish();
        DetailsActivity.super.onBackPressed(); // Not sure if required

    }

    @Override
    protected void onDestroy() {
        itemDB.close();
        super.onDestroy();
    }
}
