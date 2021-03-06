package net.cloudapp.testh.sk.kissanimesk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    WebView myWebView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final java.lang.String INST_DATA_URL = "net.cloudapp.testh.sk.kissanimesk.url";
    private static final java.lang.String INST_DATA_SCROLLY = "net.cloudapp.testh.sk.kissanimesk.posy";

    Map<String, String> extraHeaders = new HashMap<String, String>();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = null;
        if (myWebView != null)
        {
            webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        final Context context = this;
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlraw) {
                if( urlraw.contains("googlevideo.com"))
                {
                    Toast.makeText(context, "Loading ...", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(urlraw);
                    i.setDataAndType(uri, "video/*");
                    startActivity(i);
                }
                else
                {
                    view.loadUrl(urlraw, extraHeaders);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                if( !url.contains("kissanime.to/m")
                        && !url.contains("kissanime.to/M")
                        && !url.contains("kissanime.ru/M")
                        && !url.contains("kissanime.ru/m"))
                {
                    view.loadUrl("http://kissanime.to/m/", extraHeaders);
                }

                String baseUrl = "http://kissanime.to/";
                try
                {
                    URL durl = new URL(myWebView.getUrl());
                    baseUrl = durl.getProtocol() + "://" + durl.getHost();
                }
                catch (MalformedURLException e)
                { }

                String cookieString = "playerTypeMobile=skPlayer; path=/";
                //Log.v("CookieTesting", CookieManager.getInstance().getCookie(baseUrl).toString());
                CookieManager.getInstance().setCookie(baseUrl, cookieString);
            }
        });

        myWebView.setLongClickable(true);
        extraHeaders.put("X-Requested-With", "XMLHttpRequest");
        myWebView.loadUrl("http://kissanime.to/m/", extraHeaders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_info) {

            Intent newIntent = new Intent(this, CreditPrefs.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(newIntent);

            return true;
        }
        else if ( id == R.id.action_home)
        {
            myWebView.loadUrl("http://kissanime.to/m/", extraHeaders);
            Toast.makeText(this, "Loading Home...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        outState.putString(INST_DATA_URL, myWebView.getUrl());
        outState.putInt(INST_DATA_SCROLLY, myWebView.getScrollY());
        Log.v("KissAnimeSK", "Save " + myWebView.getUrl() + ", " + myWebView.getScrollY());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        myWebView.loadUrl(savedInstanceState.getString(INST_DATA_URL), extraHeaders);
        myWebView.setScrollY(savedInstanceState.getInt(INST_DATA_SCROLLY));

        Log.v("KissAnimeSK", "Set " + savedInstanceState.getString(INST_DATA_URL) + ", " + savedInstanceState.getInt(INST_DATA_SCROLLY));
    }
}
