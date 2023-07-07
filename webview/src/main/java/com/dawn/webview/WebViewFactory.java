package com.dawn.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;

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

    /**
     * js发送指令到安卓
     * @param param 发送的参数
     */
    @JavascriptInterface
    public void sendParam(String param){

    }

}
