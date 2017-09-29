package com.herbalife.myapplication.camera.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.herbalife.myapplication.R;
import com.herbalife.myapplication.camera.AppConstants;
import com.herbalife.myapplication.camera.CameraBaseActivity;
import com.herbalife.myapplication.camera.customview.PagerSlidingTabStrip;
import com.herbalife.myapplication.camera.fragment.AlbumFragment;
import com.herbalife.myapplication.camera.model.Album;
import com.herbalife.myapplication.camera.util.FileUtils;
import com.herbalife.myapplication.camera.util.ImageUtils;
import com.herbalife.myapplication.camera.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 相册界面
 * Created by sky on 2015/7/8.
 * Weibo: http://weibo.com/2030683111
 * Email: 1132234509@qq.com
 */
public class AlbumActivity extends CameraBaseActivity {

    private Map<String, Album> albums;
    private List<String> paths = new ArrayList<String>();

    @InjectView(R.id.indicator)
    PagerSlidingTabStrip tab;
    @InjectView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.inject(this);
        albums = ImageUtils.findGalleries(this, paths, 0);
        //ViewPager的adapter
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tab.setViewPager(pager);

        initTitle();
        title_text.setText("请先选择一张图片");
        auto_menu.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent result) {
        if (requestCode == AppConstants.REQUEST_CROP && resultCode == RESULT_OK) {
            Intent newIntent = new Intent(this, PhotoProcessActivity.class);
            newIntent.setData(result.getData());
            startActivity(newIntent);
        }
    }


    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            return AlbumFragment.newInstance(albums.get(paths.get(position)).getPhotos());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Album album = albums.get(paths.get(position % paths.size()));
            if (StringUtils.equalsIgnoreCase(FileUtils.getInst().getSystemPhotoPath(),
                    album.getAlbumUri())) {
                return "本地相册";
            } else if (album.getTitle().length() > 13) {
                return album.getTitle().substring(0, 11) + "...";
            }
            return album.getTitle();
        }

        @Override
        public int getCount() {
            return paths.size();
        }
    }
}
