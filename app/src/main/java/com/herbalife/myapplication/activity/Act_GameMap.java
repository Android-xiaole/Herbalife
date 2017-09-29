package com.herbalife.myapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.herbalife.myapplication.R;
import com.herbalife.myapplication.fragment.Frag_GameMap1;
import com.herbalife.myapplication.fragment.Frag_GameMap2;
import com.herbalife.myapplication.fragment.Frag_GameMap3;
import com.herbalife.myapplication.fragment.Frag_GameMap4;
import com.herbalife.myapplication.fragment.Frag_GameMap5;
import com.herbalife.myapplication.fragment.Frag_GameMap6;
import com.herbalife.myapplication.fragment.Frag_GameMap7;
import com.herbalife.myapplication.fragment.Frag_GameMap8;
import com.herbalife.myapplication.fragment.Frag_GameMap9;
import com.sitsmice.herbalife_jar.base.BaseFragActivity;

/**
 * Created by sahara on 2016/12/13.
 */

public class Act_GameMap extends BaseFragActivity{
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gamemap);
        position = getIntent().getIntExtra("position",0);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        frags.add(new Frag_GameMap1());
        frags.add(new Frag_GameMap2());
        frags.add(new Frag_GameMap3());
        frags.add(new Frag_GameMap4());
        frags.add(new Frag_GameMap5());
        frags.add(new Frag_GameMap6());
        frags.add(new Frag_GameMap7());
        frags.add(new Frag_GameMap8());
        frags.add(new Frag_GameMap9());
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(position,false);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return frags.size();
        }

        @Override
        public Fragment getItem(int position) {
            return frags.get(position);
        }

    }

    public void goleft(){
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
    }

    public void goright(){
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }
}
