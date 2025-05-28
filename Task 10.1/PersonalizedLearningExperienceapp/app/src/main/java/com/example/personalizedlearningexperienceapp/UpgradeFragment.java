package com.example.personalizedlearningexperienceapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UpgradeFragment extends Fragment {

    public UpgradeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upgrade, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btnBack = view.findViewById(R.id.btnBackUpgrade);
        Button btnStarter = view.findViewById(R.id.btnStarter);
        Button btnIntermediate = view.findViewById(R.id.btnIntermediate);
        Button btnAdvanced = view.findViewById(R.id.btnAdvanced);

        // Back button
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Stripe test URLs for each plan
        String starterUrl = "https://buy.stripe.com/test_bJebJ0gK5cqL3aG2GTbfO00"; // Replace with your actual test link
        String intermediateUrl = "https://buy.stripe.com/test_6oU4gydxTeyTcLg2GTbfO01"; // Replace with your actual test link
        String advancedUrl = "https://buy.stripe.com/test_4gM4gy8dz4Yjh1wftFbfO02"; // Replace with your actual test link

        btnStarter.setOnClickListener(v -> launchStripePayment(starterUrl));
        btnIntermediate.setOnClickListener(v -> launchStripePayment(intermediateUrl));
        btnAdvanced.setOnClickListener(v -> launchStripePayment(advancedUrl));
    }

    private void launchStripePayment(String url) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PaymentWebViewFragment(url))
                .addToBackStack(null)
                .commit();
    }
}
