package com.example.cheejin.fyp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class DietTips  extends AppCompatActivity {

    WebView webview;
    String gain, diet, maintain, dietExerc, gainExerc, maintainExerc;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_display);

        gain = (String) getIntent().getSerializableExtra("gain");
        diet = (String) getIntent().getSerializableExtra("diet");
        maintain = (String) getIntent().getSerializableExtra("maintain");
        dietExerc = (String) getIntent().getSerializableExtra("diet exercise");
        gainExerc = (String) getIntent().getSerializableExtra("gain exercise");
        maintainExerc = (String) getIntent().getSerializableExtra("maintain exercise");

        backBtn = findViewById(R.id.backBtn);
        webview = findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        if (gain != null) {
            webview.loadUrl("https://www.bodybuilding.com/fun/mohr60.htm");
        }else if (diet != null){
            webview.loadUrl("http://www.eatingwell.com/article/288643/14-day-clean-eating-meal-plan-1200-calories/");
        }else if (maintain != null){
            webview.loadUrl("http://www.eatingwell.com/article/289245/7-day-heart-healthy-meal-plan-1200-calories/");
        }else if (dietExerc != null){
            webview.loadUrl("https://www.stylecraze.com/articles/5-exercises-and-5-foods-to-reduce-belly-fat/#gref");
        }else if (gainExerc != null){
            webview.loadUrl("https://www.stylecraze.com/articles/exercises-that-help-you-gain-weight/#gref");
        }else if (maintainExerc != null){
            webview.loadUrl("https://www.daimanuel.com/2014/03/02/top-10-best-exercises-to-keep-you-healthy-and-fit/");
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
