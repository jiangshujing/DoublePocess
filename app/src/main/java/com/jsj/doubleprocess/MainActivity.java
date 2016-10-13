package com.jsj.doubleprocess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jsj.doubleprocess.service.LocalCastielService;
import com.jsj.doubleprocess.service.RemoteCastielService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 启动本地服务和远程服务
        startService(new Intent(this, LocalCastielService.class));
        startService(new Intent(this, RemoteCastielService.class));
    }
}
