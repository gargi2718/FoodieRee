package com.example.foodieree.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.example.foodieree.R;
import com.example.foodieree.models.Reel;

public class OrderBottomSheetFragment extends BottomSheetDialogFragment {
    private final Reel reel;
    private static final String DEFAULT_CURRENCY = "$";

    public OrderBottomSheetFragment(Reel reel) {
        this.reel = reel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_order, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        // Initialize views
        TextView titleText = view.findViewById(R.id.titleText);
        TextView restaurantText = view.findViewById(R.id.restaurantText);
        TextView priceText = view.findViewById(R.id.priceText);
        MaterialButton orderButton = view.findViewById(R.id.orderButton);

        // Set data
        titleText.setText(reel.getFoodTitle());
        restaurantText.setText(String.format("%s • %s", reel.getRestaurantName(), reel.getDistance()));

        // Set dummy price (in a real app, this would come from your data model)
        String price = DEFAULT_CURRENCY + "9.99";
        priceText.setText(price);

        // Setup order button
        orderButton.setOnClickListener(v -> handleOrder());
    }

    private void handleOrder() {
        // TODO: Implement order processing
        // You could:
        // 1. Show a confirmation dialog
        // 2. Start a checkout activity
        // 3. Add to cart
        // 4. Make an API call to your backend

        // For now, just dismiss the bottom sheet
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        setupBottomSheetBehavior();
    }

    private void setupBottomSheetBehavior() {
        // You can customize the BottomSheet behavior here
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(true);
        }
    }
}