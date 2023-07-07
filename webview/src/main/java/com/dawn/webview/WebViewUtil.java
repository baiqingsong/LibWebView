package com.dawn.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.webkit.WebViewAssetLoader;

public class WebViewUtil {
    private boolean webViewFinish = false;//webView加载完成
    /**
     * web view 加载
     */
    @SuppressLint("JavascriptInterface")
    protected void loadWebView(Context context, WebView webView, String url, String paramJson, OnWebViewListener listener) {
        //要嵌套的uniapp页面所在安卓下的路径(也就是assets下的路径，记得
        //assets路径就是要写成android_asset,不要改)
//        String url = "file:///android_asset/ui/h5/index.html";
//        String url = "file://" + getExternalFilesDir(null).getAbsolutePath() + "/other/index.html";
        WebSettings webSettings = webView.getSettings();
        //可以访问https
        webSettings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //开启JavaScript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //下面这些都是设置，自行结合项目需要删减
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        String dir = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.supportMultipleWindows();
        webSettings.setAllowContentAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        //这一行代码很重要，先往下看，这个就是调用传值的方法，后面会解释
        webView.addJavascriptInterface(WebViewFactory.getInstance(context),"webViewFactory");
        //加载url
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            private WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder().addPathHandler("/sdcard/", new SdcardStoragePathHandler()).build();
            @Override
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受所有网站的证书
                super.onReceivedSslError(view, handler, error);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());

            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return assetLoader.shouldInterceptRequest(Uri.parse(url));
            }
        });
        webViewFinish = false;
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress >= 100){
                    if(webViewFinish)
                        return;
                    webViewFinish = true;
                    if(!TextUtils.isEmpty(paramJson)) {
                        //发送参数
                        webView.evaluateJavascript("javascript:pageStartParam('" + paramJson + "')", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                if(listener != null)
                                    listener.getWebViewParam(value);
                            }

                        });
                    }
                    if(listener != null)
                        listener.loadUrlSuccess();
                }

            }
        });
    }
}
