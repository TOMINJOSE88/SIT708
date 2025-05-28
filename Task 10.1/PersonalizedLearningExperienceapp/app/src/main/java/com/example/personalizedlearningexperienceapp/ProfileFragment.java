package com.example.personalizedlearningexperienceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;

public class ProfileFragment extends Fragment {

    private TextView txtUsername, txtEmail, txtNotification;
    private TextView txtTotalQuestions, txtCorrectAnswers, txtIncorrectAnswers;
    private Button btnShare;
    private ImageView qrImage;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Bind UI elements
        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtNotification = view.findViewById(R.id.txtNotification);
        txtTotalQuestions = view.findViewById(R.id.txtTotalQuestions);
        txtCorrectAnswers = view.findViewById(R.id.txtCorrectAnswers);
        txtIncorrectAnswers = view.findViewById(R.id.txtIncorrectAnswers);
        qrImage = view.findViewById(R.id.qrImage);
        btnShare = view.findViewById(R.id.btnShare);
        ImageButton btnBack = view.findViewById(R.id.btnBackProfile);

        // Load username from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Guest");

        // Load user stats and email from database
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(requireContext());
        String email = dbHelper.getEmailByUsername(username);
        int[] stats = dbHelper.getQuizStats(username);

        // Populate UI
        txtUsername.setText(username);
        txtEmail.setText(email != null ? email : "Not found");
        txtNotification.setText("🔔 You're now a premium user!");
        txtTotalQuestions.setText("📘\nTotal Questions\n" + stats[0]);
        txtCorrectAnswers.setText("✅\nCorrectly Answered\n" + stats[1]);
        txtIncorrectAnswers.setText("❌\nIncorrect Answers\n" + stats[2]);

        // Generate shareable text
        String shareText = username + " just answered " + stats[1] +
                " out of " + stats[0] + " questions correctly! 💪\nTry it yourself!";

        // Generate QR code from share text
        Bitmap qrBitmap = null;
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrBitmap = barcodeEncoder.encodeBitmap(shareText, BarcodeFormat.QR_CODE, 400, 400);
            qrImage.setImageBitmap(qrBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Share QR code as image
        Bitmap finalQrBitmap = qrBitmap;
        btnShare.setOnClickListener(v -> {
            if (finalQrBitmap != null) {
                try {
                    // Save QR image to cache
                    File cachePath = new File(requireContext().getCacheDir(), "images");
                    if (!cachePath.exists()) cachePath.mkdirs();
                    File file = new File(cachePath, "qr_share.png");

                    try (FileOutputStream stream = new FileOutputStream(file)) {
                        finalQrBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    }

                    Uri contentUri = FileProvider.getUriForFile(
                            requireContext(),
                            requireContext().getPackageName() + ".provider",
                            file
                    );

                    // Share image intent
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/png");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Share QR Code"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Back button functionality
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }
}
