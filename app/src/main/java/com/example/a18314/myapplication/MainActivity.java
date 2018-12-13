package com.example.a18314.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.baidu.mapapi.map.MapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewpager;
    private ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
    private RadioGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        group = (RadioGroup) findViewById(R.id.group);
        fragmentArrayList.add(new MyContact());
        viewpager.setAdapter(new Myadapter(getSupportFragmentManager()));
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.message:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.contact:
                        viewpager.setCurrentItem(1);
                        break;
                    case R.id.dynamic_state:
                        viewpager.setCurrentItem(2);
                        break;
                }
            }
        });
    }


    class Myadapter extends FragmentPagerAdapter {


        public Myadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
