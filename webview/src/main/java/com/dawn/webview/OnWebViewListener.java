package com.dawn.webview;

public interface OnWebViewListener {
    void getWebViewParam(String value);//发送参数后返回数据
    void loadUrlSuccess();//加载成功
}
