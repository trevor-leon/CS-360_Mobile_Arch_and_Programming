package com.example.inventory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InventoryActivity extends AppCompatActivity {

    // Controller

    private InventoryDatabase itemDB;

    private final int REQUEST_SMS_CODE = 0;

    protected int lowInventoryNo;
    protected String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        RecyclerView dataList = findViewById(R.id.dataList);

        itemDB = InventoryDatabase.getInstance(getApplicationContext());

        // Red apple image source: https://pixabay.com/photos/apple-red-fruit-apples-healthy-5012196/
        // Granny smith image source: https://pixabay.com/photos/apple-fruit-healthy-hand-offer-2766693/
        // Watermelon image source: https://pixabay.com/photos/watermelon-fruit-health-2409368
        // Banana image source: https://pixabay.com/photos/banana-fruit-delicious-food-7117589/
        InventoryItem[] initialItems = new InventoryItem[4];
        initialItems[0] = new InventoryItem("Red Delicious Apple", R.drawable.apple,
                1, "Sweet red delicious apple");
        initialItems[1] = new InventoryItem("Granny Smith Apple", R.drawable.gs_apple,
                0, "Sweet granny smith apple");
        initialItems[2] = new InventoryItem("Watermelon", R.drawable.watermelon,
                1000, "Sweet, juicy, watermelon");
        initialItems[3] = new InventoryItem("Bananas (Bunch)", R.drawable.bananas,
                99, "Sweet bunch of bananas");

        for (InventoryItem initItem : initialItems) {
            itemDB.updateItem(initItem);
        }

        // Get the item at the selected position
        // Add and send the data in the intent to the detailsActivity.
        // TODO: FIXME: RecyclerViewClickListener not working correctly; not even reaching logging
        InventoryItemAdapter.RecyclerViewClickListener listener = (v, position) -> {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            Log.d("InvAct", "made intent");
            // Get the item at the selected position
            InventoryItem item = itemDB.getItems().get(position);
            Log.d("InvAct", "item received");

            // Add and send the data in the intent to the detailsActivity.
            intent.putExtra(DetailsActivity.ITEM_NAME, item.getItemName());
            intent.putExtra(DetailsActivity.IMAGE_ID, item.getImage());
            intent.putExtra(DetailsActivity.ITEM_QUANTITY, item.getQuantity());
            intent.putExtra(DetailsActivity.ITEM_DESC, item.getDescription());
            // Launch the DetailsActivity
            detailsActivityLauncher.launch(intent);
            Log.d("InvAct", "launched intent: ");
        };

        InventoryItemAdapter itemAdapter = new InventoryItemAdapter(getApplicationContext(),
                listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),
                2);

        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(itemAdapter);

        // Implement the FAB code
        // https://developer.android.com/develop/ui/views/components/floating-action-button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            detailsActivityLauncher.launch(intent);

            Toast.makeText(getApplicationContext(), "Inventory item added", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    protected void onDestroy() {
        itemDB.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the app menu
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If the MenuItem selected is text updates, get the permissions before opening SMSActivity.
        if (item.getItemId() == R.id.text_updates) {
            // Make sure SMS permissions are granted before starting the SMS update activity.
            if (PermissionsUtil.hasPermissions(this, Manifest.permission.SEND_SMS,
                    R.string.write_rationale, REQUEST_SMS_CODE)) {
                // Start the SMS_UpdatesActivity if permissions were granted.
                Intent intent = new Intent(getApplicationContext(), SMS_UpdatesActivity.class);
                activitySMSResultLauncher.launch(intent);
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SMS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Start the SMS_UpdatesActivity if permissions were granted.
                Intent intent = new Intent(getApplicationContext(), SMS_UpdatesActivity.class);
                activitySMSResultLauncher.launch(intent);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Set up the launcher for the SMS updates activity.
    ActivityResultLauncher<Intent> activitySMSResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // If the result code matches the result code here...
                if (result.getResultCode() == SMS_UpdatesActivity.SMS_RESULT_CODE) {
                    // Get the intent's data, but make sure it isn't null before working with it.
                    Intent intent = result.getData();
                    if (intent != null) {
                        /*
                          Save the data from the intent to send SMS updates when an inventory's
                          stock is considered low by the user. Default is 0.
                         */
                        lowInventoryNo = intent.getIntExtra(SMS_UpdatesActivity.LOW_INVENTORY_NUM, 0);
                        phoneNo = intent.getStringExtra(SMS_UpdatesActivity.PHONE_NO_RESULT);

                        /*
                        If there is an item in the database that is at or below the low inventory
                        threshold, then send SMS updates for those objects.
                         */
                        SmsManager smsManager = SmsManager.getDefault();
                        for (InventoryItem item : itemDB.getItems()) {
                            if (item.getQuantity() <= lowInventoryNo) {
                                // Send an SMS message to the provided phone number
                                smsManager.sendTextMessage(phoneNo, null,
                                        String.format(getResources().getString(R.string.low_inventory_toast),
                                        item.getItemName()), null, null);

                                // Make a test Toast to confirm that a message was sent for the item
                                String text = String.format(getResources().getString(R.string.low_inventory_toast),
                                        item.getItemName());
                                Toast.makeText(getApplicationContext(),
                                        text, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
    );

    // TODO: DetailsActivity is not showing to the screen, but it's being called.
    // Details activity launcher; result -> {} is the code that is run after returning from Details
    ActivityResultLauncher<Intent> detailsActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                Intent intent = result.getData();
                if (intent != null) {
                    // Change what happens depending on what the user selected
                    switch (resultCode) {
                        case DetailsActivity.REQUEST_CODE_DETAILS_ADD:
                            // Add the item to the database.
                            itemDB.addItem(new InventoryItem(intent.getStringExtra(DetailsActivity.ITEM_NAME),
                                    intent.getIntExtra(DetailsActivity.IMAGE_ID, R.drawable.ic_baseline_inventory_2_24),
                                    intent.getIntExtra(DetailsActivity.ITEM_QUANTITY, 0),
                                    intent.getStringExtra(DetailsActivity.ITEM_DESC)));
                            break;

                        case DetailsActivity.REQUEST_CODE_DETAILS_UPDATE:
                            // Update the item in the database.
                            itemDB.updateItem(new InventoryItem(intent.getStringExtra(DetailsActivity.ITEM_NAME),
                                    intent.getIntExtra(DetailsActivity.IMAGE_ID, R.drawable.ic_baseline_inventory_2_24),
                                    intent.getIntExtra(DetailsActivity.ITEM_QUANTITY, 0),
                                    intent.getStringExtra(DetailsActivity.ITEM_DESC)));
                            break;

                        case DetailsActivity.REQUEST_CODE_DETAILS_DELETE:
                            itemDB.deleteItem(intent.getStringExtra("nameResult"));
                            break;

                        default:
                            break;
                    }
                }
            }
    );

}