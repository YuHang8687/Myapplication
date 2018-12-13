package com.example.a18314.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    private int count=0;
    private ViewPager vp;
    private Handler handler;
    private ArrayList<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);
        vp = (ViewPager) findViewById(R.id.vp);
        list.add(R.drawable.qq1);
        list.add(R.drawable.qq1);
        vp.setAdapter(new Myadapter());
        handler=new Handler();
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                startActivity(new Intent(Main2Activity.this,LoginMainActivity.class));
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                count++;
                vp.setCurrentItem(count);
            }
        },3000);
    }

    class Myadapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(Main2Activity.this).inflate(R.layout.image, null);
            ImageView image = view. findViewById(R.id.image);
            image.setImageResource(list.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
