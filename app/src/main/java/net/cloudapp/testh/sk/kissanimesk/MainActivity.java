package net.cloudapp.testh.sk.kissanimesk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    WebView myWebView;
    private ActionMode mActionMode = null;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        final Context context = this;
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlraw) {
                if( !urlraw.contains("kissanime.to"))
                {
                    final String threadurlraw = urlraw;
                    Toast.makeText(context, "Loading ...", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(threadurlraw);
                    i.setDataAndType(uri, "video/*");
                    startActivity(i);

                    /*Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                HttpURLConnection connection = null;
                                try {
                                    Log.v("Hmm", "THread started and Url Connectingg ....");
                                    URL url = new URL(threadurlraw);
                                    connection = (HttpURLConnection) url.openConnection();
                                    connection.setRequestMethod("HEAD");
                                    connection.connect();
                                    Log.v("Hmm", "... and Url comp[leted ....");

                                    String contentType = connection.getContentType();
                                    Log.v("Hmm", "ContentType" + contentType);
                                    if( contentType.substring(0,5).contains("video"))
                                    {
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        Uri uri = Uri.parse(threadurlraw);
                                        i.setDataAndType(uri, "video/*");
                                        startActivity(i);
                                    }
                                    Log.v("Hmm","Thread completed...");
                                } catch (java.net.MalformedURLException e) {

                                } catch (java.net.ProtocolException e) {

                                } catch (java.io.IOException e) {

                                }
                            } catch (Exception e) {
                                Log.e("Hmm", e.getMessage());
                            }
                        }
                    });
                    thread.start();*/
                    return true;
                }
                return false;
            }
        });

        myWebView.setLongClickable(true);
        myWebView.loadUrl("http://kissanime.to/m/");
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (mActionMode == null) {
            mActionMode = mode;
            Menu menu = mode.getMenu();
            menu.clear();
            mActionMode.finish();

            WebView.HitTestResult result = myWebView.getHitTestResult();

            String url;
            if(result.getType()== WebView.HitTestResult.SRC_ANCHOR_TYPE )
            {
                url = result.getExtra();

                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(url);
                i.setDataAndType(uri, "video/*");
                startActivity(i);
            }
        }

        super.onActionModeStarted(mode);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        mActionMode = null;
        super.onActionModeFinished(mode);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {

            Intent newIntent = new Intent(this, CreditPrefs.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(newIntent);

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
}
