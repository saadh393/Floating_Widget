package com.hasanAcademy.floatingwidget;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Floating_Widget_Service extends Service {

    private View view;
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;

    public Floating_Widget_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        view = LayoutInflater.from(this).inflate(R.layout.widget,null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        layoutParams.x = 0;
        layoutParams.y = 0;

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        assert windowManager != null;
        windowManager.addView(view,layoutParams);

        ImageView closeButton = (ImageView) view.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(view);
                stopSelf();

            }
        });



        view.findViewById(R.id.container).setOnTouchListener(new View.OnTouchListener() {
            private int x,y;
            private float touched_X, touched_Y;
            WindowManager.LayoutParams updatedParams = layoutParams;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x = layoutParams.x;
                        y = layoutParams.y;
                        touched_X = event.getRawX();
                        touched_Y = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        updatedParams.x = x + (int) (event.getRawX() - touched_X);
                        updatedParams.y = y + (int) (event.getRawY() - touched_Y);
                        windowManager.updateViewLayout(view, updatedParams);

                       return true;
                }

                return false;
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent,
                              int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
