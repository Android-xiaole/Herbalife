package com.herbalife.myapplication.zxing.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.herbalife.myapplication.ImageRes;
import com.herbalife.myapplication.MyApplication;
import com.herbalife.myapplication.R;
import com.herbalife.myapplication.camera.model.Addon;
import com.herbalife.myapplication.zxing.camera.CameraManager;
import com.herbalife.myapplication.zxing.decoding.CaptureActivityHandler;
import com.herbalife.myapplication.zxing.decoding.InactivityTimer;
import com.herbalife.myapplication.zxing.view.ViewfinderView;
import com.sitsmice.herbalife_jar.MLog;
import com.sitsmice.herbalife_jar.base.BaseActivity;
import com.sitsmice.herbalife_jar.utils.UtilObjectIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;

import static com.sitsmice.herbalife_jar.utils.UtilObjectIO.readObject;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class Act_ZXing extends BaseActivity implements Callback {

	private ViewfinderView viewfinderView;
	private SignFailPopwindow signFailPopwindow;

	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private List<ImageRes> list = new ArrayList<>();
	private List<String> listCode = new ArrayList<>();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ButterKnife.inject(this);
		setContentView(R.layout.camera);
		initUI();
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		list = readObject(ArrayList.class, MyApplication.mDownLoadPath);
		for (ImageRes imageRes:list) {
			listCode.add(imageRes.code);
		}
	}

	@Override
	public void initUI() {
		super.initUI();
		auto_FrameL.setBackgroundColor(getResources().getColor(R.color.none));
		title_text.setVisibility(View.GONE);
		auto_menu.setVisibility(View.GONE);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
	}


	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		//处理扫描结果
		onResultHandler(resultString,barcode);
	}

	/**
	 * 处理扫描结果
	 * @param resultString
	 * @param bitmap
	 */
	private void onResultHandler(String resultString, Bitmap bitmap) {
		System.out.println("scan Result:"+resultString);
		if (TextUtils.isEmpty(resultString)) {
			Toast.makeText(Act_ZXing.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			return;
		}
		MLog.e("test", "扫描二维码返回：" + resultString);
		if (listCode.contains(resultString)){
			for (ImageRes imageRes:list) {
				if (resultString.equals(imageRes.code)){
					if (!imageRes.isLight){//如果包含，并且未解锁过，那就返回提示解锁成功
						imageRes.isLight = true;
						UtilObjectIO.writeObject(list,MyApplication.mDownLoadPath);
						Intent intent = new Intent();
						intent.putExtra("ImageRes",imageRes);
						setResult(1,intent);
						finish();
						//解锁贴纸和相框
						int position = Integer.valueOf(imageRes.code);
						ArrayList<Addon> tiezhiList = UtilObjectIO.readObject(ArrayList.class,MyApplication.mDownLoadPath,"tiezhi");
						tiezhiList.get(position+6).isLight = true;
						UtilObjectIO.writeObject(tiezhiList,MyApplication.mDownLoadPath,"tiezhi");
						ArrayList<Addon> xkList = UtilObjectIO.readObject(ArrayList.class,MyApplication.mDownLoadPath,"xiangkuang");
						xkList.get(position*2-1).isLight = true;
						xkList.get(position*2).isLight = true;
						UtilObjectIO.writeObject(xkList,MyApplication.mDownLoadPath,"xiangkuang");
					}else{//解锁过的就提示：已经解锁过啦！
						if (signFailPopwindow == null){
							signFailPopwindow = new SignFailPopwindow(this);
						}
						signFailPopwindow.setTitle("当前场景已解锁");
						signFailPopwindow.showPopwindow(auto_icon);
					}
				}
			}
		}else{//无效的二维码
			if (signFailPopwindow == null){
				signFailPopwindow = new SignFailPopwindow(this);
			}
			signFailPopwindow.setTitle("无效的二维码");
			signFailPopwindow.showPopwindow(auto_icon);
		}
		MyApplication.post(new Runnable() {
			@Override
			public void run() {
				if (signFailPopwindow!=null){
					signFailPopwindow.dismissPop();
				}
				if (handler != null) {
					handler.quitSynchronously();
					handler = null;
				}
				CameraManager.get().closeDriver();
				SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
				SurfaceHolder surfaceHolder = surfaceView.getHolder();
				if (hasSurface) {
					initCamera(surfaceHolder);
				} else {
					surfaceHolder.addCallback(Act_ZXing.this);
					surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
				}
				decodeFormats = null;
				characterSet = null;

				playBeep = true;
				AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
				if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
					playBeep = false;
				}
				vibrate = true;
				initBeepSound();
			}
		},2000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
//		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		CameraManager.get().closeDriver();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (signFailPopwindow!=null&&signFailPopwindow.isShowing()){
			signFailPopwindow.dismissPop();
			return;
		}
//		signFailPopwindow.backgroundAlpha(this,(float) 1.0);
		super.onBackPressed();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}