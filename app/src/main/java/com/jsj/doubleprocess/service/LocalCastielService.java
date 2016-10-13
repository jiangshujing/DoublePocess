package com.jsj.doubleprocess.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.jsj.doubleprocess.R;
import com.jsj.doubleprocess.aidl.CastielProgressConnection;

/**
 * Created by jsj on 16/10/12.
 */

/**
 * @author 猴子搬来的救兵 http://blog.csdn.net/mynameishuangshuai
 * @ClassName: LocalCastielService
 * @Description: 本地服务
 */
public class LocalCastielService extends Service {

    MyBinder myBinder;
    private PendingIntent pintent;
    MyServiceConnection myServiceConnection;
    private CastielProgressConnection remoteService;		// 远程服务接口对象

    @Override
    public void onCreate() {
        super.onCreate();
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        myServiceConnection = new MyServiceConnection();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //绑定服务
        this.bindService(new Intent(this, RemoteCastielService.class), myServiceConnection, Context.BIND_IMPORTANT);
//        Notification notification = new Notification(R.mipmap.ic_launcher, "猴子服务启动中", System.currentTimeMillis());
//        pintent = PendingIntent.getService(this, 0, intent, 0);
//        notification.setLatestEventInfo(this, "猴子服务", "防止被杀掉！", pintent);

//        //使用通知推送消息
//        Notification.Builder builder = new Notification.Builder(this);
//        //设置消息属性
//        //必须设置的属性：小图标 标题 内容
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setContentTitle("猴子服务启动中");
//        builder.setContentText("猴子服务防止被杀掉！== 本地");
//        pintent = PendingIntent.getService(this, 0, intent, 0);
//        builder.setContentIntent(pintent);
//
//        //创建一个通知对象
//        Notification notification = builder.build();
//        //使用通知管理器发送一条通知
//        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(1,notification);



        Notification.Builder builder1 = new Notification.Builder(this);
        builder1.setSmallIcon(R.mipmap.ic_launcher); //设置图标
//        builder1.setContentTitle("通知"); //设置标题
//        builder1.setContentText("点击查看详细内容"); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
//        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        pintent = PendingIntent.getService(this, 0, intent, 0);
        builder1.setContentIntent(pintent);
        Notification notification1 = builder1.build();
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification1); // 通过通知管理器发送通知





        // 设置service为前台进程，避免手机休眠时系统自动杀掉该服务
        startForeground(startId, notification1);
        return START_STICKY;
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            remoteService = CastielProgressConnection.Stub.asInterface(arg1);
            String name = null;
            try {
                name = remoteService.getProName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.i("本地服务连接=== ", name);
            Log.i("castiel", "本地服务连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("远程服务 --- 断开 ",arg0.getClassName());
            // 连接出现了异常断开了，RemoteService被杀掉了
            Toast.makeText(LocalCastielService.this, "远程服务Remote被干掉", Toast.LENGTH_LONG).show();
            // 启动RemoteCastielService
            LocalCastielService.this.startService(new Intent(LocalCastielService.this, RemoteCastielService.class));
            LocalCastielService.this.bindService(new Intent(LocalCastielService.this, RemoteCastielService.class),
                    myServiceConnection, Context.BIND_IMPORTANT);
        }

    }

    class MyBinder extends CastielProgressConnection.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String getProName() throws RemoteException {
            return "Local猴子搬来的救兵";
        }

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return myBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑服务
        unbindService(myServiceConnection);
    }
}
