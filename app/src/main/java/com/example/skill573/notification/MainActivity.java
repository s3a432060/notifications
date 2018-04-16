package com.example.skill573.notification;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    public String prb;
    //推播儲存變數
    private WebView mWebView;
    private WebView noti ;
    //連接網頁介質
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    //連結藍芽介質
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000; //10 seconds 搜尋頻率 1S:1000
    private Handler mHandler;
    private Button but;//虛擬機測試用物件
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prb="test1";
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mWebView = (WebView) findViewById(R.id.wv);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://140.128.80.192:8001/noti");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                mWebView.loadUrl("javascript:test.test()");
            }
        });
        mWebView.addJavascriptInterface(new JsOperation(), "test1");
        //瀏覽介面
        noti=(WebView)findViewById(R.id.noti);
        noti.getSettings().setJavaScriptEnabled(true);
        noti.loadUrl("http://140.128.80.192:8001/noti");
        noti.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                noti.loadUrl("javascript:test.test()");
            }
        });

        but =(Button)findViewById(R.id.butt);//虛擬機測試用方法
        but.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                ; //主動觸發網頁溝通
                if(prb=="test1")
                    prb="test2";
                else
                    prb="test1";
                //接收通知數量
            }});
        tv=(TextView)findViewById(R.id.TV);
/*
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
            */
    };
    private class JsOperation  //從網頁取值
    {
        @JavascriptInterface
        public void responseID(String ID,String title,String context,int number)
        {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
            notificationManager.cancelAll();//清理舊的通知資料
            int notifyID = number; // 通知的識別號碼
            Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.test).setContentTitle(title).setContentText(context).build(); // 建立通知
            notificationManager.notify(notifyID, notification); // 發送通知
        }
    }
    }
    /*
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
*/

