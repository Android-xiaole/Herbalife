package com.lansosdk.videoeditor;



import java.io.File;

import android.content.Context;
import android.os.Message;
import android.util.Log;

/**
 *  用来得到当前音视频中的各种信息,比如视频的宽高,码率, 帧率, 旋转角度,解码器信息,总时长,总帧数;音频的码率,采样率,解码器信息,总帧数等,为其他各种编辑处理为参考使用.
 *  
 *  
 *  暂时只支持一个音频和一个视频组成的多媒体文件,如MP4等,如果有多个音频,则音频数据是最后一个音频的info.
 *	
 *	如您的视频个格式比较特殊, 需要另外另外的方式, 请联系我们.
 *  如您的视频个格式比较特殊, 需要另外另外的方式, 请联系我们.
 *  
 *  使用方法是:创建对象, 执行prepare, 使用结果.
 *  可以在任意线程中执行如下:
 *  
 *  MediaInfo info=new MediaInfo(videoPath);
 *  
 *  if(info.prepare()){     //<==============主要是这里, 需要执行以下, 
 *    
 *    //可以使用MediaInfo中的各种成员变量, 比如vHeight, vWidth vBitrate等等.
 *    
 *  }else{
 *    
 *    //执行失败.....(大部分是视频路径不对,或Android6.0及以上设备没有打开权限导致)
 *    
 *  }
 */
public class MediaInfo {
//

	 private static final String TAG="MediaInfo";
	 private static final boolean VERBOSE = true; 
	 
	 /**
	  * 视频的显示高度,即正常视频高度. 如果视频旋转了90度或270度,则这里等于实际视频的宽度!!
	  */
	 public  int vHeight;
	 /**
	  * 视频的显示宽度, 即正常视频宽度. 如果视频旋转了90度或270度,则这里等于实际视频的高度,请注意!!
	  */
     public  int vWidth;
     /**
      *  视频在编码时的高度, 编码是以宏块为单位,默认是16的倍数;
      *  MediaCodec在使用中,也需要视频的宽高是16的倍数, 这两个宽高用来使用到MediaCodec中.   如果视频旋转了90度或270度,则这里等于编码高度和宽度调换
      *  
      *  如果没有获取到  vCodecHeight或vCodecWidth,说明当前文件中没有视频流, 但能获取vHeight/vWidth说明可能是一个mp3
      */
     public  int vCodecHeight;
     public  int vCodecWidth;    
     
     /**
      * 视频的码率,注意,一般的视频编码时采用的是动态码率VBR,故这里得到的是平均值, 建议在使用时,乘以1.2后使用.
      * 
      */
     public int vBitRate; 
     /**
      * 视频文件中的视频流总帧数.
      */
     public int vTotalFrames;  
     /**
      * mp4文件中的视频轨道的总时长, 注意,一个mp4文件可能里面的音频和视频时长不同.//单位秒.
      */
     public float vDuration;  
     /**
      * 视频帧率,可能有浮点数, 如用到MediaCodec中, 则需要(int)转换一下. 但如果要依此来计算时间戳, 尽量采用float类型计算, 这样可减少误差.
      */
     public float vFrameRate;
     /**
      * 视频旋转角度, 比如android手机拍摄的视频, 后置摄像头270度, 前置摄像头旋转了90度, 这个可以获取到. 如果需要进行画面, 需要测试下,是否需要宽度和高度对换下.
      * 正常的网上视频, 是没有旋转的. 
      */
     public float vRotateAngle;
     /**
      * 该视频是否有B帧, 即双向预测帧, 如果有的话, 在裁剪时需要注意, 目前大部分的视频不存在B帧.
      */
     public boolean vHasBFrame;
     /**
      * 视频可以使用的解码器,用来设置到{@link VideoEditor}中的各种需要编码器的场合使用.
      */
     public String vCodecName;
     /**
      * 视频的 像素格式.目前暂时没有用到.
      */
     public String vPixelFmt;
     
     /********************audio track info******************/
     
