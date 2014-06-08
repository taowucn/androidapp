package com.robot.agent;

import java.io.IOException;
import java.util.List;

//import net.roboduino.agent.socket.UDPServer;
import net.roboduino.commons.VideoConstant;

//import org.apache.mina.core.buffer.IoBuffer;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.zxing.Android.PlanarYUVLuminanceSource;

public class VideoView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder holder;
	private Camera camera;
	private int width;
	private int height;

	VideoView(Context context) {
		this(context, (AttributeSet) null);
	}

	public VideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// this.setKeepScreenOn(true);// 保持屏幕常亮
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		camera = Camera.open();
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			camera.release();
			camera = null;
			
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		/* 构建Camera.Parameters对相机的参数进行设置 */
		Camera.Parameters parameters = camera.getParameters();
		// parameters.setPreviewSize(width, height);
		// parameters.setJpegQuality(100);// 图像质量
		/* 设置拍照的图片格�?*/
		parameters.setPictureFormat(PixelFormat.JPEG);
		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Size optimalSize = getOptimalPreviewSize(sizes, width, height);
		/* 设置Preview的尺�?*/
		this.width = optimalSize.width;
		this.height = optimalSize.height;
		// parameters.setPreviewFrameRate(3);// 每秒3�?
		// parameters.set("jpeg-quality", 85);// 照片质量
		parameters.setPreviewSize(this.width, this.height);
		// parameters.setPreviewSize(320, 240);
		// parameters.setPictureSize(320, 240);
		// parameters.setPreviewFrameRate(1);// //帧率30，即�?��钟播�?0帧，1/30秒播放一�?

		StreamIt streamIt = new StreamIt();
		camera.setPreviewCallback(streamIt);// Android的拍照视频预览时就可以截取视频数据�?每获得一帧就调用�?��接口函数�?
		/* 设置相机采用parameters */
		camera.setParameters(parameters);

		/* �?��预览 */
		camera.startPreview();
	}

	class StreamIt implements Camera.PreviewCallback {
		public byte[] yuv420sp = null;

		public void onPreviewFrame(byte[] data, Camera camera) {
//			long now = System.currentTimeMillis();
//			if (VideoConstant.TIME + VideoConstant.INTERVAL <= now) {
//				PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
//						data, width, height, 0, 0, VideoConstant.WIDTH,
//						VideoConstant.HEIGHT);
//				yuv420sp = data;
			//	 logger.info("viedeo datalength:{}", data.length);
				// IoBuffer buffer = IoBuffer.allocate(499584);
//				IoBuffer buffer = IoBuffer.allocate(VideoConstant.SIZE);
//				// IoBuffer buffer = IoBuffer.allocate(4);
//				buffer.setAutoExpand(true);
//				buffer.put(source.getMatrix());
//				buffer.flip();
//				// 做一个阻塞式队列发�?�?
////				UDPServer.broadcast(buffer);
//				VideoConstant.TIME = now;
//			}
		}
	}
}