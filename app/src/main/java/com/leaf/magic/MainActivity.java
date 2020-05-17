package com.leaf.magic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.leaf.magic.api.Magic;

public class MainActivity extends Activity {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        textView3 = findViewById(R.id.text3);
        textView4 = findViewById(R.id.text4);
        textView5 = findViewById(R.id.text5);
        frameLayout = findViewById(R.id.content_frame);
        init();
    }

    private void init() {
        MyTestProvider myTestProvider1 = (MyTestProvider) Magic.getInstance().get(MyTestProvider.class);
        if (myTestProvider1 != null) {
            Log.i("Magic", "myTestProvider1 get: " + myTestProvider1.getCount());
            textView1.setText(myTestProvider1.getCount());
        }


        MyTestProvider myTestProvider2 = (MyTestProvider) Magic.getInstance().get(MyTestProvider.class);
        if (myTestProvider2 != null) {
            Log.i("Magic", "myTestProvider2 get: " + myTestProvider2.getCount());
            textView2.setText(myTestProvider2.getCount());

        }

        MyTestProvider myTestProvider3 = (MyTestProvider) Magic.getInstance().create(MyTestProvider.class);
        if (myTestProvider3 != null) {
            Log.i("Magic", "myTestProvider3 create: " + myTestProvider3.getCount());
            textView3.setText(myTestProvider3.getCount());

        }

        MyTestProvider myTestProvider4 = (MyTestProvider) Magic.getInstance().create(MyTestProvider.class, 100);
        if (myTestProvider4 != null) {
            Log.i("Magic", "myTestProvider4 type=100 create: " + myTestProvider4.getCount());
            textView4.setText(myTestProvider4.getCount());
        }

//        DemoProvider demoProvider = (DemoProvider) Magic.getInstance().get(DemoProvider.class);
//        if (demoProvider != null) {
//            Log.i("Magic", "is other module getDemoName: " + demoProvider.getDemoName());
//            textView5.setText(demoProvider.getDemoName());
//        }

        BaseViewHolder baseViewHolder = (BaseViewHolder) Magic.getInstance().createViewHolder(BaseViewHolder.class, 100, getLayoutInflater(), frameLayout, false);
        if (baseViewHolder != null) {
            baseViewHolder.bindView("titleForTest", 0);
            frameLayout.addView(baseViewHolder.itemView);
        }
    }
}
