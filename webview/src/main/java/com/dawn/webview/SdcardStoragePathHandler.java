package com.dawn.webview;


import android.webkit.MimeTypeMap;
import android.webkit.WebResourceResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.webkit.WebViewAssetLoader;

import java.io.FileInputStream;


//首先要集成androidx.webkit:webkit:1.4.0
public class SdcardStoragePathHandler implements WebViewAssetLoader.PathHandler {
    @Nullable
    @Override
    public WebResourceResponse handle(@NonNull String filePath) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        try {
            return new WebResourceResponse(mimeType, "UTF-8", new FileInputStream(filePath));
        } catch (Exception e) {
            return null;
        }
    }
}
