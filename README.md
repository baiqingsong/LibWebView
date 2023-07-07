# LibWebView
 WebView的引用


#### WebViewUtil
WebView的工具类，用于创建WebView

* loadWebView 加载WebView
* pageStartParam js接收参数

#### OnWebViewListener
WebView的监听器

* getWebViewParam 加载参数的返回数据
* loadUrlSuccess 加载成功

#### WebViewFactory
WebView的工厂类，用于创建WebView
用于传递参数，接口

```
    /**
    * 安卓发送指令到js
    */
    public void sendCommand(WebView webView, String command){
        if(webView != null)
            webView.loadUrl("javascript:sendCommand('" + command + "')");
    }
```
