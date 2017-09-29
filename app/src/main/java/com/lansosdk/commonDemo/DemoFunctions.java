package com.lansosdk.commonDemo;

import android.content.Context;
import android.util.Log;

import com.lansosdk.videoeditor.CopyDefaultVideoAsyncTask;
import com.lansosdk.videoeditor.CopyFileFromAssets;
import com.lansosdk.videoeditor.MediaInfo;
import com.lansosdk.videoeditor.SDKDir;
import com.lansosdk.videoeditor.SDKFileUtils;
import com.lansosdk.videoeditor.VideoEditor;

public class DemoFunctions {
	
	public final static String TAG="DemoFunctions";
	/**
	 * 演示音频和视频合成, 也可以认为是音频替换.
	 * 
	 * 音视频合成:\n把一个纯音频文件和纯视频文件合并成一个mp4格式的多媒体文件, 如果源视频之前有音频,会首先删除音频部分. \n\n
	 */
	public static int demoAVMerge(Context ctx,VideoEditor editor,String srcVideo,String dstPath)
	{
		int ret=-1;
		MediaInfo info=new MediaInfo(srcVideo,false);
		if(info.prepare())
		{
			String video2=srcVideo;
			String video3=null;
			//如果源视频中有音频,则先删除音频
	  		if(info.isHaveAudio()){ 
	  			video3=SDKFileUtils.createFileInBox(info.fileSuffix);
	  			editor.executeDeleteAudio(video2, video3);
	  			
	  			video2=video3;
	  		}
	  		String audio=CopyDefaultVideoAsyncTask.copyFile(ctx,"aac20s.aac");
	  		ret=editor.executeVideoMergeAudio(video2,audio, dstPath);
	  		SDKFileUtils.deleteFile(video3);
		}

		return ret;
	}
	/**
	 * 演示音视频分离 
	 * 
	 * 音视频分离:\n把多媒体文件如mp4,flv等中的的音频和视频分离开,形成独立的音频文件和视频文件 \n\n
	 */
	public static int demoAVSplite(VideoEditor editor,String srcVideo,String dstVideo,String dstAudio)
	{
			MediaInfo   info=new MediaInfo(srcVideo);
			int ret=-1;
	    	if(info.prepare())
	    	{
	    		ret=editor.executeDeleteAudio(srcVideo, dstVideo);
	    		ret=editor.executeDeleteVideo(srcVideo, dstAudio);	
	    	}
	    	return ret;
	}
	/**
	 * 演示 视频截取
	 * 
	 * 视频剪切:\n剪切视频的长度,可以指定开始位置,指定结束位置.\n这里演示截取视频的前20秒或时长的一半,形成新的视频文件.
	 */
	public static int demoVideoCut(VideoEditor editor,String srcVideo,String dstVideo)
	{
			MediaInfo   info=new MediaInfo(srcVideo);
	    	if(info.prepare())
	    	{
	    		if(info.vDuration>20)
	    		 return	editor.executeVideoCutOut(srcVideo,dstVideo,0,20);
				else
					return	editor.executeVideoCutOut(srcVideo,dstVideo,0,info.aDuration/2);
	    	}
	    	return -1;
	}
	/**
	 * 演示音频截取
	 * 
	 * 音频剪切:\n剪切音频的长度,可以指定开始位置,指定结束位置.\n这里演示截取音频的前20秒或时长的一半,形成新的音频文件.
	 */
	public static int demoAudioCut(Context ctx,VideoEditor editor,String dstAudio)
	{
			String srcAudio=CopyDefaultVideoAsyncTask.copyFile(ctx,"niusanjin.mp3");
			MediaInfo   info=new MediaInfo(srcAudio);
	    	if(info.prepare() && info.aCodecName!=null)
	    	{
	    		if(info.aDuration>20)
	    			return 	editor.executeAudioCutOut(srcAudio,dstAudio,0,20);
				else
					return 	editor.executeAudioCutOut(srcAudio,dstAudio,0,info.aDuration/2);
	    	}else{
	    		return -1;
	    	}
	}
	/**
	 * 视频拼接 , 
	 * 
	 * 为了方便演示,需要您的视频大于20秒(或用默认视频).先把原视频从(0到1/3处)和(2/3到结束)截取成两段,这样就有了两个独立的视频, 然后把这两段拼接在一起,来演示视频的拼接, 
	 * 您实际可任意的组合,注意尽量视频的宽高比等参数一致,不然合成是可以,但有些播放器无法播放.
	 */
	public static int demoVideoConcat(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo   info=new MediaInfo(srcVideo);
		int ret=-1;
    	if(info.prepare() && info.vDuration>20)
    	{
    		//第一步:先创建三个视频文件,并剪切好视频.
    		String seg1=SDKFileUtils.createFileInBox(info.fileSuffix);
    		String seg2=SDKFileUtils.createFileInBox(info.fileSuffix);
    		
    		String segTs1=SDKFileUtils.createFileInBox("ts");
    		String segTs2=SDKFileUtils.createFileInBox("ts");
    		
    		
    		ret=editor.executeVideoCutOut(srcVideo,seg1, 0,info.vDuration/3);
    		ret=editor.executeVideoCutOut(srcVideo,seg2, info.vDuration*2/3,info.vDuration);
    		
    		
    		//第二步: 把他们转换为ts格式.
    		ret=editor.executeConvertMp4toTs(seg1, segTs1);
    		ret=editor.executeConvertMp4toTs(seg2, segTs2);
    		
    		//第三步: 把ts文件拼接成mp4
    		ret=editor.executeConvertTsToMp4(new String[]{segTs1,segTs2} , dstVideo);
    		
    		 //第四步: 删除之前的临时文件.
    		
    		 SDKFileUtils.deleteFile(segTs2);
    		 SDKFileUtils.deleteFile(segTs1);
    		 SDKFileUtils.deleteFile(seg2);
    		 SDKFileUtils.deleteFile(seg1);
    	}
    	return ret;
	}
	/**
	 *  演示视频压缩, 硬件实现 
	 *  
	 *  视频压缩转码:\n调整视频的码率,让视频文件大小变小一些,方便传输.注意:如调整的小太多,则会导致画面下降.这里演示把码率降低为70%\n
	 */
	public static int demoVideoCompress(VideoEditor editor,String srcVideo,String dstVideo)
	{
		return editor.executeVideoCompress(srcVideo, dstVideo, 0.7f);
	}
	/**
	 * 演示视频画面裁剪
	 * 
	 * 视频画面裁剪:裁剪视频的宽度和高度\n 这里演示从左上角裁剪视频高度和宽度为原来的一半保存为新的视频.\n
	 */
	public static int demoFrameCrop(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			float dstBr=(float)info.vBitRate;
	    	dstBr*=0.4f;
	    	int dstBr2=(int)dstBr;
	    	
	    	//检查宽高
	    	int width=info.vCodecWidth;
    		int height=info.vCodecHeight;
    		if(info.vRotateAngle==90 || info.vRotateAngle==270){
    			width=info.vCodecHeight;
    			height=info.vCodecWidth;
    		}
    		
    		return editor.executeVideoFrameCrop(srcVideo, width/2, height/2, 0, 0, dstVideo, info.vCodecName,dstBr2);
		}else{
			return -1;
		}
	}
	/**
	 * 视频画面缩放[软件缩放], 
	 * 
	 * 视频缩放:缩小视频的宽度和高度\n 这里演示把宽度和高度都缩小一半.\n 注意:这里是采用软缩放的形式来做,流程是:硬解码-->软件缩放-->硬编码
	 * 
	 */
	public static int demoVideoScale(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
		
			float f=(float)info.vBitRate;
			f*=0.7f;
			return editor.executeVideoFrameScale(srcVideo, info.vWidth/2, info.vHeight/2, dstVideo,(int)f);
		}else{
			return -1;
		}
	}
	/**
	 *  演示 叠加图片(水印)
	 *  
	 *  视频上增加图片:\n在视频上增加图片,可以指定开始时间和结束时间,指定在画面中的坐标处增加.
	 */	
	public static int demoAddPicture(Context ctx,VideoEditor editor,String srcVideo,String dstVideo)
	{
			MediaInfo info=new MediaInfo(srcVideo);
			if(info.prepare())
			{
				String imagePath="/sdcard/videoimage.png";
				CopyFileFromAssets.copy(ctx, "ic_launcher.png", "/sdcard", "videoimage.png");
				return editor.executeAddWaterMark(srcVideo, imagePath, 0, 0, dstVideo, (int)(info.vBitRate*1.2f));
			}else{
				return -1;
			}
	}
	/**
	 * 演示获得图片,保存到/sdcard下面.
	 * 
	 * 视频提取图片:\n把视频中的画面转换为图片, 可以全部指定图片,也可以每秒钟提取一帧,可以设置开始和结束时间
	 */
	public static int demoGetAllFrames(VideoEditor editor,String srcVideo)
	{
		MediaInfo   info=new MediaInfo(srcVideo);
    	if(info.prepare())
    	{
    		return editor.executeGetAllFrames(srcVideo,info.vCodecName,SDKDir.TMP_DIR,"img");
    	}else{
    		return -1;
    	}
	}
