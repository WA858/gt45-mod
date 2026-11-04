package com.g45.pro.modmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "GT45");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl("file:///android_asset/gt45_login.html");
        setContentView(webView);
    }

    public class WebAppInterface {
        Activity mContext;
        WebAppInterface(Activity c) { mContext = c; }

        @JavascriptInterface
        public void login(String key) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Key Verified Successfully!", Toast.LENGTH_SHORT).show();
                    webView.loadUrl("file:///android_asset/gt45_home.html");
                }
            });
        }

        @JavascriptInterface
        public void startProxy() {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Proxy Tunnel Activated!", Toast.LENGTH_SHORT).show();
                    Intent serviceIntent = new Intent(mContext, TProxyService.class);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        mContext.startForegroundService(serviceIntent);
                    } else {
                        mContext.startService(serviceIntent);
                    }
                }
            });
        }

        @JavascriptInterface
        public void launchGameWithFloat(final String game) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final String targetPkg = game.equals("ffmax") ? "com.dts.freefiremax" : "com.dts.freefireth";
                    Toast.makeText(mContext, "Launching & Activating Float for: " + game.toUpperCase(), Toast.LENGTH_SHORT).show();
                    
                    Intent serviceIntent = new Intent(mContext, TProxyService.class);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        mContext.startForegroundService(serviceIntent);
                    } else {
                        mContext.startService(serviceIntent);
                    }

                    try {
                        Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(targetPkg);
                        if (launchIntent != null) {
                            mContext.startActivity(launchIntent);
                        } else {
                            Toast.makeText(mContext, "Target game package not found!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @JavascriptInterface
        public void close() {
            mContext.finishAffinity();
        }
    }
}