     /**
      * 音频采样率
      */
     public int aSampleRate;
     /**
      * 音频通道数量
      */
     public int aChannels;
     /**
      * 视频文件中的音频流 总帧数.
      */
     public int aTotalFrames;
     /**
      * 音频的码率,可用来检测视频文件中是否
      */
     public int aBitRate;
     /**
      * 音频的最大码率, 这里暂时没有用到.
      */
     public int aMaxBitRate;   
     /**
      * 多媒体文件中的音频总时长
      */
     public float aDuration;
     /**
      * 音频可以用的 解码器, 由此可以判定音频是mp3格式,还是aac格式,如果是mp3则这里"mp3"; 如果是aac则这里"aac";
      */
     public String  aCodecName;
    
     
     public final String filePath;
     public final String fileName; //视频的文件名, 路径的最后一个/后的字符串.
     public final String fileSuffix; //文件的后缀名.
     
     private boolean getSuccess=false;
     
     /**
      * 是否检测 硬件解码器,不检测会执行的快一些,默认为检测(如果您不是很明白,建议使用默认值,毕竟也只是慢30ms一下的事情.).
      * 
      *  一般在不需要解码的场合,可以设置为false,比如文件浏览器选择音视频文件, 音视频分离剪切的场合.
      */
     private boolean isCheckCodec=true;  //是否检测 
     /**
      * 构造方法, 输入文件路径; 
      * 注意: 创建对象后, 需要执行 {@link #prepare()}后才可以使用.
      * @param path
      */
     public MediaInfo(String path)
     {
    	 filePath=path;
    	 fileName=getFileNameFromPath(path);
    	 fileSuffix=getFileSuffix(path);
    	 isCheckCodec=true;
     }
     /**
      * 
      * @param path
      * @param checkCodec  默认是true,即检测解码器. 如不想检测解码器,则设置为false;这样执行prepare会快一些.
      */
     public MediaInfo(String path,boolean checkCodec)
     {
    	 filePath=path;
    	 fileName=getFileNameFromPath(path);
    	 fileSuffix=getFileSuffix(path);
    	 isCheckCodec=checkCodec;
     }
     /**
      * 准备当前媒体的信息
      * 
      * 去底层运行相关方法, 得到媒体信息.
      * @return  如获得当前媒体信息并支持格式,则返回true, 否则返回false;
      */
     public boolean prepare()
     {
    	int ret=0;
    	 if(fileExist(filePath)){ //这里检测下mfilePath是否是多媒体后缀.
    		 ret= nativePrepare(filePath,isCheckCodec);	 
    		 if(ret>=0){
    			 getSuccess=true;
    			 return isSupport();
    		 }else{   
    			 /**
    			  * 如果返回的值是-13, 请检查您的手机设备是否是Android6.0以上,并确定是否打开读写文件的授权.//很多客户是因为没有授权而失败.我们提供了PermissionsManager类来检测,可参考使用.
    			  * 
    			  */
    			 Log.e(TAG,"mediainfo prepare media is failed:"+filePath);
    			 return false;
    		 }
    	 }else{
    		 Log.e(TAG,"mediainfo prepare error . file is may be not exist!");
    		 return false;
    	 } 
     }
     public void release()
     {
    	 //TODO nothing 
    	 getSuccess=false;
     }
     public boolean isHaveAudio()
     {
    	 if(aBitRate>0)  //有音频
    	 {
    		 if(aChannels==0)
    			 return false;
    		 
    		 if(aCodecName==null || aCodecName.isEmpty())
    			 return false;
    		 
    	 }
    		 return true;
     }
     public boolean isHaveVideo()
     {
    	 if(vBitRate>0 || vWidth>0 ||vHeight>0)  
    	 {
    		 if(vHeight==0 || vWidth==0)
    		 {
    			 return false;
    		 }
    		 
    		 if(vCodecHeight==0 || vCodecWidth==0)
    		 {
    			 return false;
    		 }
    		 
    		 if(vFrameRate>60) //如果帧率大于60帧, 则不支持.  
    			 return false;
    		 
    		 if(vCodecName==null || vCodecName.isEmpty())
    			 return false;
    	 }
    	 return true;
     }
     /**
      * 传递过来的文件是否支持
      * 
      * @return
      */
     public boolean isSupport()
     {
    	 
    	 if(vBitRate>0 || vWidth>0 ||vHeight>0)  //有视频,
    	 {
    		 if(vHeight==0 || vWidth==0)
    		 {
    			 return false;
    		 }
    		 
    		 if(vFrameRate>60) //如果帧率大于60帧, 则不支持.  
    			 return false;
    		 
    		 if(vCodecName==null || vCodecName.isEmpty())
    			 return false;
    	 }else if(aBitRate>0)  //有音频
    	 {
    		 if(aChannels==0)
    			 return false;
    		 
    		 if(aCodecName==null || aCodecName.isEmpty())
    			 return false;
    		 
    	 }
   
    	 return true;
     }
     @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	 String info="file name:"+filePath+"\n";
    	 info+= "fileName:"+fileName+"\n";
    	 info+= "fileSuffix:"+fileSuffix+"\n";
    	 info+= "vHeight:"+vHeight+"\n";
    	 info+= "vWidth:"+vWidth+"\n";
    	 info+= "vCodecHeight:"+vCodecHeight+"\n";
    	 info+= "vCodecWidth:"+vCodecWidth+"\n";
    	 info+= "vBitRate:"+vBitRate+"\n";
    	 info+= "vTotalFrames:"+vTotalFrames+"\n";
    	 info+= "vDuration:"+vDuration+"\n";
    	 info+= "vFrameRate:"+vFrameRate+"\n";
    	 info+= "vRotateAngle:"+vRotateAngle+"\n";
    	 info+= "vHasBFrame:"+vHasBFrame+"\n";
    	 info+= "vCodecName:"+vCodecName+"\n";
    	 info+= "vPixelFmt:"+vPixelFmt+"\n";
    	 