//	/**
//	 *  演示把一张图片转换为视频
//	 */
//	public static int demoOnePicture2Video(Context ctx,VideoEditor editor,String dstVideo)
//	{
//		String imagePath=SDKDir.TMP_DIR+"/threeword.png";
//		
//		CopyFileFromAssets.copy(ctx, "threeword.png", SDKDir.TMP_DIR, "threeword.png");
//		
//		Log.i(TAG,"demoOne picture to video :"+imagePath);
//		return editor.pictureFadeInOut(imagePath,5,0,40,50,75,dstVideo);
//	}

	
	/**
	 * 演示 先剪切, 再画面裁剪,再图片叠加. 
	 */
	public static int demoVideoCropOverlay(Context ctx,VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			String imagePath="/sdcard/videoimage.png";
			if(SDKFileUtils.fileExist(imagePath)==false){
				CopyFileFromAssets.copy(ctx, "ic_launcher.png", "/sdcard", "videoimage.png");	
			}
			 int cropW=240;
	    	 int cropH=240;
	    	 int max=Math.max(info.vWidth,info.vHeight);
	    	 
	    	 int cropMax=Math.max(cropW, cropH);
	    	 float dstBr=(float)info.vBitRate;
	    	 
	    	 float ratio=(float)cropMax/(float)max;
	    	 
	    	 dstBr*=ratio;  //得到恒定码率的等比例值.
	    	 dstBr*=0.8f; //再压缩20%.
	    	 
	    	return  editor.executeCropOverlay(srcVideo, info.vCodecName, imagePath, 20, 20, cropW, cropH, 0, 0, dstVideo, (int)dstBr);
		}else{
			return -1;
		}
	}
	/**
	 * 两个音频在混合时,第二个延时一定时间.
	 */
	public static int demoAudioDelayMix(Context ctx,VideoEditor editor,String dstAudio)
	{
		String audiostr1=CopyDefaultVideoAsyncTask.copyFile(ctx,"aac20s.aac");
		String audiostr2=CopyDefaultVideoAsyncTask.copyFile(ctx,"niusanjin.mp3");
		
		return editor.executeAudioDelayMix(audiostr1, audiostr2, 3000, 3000, dstAudio);
	}
	/**
	 * 两个音频在混合时调整音量
	 */
	public static int demoAudioVolumeMix(Context ctx,VideoEditor editor,String dstAudio)
	{
		String audiostr1=CopyDefaultVideoAsyncTask.copyFile(ctx,"aac20s.aac");
		String audiostr2=CopyDefaultVideoAsyncTask.copyFile(ctx,"niusanjin.mp3");
		
		return editor.executeAudioVolumeMix(audiostr1,audiostr2, 0.5f, 4, dstAudio);
	}
	/**
	 *垂直平镜像
	 */
	public static int demoVideoMirrorH(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			return editor.executeVideoMirrorH(srcVideo,info.vCodecName,bitrate,dstVideo);
		}else{
			return -1;
		}
	}
	/**
	 * 水平镜像
	 */
	public static int demoVideoMirrorV(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			return editor.executeVideoMirrorV(srcVideo,info.vCodecName,bitrate,dstVideo);
		}else{
			return -1;
		}
	}
	/**
	 * 视频逆向
	 */
	public static int demoVideoReverse(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			return editor.executeVideoReverse(srcVideo,info.vCodecName,bitrate,dstVideo);
		}else{
			return -1;
		}
		
	}
	/**
	 * 视频增加边框
	 */
	public static int demoPaddingVideo(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			int width=info.vCodecWidth+32;  //向外padding32个像素
			int height=info.vCodecHeight+32;
			
			return editor.executePadingVideo(srcVideo, info.vCodecName, width, height, 0, 0, dstVideo, (int)(info.vBitRate*1.2f));
		}else{
			return -1;
		}
	}
	/**
	 * 获取一张图片
	 */
	public static int demoGetOneFrame(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			String picPath=SDKFileUtils.createFileInBox("png");  //这里是保存的路径
			Log.i("lansosdk","picture save at "+picPath);
			return editor.executeGetOneFrame(srcVideo,info.vCodecName,info.vDuration/2,picPath);
		}else{
			return -1;
		}
	}
	/**
	 * 视频垂直方向反转
	 * @return
	 */
	public static int demoVideoRotateVertically(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			return editor.executeVideoRotateVertically(srcVideo, info.vCodecName, (int)(info.vBitRate*1.2f), dstVideo);
		}else{
			return -1;
		}
	}
	/**
	 *  视频水平方向反转
	 */
	public static int demoVideoRotateHorizontally(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			return editor.executeVideoRotateHorizontally(srcVideo, info.vCodecName, (int)(info.vBitRate*1.2f), dstVideo);
		}else{
			return -1;
		}
	}
	/**
	 *  视频顺时针旋转９０度
	 */
	public static int demoVideoRotate90Clockwise(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			return editor.executeVideoRotate90Clockwise(srcVideo, info.vCodecName, bitrate, dstVideo);
		}else{
			return -1;
		}
	}
	/**
	 * 视频逆时针旋转９０度,也即使顺时针旋转270度.
	 */
	public static int demoVideoRotate90CounterClockwise(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			return editor.executeVideoRotate90CounterClockwise(srcVideo, info.vCodecName, bitrate, dstVideo);
		}else{
			return -1;
		}
	}
	/**
	 * 视频和音频都逆序
	 */
	public static int demoAVReverse(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			return editor.executeAVReverse(srcVideo, info.vCodecName, bitrate, dstVideo);
		}else{
			return -1;
		}
	}
	/**
	 * 调整视频的播放速度. 
	 */
	public static int demoVideoAdjustSpeed(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			return editor.executeVideoAdjustSpeed(srcVideo, info.vCodecName,0.5f, bitrate, dstVideo);
		}else{
			return -1;
		}
	}
	/**
	 * 矫正视频的角度.
	 */
	public static int demoVideoZeroAngle(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare() &&info.vRotateAngle!=0)
		{
			int bitrate=(int)(info.vBitRate*1.2f);
			if(bitrate>2000*1000)
				bitrate=2000*1000; //2M
			
			return editor.executeVideoZeroAngle(srcVideo, info.vCodecName, bitrate, dstVideo);
		}else{
			return -1;
		}
	}
	/**
	 * 设置视频角度元数据.
	 */
	public static int demoSetVideoMetaAngle(VideoEditor editor,String srcVideo,String dstVideo)
	{
		MediaInfo info=new MediaInfo(srcVideo);
		if(info.prepare())
		{
			return editor.executeSetVideoMetaAngle(srcVideo,270,dstVideo);
		}else{
			return -1;
		}
	}
	
		
	
}
