package com.example.skill573.notification;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    private int shareURL;
    public String[] title;
    public String[] content;
    public String prb;

    private WebView mWebView;
    private WebView noti ;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000; //10 seconds 搜尋頻率 1S:1000
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.wv);
        mWebView.loadUrl("http://140.128.80.192:8001/home");
        mWebView.getSettings().setJavaScriptEnabled(true);
        //瀏覽介面

        noti=(WebView)findViewById(R.id.noti);
        noti.loadUrl("http://140.128.80.192:8001/noti");
        noti.getSettings().setJavaScriptEnabled(true);
        //推播接收媒介

        mHandler = new Handler();
        bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
            //檢查是否支援藍芽
        if (!getPackageManager().hasSystemFeature
                    (PackageManager.FEATURE_BLUETOOTH_LE)) {
                Toast.makeText(this, "硬體不支援", Toast.LENGTH_SHORT).show();
                finish(); }
            // 檢查手機是否開啟藍芽裝置
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "請開啟藍芽裝置", Toast.LENGTH_SHORT).show();
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT); }
                else {
                scanLeDevice(true);
            }
    };

    private class JsOperation  //從網頁取值
    {
        @JavascriptInterface
        public void responseID(String result)
        {
            shareURL = Integer.parseInt(result);
            //儲存 數量
        }
        public void responseTitle(String[] result)
        {
            for(int a=1; a<=shareURL ;a++){
                title[a]=result[a];
            }
            //儲存 標題
        }
        public void responssContent(String[] result)
        {
            for(int a=1; a<=shareURL ;a++){
                content[a]=result[a];
            }
            //儲存 內文
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
    }
    // 掃描藍芽裝置
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     final byte[] scanRecord) {
                    // 搜尋回饋
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                    if(device.getName()==prb){
                        notificationManager.cancelAll();//清理舊的通知資料
                        noti.addJavascriptInterface(new JsOperation(), device.getName());//接收通知數量
                        int input=shareURL;
                        for (int a=1 ;a<=input ;a++){
                            int notifyID = a; // 通知的識別號碼
                            Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.test).setContentTitle(title[a]).setContentText(content[a]).build(); // 建立通知
                            notificationManager.notify(notifyID, notification); // 發送通知
                        }
                        prb=device.getName();
                    }
                }
            };

}
