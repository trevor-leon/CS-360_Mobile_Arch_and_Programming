package com.example.inventory;

public class InventoryItem {

    // Model

    private String itemName;
    private int image;
    private int quantity;
    private String description;

    public InventoryItem(String name, int img, int quantity, String desc) {
        setItemName(name);
        setImage(img);
        setQuantity(quantity);
        setDescription(desc);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) { this.image = image; }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}