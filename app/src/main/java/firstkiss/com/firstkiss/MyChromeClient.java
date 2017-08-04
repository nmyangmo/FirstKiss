package firstkiss.com.firstkiss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;

import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;

import java.io.File;

/**
 * 作者：yangmo on 2017/7/8 09:53
 * 邮箱：nmyangmo@126.com
 */
public class MyChromeClient  extends WebChromeClient {
    public ValueCallback<Uri> UploadMsg;
    public ValueCallback<Uri[]> UploadMsg2;
    private Activity activity;

    public static final int FILECHOOSER_RESULTCODE = 5173;

    public static String mCameraFilePath = "";

    @SuppressWarnings("deprecation")
    public MyChromeClient(Activity cordova) {

        this.activity = cordova;

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

        super.onProgressChanged(view, newProgress);

    }


    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }


    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        new AlertDialog.Builder(activity).setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }


    @Override
    public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
        geolocationPermissionsCallback.invoke(s, true, true);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
        super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
    }

    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded, long l1, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(spaceNeeded * 2);
    }

    // <input type="file" name="fileField" id="fileField" />

    // Android > 4.1.1

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(view);
        resultMsg.sendToTarget();
        return true;
    }
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        // TODO 自动生成的方法存根
        UploadMsg2 = filePathCallback;
        this.activity.startActivityForResult(createDefaultOpenableIntent(),

                this.FILECHOOSER_RESULTCODE);
        return false;
    }

    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg,

                                String acceptType, String capture) {

        UploadMsg = uploadMsg;

        this.activity.startActivityForResult(createDefaultOpenableIntent(),

                this.FILECHOOSER_RESULTCODE);

    }

    // 3.0 +

    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

        UploadMsg = uploadMsg;

        this.activity.startActivityForResult(createDefaultOpenableIntent(),

                this.FILECHOOSER_RESULTCODE);

    }

    // Android < 3.0

    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {

        UploadMsg = uploadMsg;

        this.activity.startActivityForResult(createDefaultOpenableIntent(),

                this.FILECHOOSER_RESULTCODE);

    }

    private Intent createDefaultOpenableIntent() {

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        i.addCategory(Intent.CATEGORY_OPENABLE);

        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                "image/*");

        Intent chooser = createChooserIntent(createCameraIntent()/*
                                                                 *
                                                                 * ,
                                                                 *
                                                                 * createCamcorderIntent
                                                                 *
                                                                 * (),
                                                                 *
                                                                 * createSoundRecorderIntent
                                                                 *
                                                                 * ()
                                                                 */);

        chooser.putExtra(Intent.EXTRA_INTENT, i);

        return chooser;

    }

    private Intent createChooserIntent(Intent... intents) {

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);

        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);

        chooser.putExtra(Intent.EXTRA_TITLE, "选择图片");

        return chooser;

    }

    @SuppressWarnings("static-access")
    private Intent createCameraIntent() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        File cameraDataDir = new File(externalDataDir.getAbsolutePath()

                + File.separator + "515aaa");

        cameraDataDir.mkdirs();

        String mCameraFilePath = cameraDataDir.getAbsolutePath()

                + File.separator + System.currentTimeMillis() + ".jpg";

        this.mCameraFilePath = mCameraFilePath;

        cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,

                Uri.fromFile(new File(mCameraFilePath)));

        return cameraIntent;

    }

    /*
     *
     * private Intent createCamcorderIntent() { return new
     *
     * Intent(MediaStore.ACTION_VIDEO_CAPTURE); }
     *
     *
     *
     * private Intent createSoundRecorderIntent() { return new
     *
     * Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION); }
     */

    public static Uri getImageContentUri(Context context, java.io.File imageFile) {

        String filePath = imageFile.getAbsolutePath();

        Cursor cursor = context.getContentResolver().query(

                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                new String[] { MediaStore.Images.Media._ID },

                MediaStore.Images.Media.DATA + "=? ",

                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {

            int id = cursor.getInt(cursor

                    .getColumnIndex(MediaStore.MediaColumns._ID));

            Uri baseUri = Uri.parse("content://media/external/images/media");

            return Uri.withAppendedPath(baseUri, "" + id);

        } else {

            if (imageFile.exists()) {

                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.DATA, filePath);

                return context.getContentResolver().insert(

                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } else {

                return null;

            }

        }

    }

}
