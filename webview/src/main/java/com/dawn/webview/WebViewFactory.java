package com.dawn.webview;

import android.content.Context;

public class WebViewFactory {
    private Context mContext;
    //单例模式
    private static WebViewFactory webViewFactory;
    private WebViewFactory(Context context){
        mContext = context;
    }

    public static WebViewFactory getInstance(Context context){
        if(webViewFactory == null)
            webViewFactory = new WebViewFactory(context);
        return webViewFactory;
    }

}
