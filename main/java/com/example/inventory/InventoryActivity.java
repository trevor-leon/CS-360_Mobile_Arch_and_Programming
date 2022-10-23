package com.example.inventory;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InventoryActivity extends AppCompatActivity {

    // Controller

    private InventoryDatabase itemDB;

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
        InventoryItemAdapter.RecyclerViewClickListener listener = new InventoryItemAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                // Get the item at the selected position
                InventoryItem item = itemDB.getItems().get(position);

                // Add and send the data in the intent to the detailsActivity.
                intent.putExtra("itemName", item.getItemName());
                intent.putExtra("itemImage", item.getImage());
                intent.putExtra("itemQuantity", item.getQuantity());
                intent.putExtra("itemDescription", item.getDescription());
                // Launch the DetailsActivity
                detailsActivityLauncher.launch(intent);
            }
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
            // TODO: Create a fragment/intent/activity to get item information: image, name, quantity
            // TODO: Research what needs to be done
            // TODO: DELETE L8r
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
        // If the MenuItem selected is text updates, set and launch the SMS updates intent
        if (item.getItemId() == R.id.text_updates) {
            Intent intent = new Intent(getApplicationContext(), SMS_UpdatesActivity.class);
            activitySMSResultLauncher.launch(intent);
        }
        return true;
    }

    // Set up the launcher for the SMS updates activity.
    ActivityResultLauncher<Intent> activitySMSResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                final int REQUEST_CODE_SMS_UPDATES = 0;
                // If the result code matches the result code here...
                if (result.getResultCode() == REQUEST_CODE_SMS_UPDATES) {
                    // Get the intent's data, but make sure it isn't null before working with it.
                    Intent intent = result.getData();
                    if (intent != null) {
                        /*
                          Save the data from the intent to send SMS updates when an inventory's
                          stock is considered low by the user. Default is 0.
                         */
                        int lowInventoryNumber = intent.getIntExtra("result", 0);

                        /* If there is an item in the database that is at or below the low inventory
                        threshold, then send SMS updates for those objects.
                         */

                        for (InventoryItem item : itemDB.getItems()) {
                            if (item.getQuantity() <= lowInventoryNumber) {
                                // TODO: ******Send SMS notification********************************
                                // TODO For now, make a toast
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

    ActivityResultLauncher<Intent> detailsActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                final int REQUEST_CODE_DETAILS_ADD = 1;
                final int REQUEST_CODE_DETAILS_UPDATE = 2;
                final int REQUEST_CODE_DETAILS_DELETE = 3;
                int resultCode = result.getResultCode();
                Intent intent = result.getData();
                if (intent != null) {
                    switch (resultCode) {
                        case REQUEST_CODE_DETAILS_ADD:
                            // Add the item to the database.
                            itemDB.addItem(new InventoryItem(intent.getStringExtra("nameResult"),
                                    intent.getIntExtra("imageResult", R.drawable.ic_baseline_inventory_2_24),
                                    intent.getIntExtra("quantityResult", 0),
                                    intent.getStringExtra("descResult")));
                            break;

                        case REQUEST_CODE_DETAILS_UPDATE:
                            // Update the item in the database.
                            itemDB.updateItem(new InventoryItem(intent.getStringExtra("nameResult"),
                                    intent.getIntExtra("imageResult", R.drawable.ic_baseline_inventory_2_24),
                                    intent.getIntExtra("quantityResult", 0),
                                    intent.getStringExtra("descResult")));
                            break;

                        case REQUEST_CODE_DETAILS_DELETE:
                            itemDB.deleteItem(intent.getStringExtra("nameResult"));
                            break;
                    }
                }
            }
    );

}