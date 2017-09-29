package com.sitsmice.herbalife_jar.utils;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

import com.sitsmice.herbalife_jar.Config;
import com.sitsmice.herbalife_jar.MLog;
import com.sitsmice.herbalife_jar.JarApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 */
public class UtilBitmap {
	public static BitmapFactory.Options options = Config.options;
	
	
	public static Bitmap getBitmap(String path,boolean isOptions) {
		if (path == null) {
			return null;
		}
		if (isOptions) {
			return BitmapFactory.decodeFile(path,options);
		}else {
			return BitmapFactory.decodeFile(path);
		}
	}
	public static Bitmap getBitmap(int id,boolean isOptions) {
		if (isOptions) {
			return BitmapFactory.decodeResource(JarApplication.mContext.getResources(), id,options);
		}else{
			return BitmapFactory.decodeResource(JarApplication.mContext.getResources(), id);
		}
	}
	public static BitmapDrawable getDrawable(int id,boolean isOptions) {
		return new BitmapDrawable(JarApplication.mContext.getResources(),getBitmap(id,isOptions));
	}
	public static BitmapDrawable getDrawable(String path,boolean isOptions) {
		return new BitmapDrawable(JarApplication.mContext.getResources(),getBitmap(path,isOptions));
	}
	/**
	 * 自己src目录下的图片 src/test.png
	 */
	public static Bitmap getBitmap(String src){
		src = "src/"+src;
		Bitmap bit = null;
		try {
			bit = BitmapFactory.decodeStream(UtilBitmap.class.getResourceAsStream(src));
	    } catch (Exception e) {
			MLog.e("test","UtilBitmap error:"+e.getMessage());
		}
		return bit;
	}
	
	//======================================
	//图片尺寸压缩长宽比例不变 
	//=============================================================================
	/**
	 * 获取图片的长度和宽度
	 */
	public static int[] getSize(String path){
		int i = options.inSampleSize;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = i;
		int[] ret = new int[]{options.outWidth,options.outHeight};
		return ret;
	}
    /**
     * 获取一个缩放后的bitmap
     */
	public static Bitmap getBitmap(String path, int reqWidth, int reqHeight) {
		int i = options.inSampleSize;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options = UtilBitmap.calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap ret = BitmapFactory.decodeFile(path, options);
		options.inSampleSize = i;
		return ret;
	}
	
