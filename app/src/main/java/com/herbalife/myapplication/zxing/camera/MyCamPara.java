package com.herbalife.myapplication.zxing.camera;

import android.hardware.Camera;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sahara on 2017/4/19.
 */

public class MyCamPara {

    private static final String tag = "yan";
    private CameraSizeComparator sizeComparator = new CameraSizeComparator();
    private static MyCamPara myCamPara = null;
    private MyCamPara(){

    }
    public static MyCamPara getInstance(){
        if(myCamPara == null){
            myCamPara = new MyCamPara();
            return myCamPara;
        }
        else{
            return myCamPara;
        }
    }

    public Camera.Size getPreviewSize(List<Camera.Size> list, int th){
        Collections.sort(list, sizeComparator);
        for(Camera.Size s:list){
            if((s.width > th) && equalRate(s, 1.33f)){
                Log.i(tag, "最终设置预览尺寸:w = " + s.width + "h = " + s.height);
                return s;
            }
        }
        return null;
    }

    public Camera.Size getPreviewSize2(List<Camera.Size> list, int width,int heigh){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Camera.Size s:list){
            if((s.width > width) && equalRate(s, 1.33f)){
                Log.i(tag, "最终设置预览尺寸:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }

        return list.get(i);
    }
    public Camera.Size getPictureSize(List<Camera.Size> list, int th){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Camera.Size s:list){
            if((s.width > th) && equalRate(s, 1.33f)){
                Log.i(tag, "最终设置图片尺寸:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }

        return list.get(i);
    }

    public boolean equalRate(Camera.Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.2)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public  class CameraSizeComparator implements Comparator<Camera.Size> {
        //按升序排列
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // TODO Auto-generated method stub
            if(lhs.width == rhs.width){
                return 0;
            }
            else if(lhs.width > rhs.width){
                return 1;
            }
            else{
                return -1;
            }
        }

    }

}