    	 info+= "aSampleRate:"+aSampleRate+"\n";
    	 info+= "aChannels:"+aChannels+"\n";
    	 info+= "aTotalFrames:"+aTotalFrames+"\n";
    	 info+= "aBitRate:"+aBitRate+"\n";
    	 info+= "aMaxBitRate:"+aMaxBitRate+"\n";
    	 info+= "aDuration:"+aDuration+"\n";
    	 info+= "aCodecName:"+aCodecName+"\n";
    	 
    	if(getSuccess)
    		return info;
    	else
    	 return "MediaInfo is not ready.or call failed";
    }
     public native int nativePrepare(String filepath,boolean checkCodec);
     
     //used by JNI
     private void setVideoCodecName(String name)
     {
    	 this.vCodecName=name;
     }
     //used by JNI
     private void setVideoPixelFormat(String pxlfmt)
     {
    	 this.vPixelFmt=pxlfmt;
     }
     //used by JNI
     private void setAudioCodecName(String name)
     {
    	 this.aCodecName=name;
     }
     public static boolean isSupport(String videoPath)
     {
    	 if(fileExist(videoPath))
    	 {
    		 MediaInfo  info=new MediaInfo(videoPath,false);
        	 return  info.prepare();
    	 }else{
    		 if(VERBOSE)
    			 Log.i(TAG,"video:"+videoPath+" not support");
    		 return false;
    	 }
     }
     //-------------------------------文件操作-------------------------
     private static boolean fileExist(String absolutePath)
	 {
		 if(absolutePath==null)
			 return false;
		 else 
			 return (new File(absolutePath)).exists();
	 }
     
     private  String getFileNameFromPath(String path){
	        if (path == null)
	            return "";
	        int index = path.lastIndexOf('/');
	        if (index> -1)
	            return path.substring(index+1);
	        else
	            return path;
	    }
     private  String getFileSuffix(String path){
	        if (path == null)
	            return "";
	        int index = path.lastIndexOf('.');
	        if (index> -1)
	            return path.substring(index+1);
	        else
	            return "";
	}
     /*
      * ****************************************************************************
      * 测试
 //        new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				MediaInfo mif=new MediaInfo("/sdcard/2x.mp4"); //这里是我们的测试视频地址, 如您测试, 则需要修改视频地址.
//				mif.prepare();
//				Log.i(TAG,"mif is:"+ mif.toString());
//				mif.release();
//			}
//		},"testMediaInfo#1").start();
//  new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				MediaInfo mif=new MediaInfo("/sdcard/2x.mp4");//这里是我们的测试视频地址, 如您测试, 则需要修改视频地址.
//				mif.prepare();
//				Log.i(TAG,"mif is:"+ mif.toString());
//				mif.release();
//			}
//		},"testMediaInfo#2").start();
      */
}
