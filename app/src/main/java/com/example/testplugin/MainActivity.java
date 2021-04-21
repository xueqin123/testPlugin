package com.example.testplugin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.annotion.MyAnnotation;

public class MainActivity extends Activity {
    private Button button1;
    private Button button2;
    private TextView textView;
    private String str = "testQinxueStr";
    private int num1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.btn_1);
        button2 = findViewById(R.id.btn_2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("按钮1 点击");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("按钮2 点击");
            }
        });
        textView = findViewById(R.id.test_view);
    }

    @MyAnnotation
    private boolean setQinxueText(String text, int position) {
        textView.setText(text);
        return false;
    }

}