	/**
	 * 计算压缩比例
	 */
	private static BitmapFactory.Options calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inSampleSize = 1;
		if (reqWidth<=0||reqHeight<=0) {
			return options;
		}
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			float heightRatio = Math.round((float) height / (float) reqHeight);
			float widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			options.inSampleSize = (int)Math.max(heightRatio, widthRatio);
		}
		// 设置压缩比例
		return options; 
	}
	
	//======================================
	//图片质量压缩
	//============================================================================
	/**
	 * 压缩图片（使用compress的方法） 
	 */
	public static Bitmap getBitmap(Bitmap bit,int reqWidth, int reqHeight){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int height = bit.getHeight();
		int width = bit.getWidth();
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			float heightRatio = Math.round((float) height / (float) reqHeight);
			float widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = (int)Math.max(heightRatio, widthRatio);
		}
		bit.compress(Bitmap.CompressFormat.JPEG, 100/inSampleSize, baos);
		byte[] b = baos.toByteArray();
		Bitmap ret = BitmapFactory.decodeByteArray(b, 0, b.length);
		recycled(bit);
		return ret;
	}
	/**
	 * 图片质量压缩（使用compress的方法） 
	 */
	public static Bitmap getBitmap(Bitmap bit,int quality){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bit.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		byte[] b = baos.toByteArray();
		Bitmap ret = BitmapFactory.decodeByteArray(b, 0, b.length);
		recycled(bit);
		return ret;
	}
	//==================================================================
    /***
     * 按长，宽(变型)缩放图片
     */
	public static Bitmap getBitmap(Bitmap src, double newWidth, double newHeight) {
		// 记录src的宽高
		float width = src.getWidth();
		float height = src.getHeight();
//		src = null;
		// 创建一个matrix容器
		Matrix matrix = new Matrix();
		// 计算缩放比例
		float scaleWidth =	((float)newWidth)/ width;
		float scaleHeight = ((float)newHeight)/ height;
		// 开始缩放
		matrix.postScale(scaleWidth, scaleHeight);
		// 创建缩放后的图片
		Bitmap ret = Bitmap.createBitmap(src, 0, 0, (int) width, (int) height, matrix, true);
		recycled(src);
		return ret;
	}
	//========================================================================== =========
	
	/**
     * 背景虚化方法1，仅在API 17以上的系统中才能使用ScriptIntrinsicBlur类
     * @param bkg
     *            将要虚化的图片
     * @param radius
     *            虚化度Supported range 0 < radius <= 25
     */
	@SuppressLint("NewApi")
	public static Bitmap blur(Bitmap bkg, float radius) {
		RenderScript rs = RenderScript.create(JarApplication.mContext);
		Allocation overlayAlloc = Allocation.createFromBitmap(rs, bkg);
		
		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
		blur.setInput(overlayAlloc);
		blur.setRadius(radius);
		blur.forEach(overlayAlloc);
		overlayAlloc.copyTo(bkg);
		rs.destroy();
		return bkg;
	}
	/**
	 * 背景虚化方法2
	 * http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
	 * Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
	 * 
	 * @param canReuseInBitmap 是否复制bitmap
	 */
    public static Bitmap blur2(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
    
    
	private static final short[] stackblur_mul = { 512, 512, 456, 512, 328, 456, 335, 512, 405, 328, 271, 456, 388, 335,
			292, 512, 454, 405, 364, 328, 298, 271, 496, 456, 420, 388, 360, 335, 312, 292, 273, 512, 482, 454, 428,
			405, 383, 364, 345, 328, 312, 298, 284, 271, 259, 496, 475, 456, 437, 420, 404, 388, 374, 360, 347, 335,
			323, 312, 302, 292, 282, 273, 265, 512, 497, 482, 468, 454, 441, 428, 417, 405, 394, 383, 373, 364, 354,
			345, 337, 328, 320, 312, 305, 298, 291, 284, 278, 271, 265, 259, 507, 496, 485, 475, 465, 456, 446, 437,
			428, 420, 412, 404, 396, 388, 381, 374, 367, 360, 354, 347, 341, 335, 329, 323, 318, 312, 307, 302, 297,
			292, 287, 282, 278, 273, 269, 265, 261, 512, 505, 497, 489, 482, 475, 468, 461, 454, 447, 441, 435, 428,
			422, 417, 411, 405, 399, 394, 389, 383, 378, 373, 368, 364, 359, 354, 350, 345, 341, 337, 332, 328, 324,
			320, 316, 312, 309, 305, 301, 298, 294, 291, 287, 284, 281, 278, 274, 271, 268, 265, 262, 259, 257, 507,
			501, 496, 491, 485, 480, 475, 470, 465, 460, 456, 451, 446, 442, 437, 433, 428, 424, 420, 416, 412, 408,
			404, 400, 396, 392, 388, 385, 381, 377, 374, 370, 367, 363, 360, 357, 354, 350, 347, 344, 341, 338, 335,
			332, 329, 326, 323, 320, 318, 315, 312, 310, 307, 304, 302, 299, 297, 294, 292, 289, 287, 285, 282, 280,
			278, 275, 273, 271, 269, 267, 265, 263, 261, 259 };

	private static final byte[] stackblur_shr = { 9, 11, 12, 13, 13, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 17, 17, 17,
			17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19,
			20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21,
			21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23,
			23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23,
			23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24,
			24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24,
			24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24 };
	/**
	 * 背景虚化方法3
	 */
	public Bitmap blur3(Bitmap original, float radius) {
		int w = original.getWidth();
		int h = original.getHeight();
		int[] currentPixels = new int[w * h];
		original.getPixels(currentPixels, 0, w, 0, 0, w, h);
		int cores = Runtime.getRuntime().availableProcessors();

		ArrayList<BlurTask> horizontal = new ArrayList<BlurTask>(cores);
		ArrayList<BlurTask> vertical = new ArrayList<BlurTask>(cores);
		
		for (int i = 0; i < cores; i++) {
			horizontal.add(new BlurTask(currentPixels, w, h, (int) radius, cores, i, 1));
			vertical.add(new BlurTask(currentPixels, w, h, (int) radius, cores, i, 2));
		}
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(cores);
		try {
			newFixedThreadPool.invokeAll(horizontal);
		} catch (InterruptedException e) {
			return null;
		}
		try {
			newFixedThreadPool.invokeAll(vertical);
		} catch (InterruptedException e) {
			return null;
		}

		return Bitmap.createBitmap(currentPixels, w, h, Bitmap.Config.ARGB_8888);
	}
	private static class BlurTask implements Callable<Void> {
		private final int[] _src;
		private final int _w;
		private final int _h;
		private final int _radius;
		private final int _totalCores;
		private final int _coreIndex;
		private final int _round;

		public BlurTask(int[] src, int w, int h, int radius, int totalCores, int coreIndex, int round) {
			_src = src;
			_w = w;
			_h = h;
			_radius = radius;
			_totalCores = totalCores;
			_coreIndex = coreIndex;
			_round = round;
		}

		@Override
		public Void call() throws Exception {
			blurIteration(_src, _w, _h, _radius, _totalCores, _coreIndex, _round);
			return null;
		}
		/**
		 * 
		 */
		private void blurIteration(int[] src, int w, int h, int radius, int cores, int core, int step) {
			int x, y, xp, yp, i;
			int sp;
			int stack_start;
			int stack_i;

			int src_i;
			int dst_i;

			long sum_r, sum_g, sum_b, sum_in_r, sum_in_g, sum_in_b, sum_out_r, sum_out_g, sum_out_b;

			int wm = w - 1;
			int hm = h - 1;
			int div = (radius * 2) + 1;
			int mul_sum = stackblur_mul[radius];
			byte shr_sum = stackblur_shr[radius];
			int[] stack = new int[div];

			if (step == 1) {
				int minY = core * h / cores;
				int maxY = (core + 1) * h / cores;

				for (y = minY; y < maxY; y++) {
					sum_r = sum_g = sum_b = sum_in_r = sum_in_g = sum_in_b = sum_out_r = sum_out_g = sum_out_b = 0;

					src_i = w * y; // start of line (0,y)

					for (i = 0; i <= radius; i++) {
						stack_i = i;
						stack[stack_i] = src[src_i];
						sum_r += ((src[src_i] >>> 16) & 0xff) * (i + 1);
						sum_g += ((src[src_i] >>> 8) & 0xff) * (i + 1);
						sum_b += (src[src_i] & 0xff) * (i + 1);
						sum_out_r += ((src[src_i] >>> 16) & 0xff);
						sum_out_g += ((src[src_i] >>> 8) & 0xff);
						sum_out_b += (src[src_i] & 0xff);
					}

					for (i = 1; i <= radius; i++) {
						if (i <= wm)
							src_i += 1;
						stack_i = i + radius;
						stack[stack_i] = src[src_i];
						sum_r += ((src[src_i] >>> 16) & 0xff) * (radius + 1 - i);
						sum_g += ((src[src_i] >>> 8) & 0xff) * (radius + 1 - i);
						sum_b += (src[src_i] & 0xff) * (radius + 1 - i);
						sum_in_r += ((src[src_i] >>> 16) & 0xff);
						sum_in_g += ((src[src_i] >>> 8) & 0xff);
						sum_in_b += (src[src_i] & 0xff);
					}

					sp = radius;
					xp = radius;
					if (xp > wm)
						xp = wm;
					src_i = xp + y * w; // img.pix_ptr(xp, y);
					dst_i = y * w; // img.pix_ptr(0, y);
					for (x = 0; x < w; x++) {
						src[dst_i] = (int) ((src[dst_i] & 0xff000000) | ((((sum_r * mul_sum) >>> shr_sum) & 0xff) << 16)
								| ((((sum_g * mul_sum) >>> shr_sum) & 0xff) << 8)
								| ((((sum_b * mul_sum) >>> shr_sum) & 0xff)));
						dst_i += 1;

						sum_r -= sum_out_r;
						sum_g -= sum_out_g;
						sum_b -= sum_out_b;

						stack_start = sp + div - radius;
						if (stack_start >= div)
							stack_start -= div;
						stack_i = stack_start;

						sum_out_r -= ((stack[stack_i] >>> 16) & 0xff);
						sum_out_g -= ((stack[stack_i] >>> 8) & 0xff);
						sum_out_b -= (stack[stack_i] & 0xff);

						if (xp < wm) {
							src_i += 1;
							++xp;
						}

						stack[stack_i] = src[src_i];

						sum_in_r += ((src[src_i] >>> 16) & 0xff);
						sum_in_g += ((src[src_i] >>> 8) & 0xff);
						sum_in_b += (src[src_i] & 0xff);
						sum_r += sum_in_r;
						sum_g += sum_in_g;
						sum_b += sum_in_b;

						++sp;
						if (sp >= div)
							sp = 0;
						stack_i = sp;

						sum_out_r += ((stack[stack_i] >>> 16) & 0xff);
						sum_out_g += ((stack[stack_i] >>> 8) & 0xff);
						sum_out_b += (stack[stack_i] & 0xff);
						sum_in_r -= ((stack[stack_i] >>> 16) & 0xff);
						sum_in_g -= ((stack[stack_i] >>> 8) & 0xff);
						sum_in_b -= (stack[stack_i] & 0xff);
					}

				}
			} else if (step == 2) {
				int minX = core * w / cores;
				int maxX = (core + 1) * w / cores;

				for (x = minX; x < maxX; x++) {
					sum_r = sum_g = sum_b = sum_in_r = sum_in_g = sum_in_b = sum_out_r = sum_out_g = sum_out_b = 0;

					src_i = x; // x,0
					for (i = 0; i <= radius; i++) {
						stack_i = i;
						stack[stack_i] = src[src_i];
						sum_r += ((src[src_i] >>> 16) & 0xff) * (i + 1);
						sum_g += ((src[src_i] >>> 8) & 0xff) * (i + 1);
						sum_b += (src[src_i] & 0xff) * (i + 1);
						sum_out_r += ((src[src_i] >>> 16) & 0xff);
						sum_out_g += ((src[src_i] >>> 8) & 0xff);
						sum_out_b += (src[src_i] & 0xff);
					}
					for (i = 1; i <= radius; i++) {
						if (i <= hm)
							src_i += w; // +stride

						stack_i = i + radius;
						stack[stack_i] = src[src_i];
						sum_r += ((src[src_i] >>> 16) & 0xff) * (radius + 1 - i);
						sum_g += ((src[src_i] >>> 8) & 0xff) * (radius + 1 - i);
						sum_b += (src[src_i] & 0xff) * (radius + 1 - i);
						sum_in_r += ((src[src_i] >>> 16) & 0xff);
						sum_in_g += ((src[src_i] >>> 8) & 0xff);
						sum_in_b += (src[src_i] & 0xff);
					}

					sp = radius;
					yp = radius;
					if (yp > hm)
						yp = hm;
					src_i = x + yp * w; // img.pix_ptr(x, yp);
					dst_i = x; // img.pix_ptr(x, 0);
					for (y = 0; y < h; y++) {
						src[dst_i] = (int) ((src[dst_i] & 0xff000000) | ((((sum_r * mul_sum) >>> shr_sum) & 0xff) << 16)
								| ((((sum_g * mul_sum) >>> shr_sum) & 0xff) << 8)
								| ((((sum_b * mul_sum) >>> shr_sum) & 0xff)));
						dst_i += w;

						sum_r -= sum_out_r;
						sum_g -= sum_out_g;
						sum_b -= sum_out_b;

						stack_start = sp + div - radius;
						if (stack_start >= div)
							stack_start -= div;
						stack_i = stack_start;

						sum_out_r -= ((stack[stack_i] >>> 16) & 0xff);
						sum_out_g -= ((stack[stack_i] >>> 8) & 0xff);
						sum_out_b -= (stack[stack_i] & 0xff);

						if (yp < hm) {
							src_i += w; // stride
							++yp;
						}

						stack[stack_i] = src[src_i];

						sum_in_r += ((src[src_i] >>> 16) & 0xff);
						sum_in_g += ((src[src_i] >>> 8) & 0xff);
						sum_in_b += (src[src_i] & 0xff);
						sum_r += sum_in_r;
						sum_g += sum_in_g;
						sum_b += sum_in_b;

						++sp;
						if (sp >= div)
							sp = 0;
						stack_i = sp;

						sum_out_r += ((stack[stack_i] >>> 16) & 0xff);
						sum_out_g += ((stack[stack_i] >>> 8) & 0xff);
						sum_out_b += (stack[stack_i] & 0xff);
						sum_in_r -= ((stack[stack_i] >>> 16) & 0xff);
						sum_in_g -= ((stack[stack_i] >>> 8) & 0xff);
						sum_in_b -= (stack[stack_i] & 0xff);
					}
				}
			}
		}
	}

//	/**
//	 * 根据滤镜类型获取图片
//	 * @param filterType
//	 *            滤镜类型
//	 * @param defaultBitmap
//	 *            默认图片
//	 * @return
//	 */
//	public static Bitmap getFilter(FilterType filterType, Bitmap defaultBitmap) {
//		if (filterType.equals(FilterType.默认)) {
//			return defaultBitmap;
//		} else if (filterType.equals(FilterType.LOMO)) {
//			return lomoFilter(defaultBitmap);
//		}
//		return defaultBitmap;
//	}
	/**
	 * 颜色矩阵（ColorMatrix）过滤器
	 * http://blog.csdn.net/tianjian4592/article/details/44336949
	 */
	public static ColorFilter getColorMatrix(float a, float r, float g, float b) {  
		ColorMatrix mColorMatrix = new ColorMatrix(new float[] {  
	            r, 0, 0, 0, 0,  
	            0, g, 0, 0, 0,  
	            0, 0, b, 0, 0,  
	            0, 0, 0, a, 0,  
	    });  
		return new ColorMatrixColorFilter(mColorMatrix);
	}
	/**
	 * 滤镜效果--LOMO
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap lomoFilter(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int dst[] = new int[width * height];
		bitmap.getPixels(dst, 0, width, 0, 0, width, height);

		int ratio = width > height ? height * 32768 / width : width * 32768 / height;
		int cx = width >> 1;
		int cy = height >> 1;
		int max = cx * cx + cy * cy;
		int min = (int) (max * (1 - 0.8f));
		int diff = max - min;

		int ri, gi, bi;
		int dx, dy, distSq, v;

		int R, G, B;

		int value;
		int pos, pixColor;
		int newR, newG, newB;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = y * width + x;
				pixColor = dst[pos];
				R = Color.red(pixColor);
				G = Color.green(pixColor);
				B = Color.blue(pixColor);

				value = R < 128 ? R : 256 - R;
				newR = (value * value * value) / 64 / 256;
				newR = (R < 128 ? newR : 255 - newR);

				value = G < 128 ? G : 256 - G;
				newG = (value * value) / 128;
				newG = (G < 128 ? newG : 255 - newG);

				newB = B / 2 + 0x25;

				// ==========边缘黑暗==============//
				dx = cx - x;
				dy = cy - y;
				if (width > height)
					dx = (dx * ratio) >> 15;
				else
					dy = (dy * ratio) >> 15;

				distSq = dx * dx + dy * dy;
				if (distSq > min) {
					v = ((max - distSq) << 8) / diff;
					v *= v;

					ri = (int) (newR * v) >> 16;
					gi = (int) (newG * v) >> 16;
					bi = (int) (newB * v) >> 16;

					newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
					newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
					newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
				}
				// ==========边缘黑暗end==============//

				dst[pos] = Color.rgb(newR, newG, newB);
			}
		}

		Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
		return acrossFlushBitmap;
	}
    /**
     * 更改图片色系，变亮或变暗 <br>
     * <b>注意</b> src实际并没有被回收，如果你不需要，请手动置空
     * @param delta
     *            图片的亮暗程度值，越小图片会越亮，取值范围(0,24)
     * @return
     */
	public static Bitmap tone(Bitmap src, int delta) {
		if (delta >= 24 || delta <= 0) {
			return null;
		}
		// 设置高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
		int width = src.getWidth();
		int height = src.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixColor = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int idx = 0;
		int[] pixels = new int[width * height];

		src.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);
						
						newR += (pixR * gauss[idx]);
						newG += (pixG * gauss[idx]);
						newB += (pixB * gauss[idx]);
						idx++;
					}
				}
				newR /= delta;
				newG /= delta;
				newB /= delta;
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
    /**
     * 将彩色图转换为黑白图 <br>
     * <b>注意</b> bmp实际并没有被回收，如果你不需要，请手动置空
     * @param bmp
     *            位图
     * @return 返回转换好的位图
     */
	public static Bitmap convertToBlackWhite(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);

		int alpha = 0xFF << 24; // 默认将bitmap当成24色图片
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];

				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
				grey = alpha | (grey << 16) | (grey << 8) | grey;
				pixels[width * i + j] = grey;
			}
		}
		Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
		return newBmp;
	}

    /**
     * 读取图片属性：图片被旋转的角度
     * @param path
     *            图片绝对路径
     * @return 旋转的角度
     */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	/**
	 * 纠正图片角度（有些相机拍照后相片会被系统旋转）
	 * @param path
	 *            图片路径
	 */
	public static Bitmap correctPictureAngle(String path) {
		int angle = readPictureDegree(path);
		if (angle != 0) {
			return rotate(angle, BitmapFactory.decodeFile(path));
		}
		return BitmapFactory.decodeFile(path);
	}
    /**
     * 旋转图片<br>
     * 
     * <b>注意</b> bitmap实际并没有被回收，如果你不需要，请手动置空
     * 
     * @param angle
     *            旋转角度
     * @param bitmap
     *            要旋转的图片
     * @return 旋转后的图片
     */
	public static Bitmap rotate(int angle, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
	
	/**
	 * @return 返回指定笔的指定字符串的长度
	 */
	public static float getFontlength(Paint paint, String str) {
		return paint.measureText(str);
	}
	/**
	 * @return 返回指定笔的文字高度
	 */
	public static float getFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.descent - fm.ascent;
	}
	/**
	 * @return 返回指定笔离文字顶部的基准距离
	 */
	public static float getFontLeading(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.leading - fm.ascent;
	}
	

	//===================================================================================
    /**
     * 回收一个未被回收的Bitmap
     */
    public static void recycled(Bitmap bitmap) {
    	if (bitmap == null) {
			return ;
		}
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
    /**
     * 回收一个Drawable含有未被回收的Bitmap
     */
    public static void recycled(Drawable d){
    	if (d == null) {
			return ;
		}
    	BitmapDrawable bd;
		try {
			bd = (BitmapDrawable)d;
			recycled(bd.getBitmap());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	d = null;
    	bd = null;
    }
    /**
     * 回收一个View的Background (ImageView 不应该此方法)含有未被回收的Bitmap
     */
    public static void recycled(View v){
    	if (v == null) {
			return ;
		}
    	recycled(v.getBackground());
    	v = null;
    }
    /**
     * 回收一个ImageView含有未被回收的Bitmap
     */
    public static void recycled(ImageView iv){
    	if (iv == null) {
			return ;
		}
    	recycled(iv.getBackground());
    	recycled(iv.getDrawable());
    	iv = null;
    }
}
