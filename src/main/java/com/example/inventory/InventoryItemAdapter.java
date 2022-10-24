package com.example.inventory;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InventoryItemAdapter
        extends RecyclerView.Adapter<InventoryItemAdapter.ViewHolder> {

    private final Context context;
    private final InventoryDatabase itemDB;
    private final RecyclerViewClickListener listener;

    public InventoryItemAdapter(Context context, RecyclerViewClickListener listener) {
        this.context = context;
        itemDB = InventoryDatabase.getInstance(context);
        this.listener = listener;
    }


    @NonNull
    @Override
    public InventoryItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_grid_layout,
                parent, false);
        return new ViewHolder(view);
    }

    /*
    "...Display the data at the specified position. This method should update the contents of
    RecyclerView.ViewHolder.itemView to reflect the item at the given position."
     */
    @Override
    public void onBindViewHolder(@NonNull InventoryItemAdapter.ViewHolder holder, int position) {
        InventoryItem item = itemDB.getItems().get(position);
        holder.itemName.setText(context.getString(R.string.name, item.getItemName()));
        holder.itemImage.setImageResource(item.getImage());
        holder.itemQuantity.setText(context.getString(R.string.quantity, item.getQuantity()));

        // Minus button decrements the
        holder.minusButton.setOnClickListener(v -> {
            // Set the quantity text and DB to the new, decremented quantity by setting the text.
            holder.itemQuantity.setText(context.getString(R.string.quantity,
                    Integer.parseInt(holder.itemQuantity.getText().toString()) - 1));
        });

        holder.itemQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && Integer.parseInt(s.toString()) >= 0) {
                    // Update the quantity of the item where the name of the item is in the DB.
                    itemDB.updateQuantity(holder.itemName.getText().toString(),
                            Integer.parseInt(holder.itemQuantity.getText().toString()));
                    // If the updated quantity is <= the lowInventoryNo, send an SMS message.
                    // TODO: NOT IMPLEMENTED; RAN OUT OF TIME
                } else {
                    holder.itemQuantity.setText(context.getString(R.string.quantity,
                            0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.plusButton.setOnClickListener(v -> {
            // Update the quantity text and DB to the new, incremented quantity by setting the text.
            holder.itemQuantity.setText(context.getString(R.string.quantity,
                    Integer.parseInt(holder.itemQuantity.getText().toString()) + 1));
        });
    }

    /*
    This class sets the item attributes like name, image, and quantity to the view resources
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView itemName;
        private final ImageView itemImage;
        private final TextView itemQuantity;

        private final Button minusButton;
        private final Button plusButton;

        // Set the items in the ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Set the onClick listener to the onClick() method (this):
            itemView.setOnClickListener(this);
            itemName = itemView.findViewById(R.id.itemName);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemImage.setOnClickListener(this);
            itemQuantity = itemView.findViewById(R.id.quantityText);

            minusButton = itemView.findViewById(R.id.minusButton);
            plusButton = itemView.findViewById(R.id.plusButton);
        }


        // Set the onClick of each view in the ViewHolder.
        @Override
        public void onClick(View v) {
            listener.onClick(v, getAbsoluteAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return itemDB.length();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

}