package com.example.itubeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlayerFragment extends Fragment {

    public PlayerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView webView = view.findViewById(R.id.webViewPlayer);

        // ✅ Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient());  // ✅ Needed for HTML5 video
        webView.setWebViewClient(new WebViewClient());       // Keeps navigation in WebView

        String rawUrl = HomeFragment.currentVideoUrl;
        String embedUrl = convertToEmbedUrl(rawUrl);

        String html = "<!DOCTYPE html><html><body style='margin:0;padding:0;'>" +
                "<iframe width='100%' height='100%' src='" + embedUrl + "' " +
                "frameborder='0' allow='autoplay; encrypted-media' allowfullscreen></iframe>" +
                "</body></html>";

        webView.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null);
    }

    private String convertToEmbedUrl(String url) {
        if (url.contains("watch?v=")) {
            return url.replace("watch?v=", "embed/");
        } else if (url.contains("youtu.be/")) {
            return url.replace("youtu.be/", "www.youtube.com/embed/");
        }
        return url;
    }
}
