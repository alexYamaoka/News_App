package com.example.news_app.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.news_app.R;


public class DetailedArticleFragment extends Fragment
{

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detailed_article, container, false);


        webView = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.progressBar);


        Intent intent = getActivity().getIntent();


        String imageUrl = intent.getStringExtra("imageUrl");
        String author = intent.getStringExtra("author");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String date = intent.getStringExtra("date");
        String publishedTime = intent.getStringExtra("publishedTime");
        String source = intent.getStringExtra("source");
        String url = intent.getStringExtra("url");



        webView.getSettings().setJavaScriptEnabled(true);       // enable javascript
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(url);


        return view;
    }
}
