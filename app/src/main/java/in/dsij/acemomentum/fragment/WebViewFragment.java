package in.dsij.acemomentum.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import in.dsij.acemomentum.R;
import in.dsij.acemomentum.view.MyTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    private static final String ARG_URL = "WebViewFragment.Arg.ARG_URL";
    private static final String ARG_HEADER = "WebViewFragment.Arg.ARG_HEADER";
    private static final String LOG_TAG = "WebViewFragment";

    private String url;
    private String headerText;

    private WebView mWebView;
    private MyTextView tvHeader;
    private ProgressBar progressBar;

    public static WebViewFragment newInstance(@NonNull String url, @Nullable String headerText) {

        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_HEADER, headerText);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getArguments().getString(ARG_URL);
        headerText = getArguments().getString(ARG_HEADER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        findViews(rootView);

        setViews();
        setEventListeners();

        return rootView;
    }

    private void setViews() {
        if (TextUtils.isEmpty(headerText)) {
            tvHeader.setText("DSIJ Trader");
        } else {
            tvHeader.setText(headerText);
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return false;
            }
        });

        mWebView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        showProgressBar(false);
//                        showEmptyView(true);
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        showProgressBar(true);

                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        showProgressBar(false);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        if (url.startsWith("mailto:")) {
                            MailTo mt = MailTo.parse(url);

                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mt.getTo(), null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, mt.getSubject());
                            emailIntent.putExtra(Intent.EXTRA_TEXT, mt.getBody());
                            try {
                                startActivity(emailIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.w(LOG_TAG, "Email Client Not Found", e);
                                Snackbar.make(view, "! Email Client Not Found", Snackbar.LENGTH_SHORT).show();
                            }
                        } else if (url.startsWith("http://www.capitalfirst.com/")) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(i);
                        } else {
                            view.loadUrl(url);
                        }

                        return true;
                    }
                }

        );

        mWebView.setDownloadListener(
                new DownloadListener() {
                    public void onDownloadStart(String url, String userAgent,
                                                String contentDisposition, String mimetype,
                                                long contentLength) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
        );
    }

    private void findViews(View rootView) {
        mWebView = (WebView) rootView.findViewById(R.id.webView);
        tvHeader = (MyTextView) rootView.findViewById(R.id.tvHeader);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setEventListeners() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    showProgressBar(false);
                }
            }
        });
    }
}
