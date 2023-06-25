package com.example.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class InventoryDatabase extends SQLiteOpenHelper {

    // Model

    private static final int VERSION = 1; // v number of the DB
    private static final String DATABASE_NAME = "inventory.db";

    private static InventoryDatabase inventoryDatabase;

    //public enum ItemSortOrder { ALPHABETIC, QUANTITY_ASC, QUANTITY_DESC };

    public static InventoryDatabase getInstance(Context context) {
        // If the inventory needs to be initialized, init it
        if(inventoryDatabase == null) {
            inventoryDatabase = new InventoryDatabase(context);
        }
        return inventoryDatabase;
    }

    private InventoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class ItemTable {
        private static final String TABLE = "Inventory";
        private static final String COL_NAME = "Name";
        private static final String COL_IMAGE = "Image";
        private static final String COL_QUANTITY = "Quantity";
        private static final String COL_DESCRIPTION = "Description";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create table Inventory (Name text PRIMARY KEY, Image integer NOT NULL,
        // Quantity integer NOT NULL, Description text NOT NULL)
        db.execSQL("create table " + ItemTable.TABLE + " (" +
                ItemTable.COL_NAME + " text PRIMARY KEY, " +
                ItemTable.COL_IMAGE + " integer NOT NULL, " +
                ItemTable.COL_QUANTITY + " integer NOT NULL, " +
                ItemTable.COL_DESCRIPTION + " text NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Recreate the table.
        db.execSQL("drop table if exists " + ItemTable.TABLE);
        onCreate(db);
    }

    // CREATE
    public long addItem(InventoryItem item) {
        SQLiteDatabase wDatabase = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemTable.COL_NAME, item.getItemName());
        values.put(ItemTable.COL_IMAGE, item.getImage());
        values.put(ItemTable.COL_QUANTITY, item.getQuantity());
        values.put(ItemTable.COL_DESCRIPTION, item.getDescription());

        // Return the id returned by the insert method (-1 if an error occurred).
        long id = wDatabase.insert(ItemTable.TABLE, null, values);
        wDatabase.close();
        return id;
    }

    // READ
    public ArrayList<InventoryItem> getItems() {
        ArrayList<InventoryItem> items = new ArrayList<>();

        SQLiteDatabase rDatabase = this.getReadableDatabase();

        // select * from Inventory
        String sql = "select * from " + ItemTable.TABLE;
        Cursor cursor = rDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                // Cursor cycles and creates items to return
                InventoryItem inventoryItem = new InventoryItem(cursor.getString(0),
                        cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3));
                items.add(inventoryItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        rDatabase.close();
        return items;
    }

    // UPDATE
    public void updateItem(InventoryItem item) {
        SQLiteDatabase wDatabase = getWritableDatabase();

        ContentValues values = new ContentValues();
        // TODO: possibly remove values.put(ItemTable.COL_NAME, item.getItemName());
        values.put(ItemTable.COL_IMAGE, item.getImage());
        values.put(ItemTable.COL_QUANTITY, item.getQuantity());
        values.put(ItemTable.COL_DESCRIPTION, item.getDescription());

        wDatabase.update(ItemTable.TABLE, values, ItemTable.COL_NAME + " = ?",
                new String[] {item.getItemName()});
        wDatabase.close();
    }

    // DELETE
    public void deleteItem(String name) {
        SQLiteDatabase wDatabase = getWritableDatabase();

        wDatabase.delete(ItemTable.TABLE, ItemTable.COL_NAME + " = ?",
                new String[] {name});
        wDatabase.close();
    }

    protected boolean contains(String name) {
        SQLiteDatabase rDatabase = getReadableDatabase();

        String sql = "select * from " + ItemTable.TABLE + " where " + ItemTable.COL_NAME + " = ?";
        Cursor cursor = rDatabase.rawQuery(sql, new String[] {name});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public void updateQuantity(String name, int quantity) {
        SQLiteDatabase wDatabase = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(ItemTable.COL_QUANTITY, quantity);

        wDatabase.update(ItemTable.TABLE, value, ItemTable.COL_NAME + " = ?",
                new String[] {name});
    }

    // return the size of the list of items
    public int length() {
        return getItems().size();
    }

}
