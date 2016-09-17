package rtapps.app.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;

import com.rtapps.buisnessapp.R;


public class StoreWebsiteFragment extends WebViewFragment {



    public static StoreWebsiteFragment newInstance(String param1) {
        StoreWebsiteFragment fragment = new StoreWebsiteFragment();

        return fragment;
    }

    public StoreWebsiteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WebView webView = (WebView) super.onCreateView(inflater, container, savedInstanceState);

        String url = getString(R.string.store_url);
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U;`Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");


        webView.setWebViewClient(new myWebClient());

        return webView;
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }
}
