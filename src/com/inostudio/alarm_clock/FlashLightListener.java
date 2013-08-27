package com.inostudio.alarm_clock;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.View;
import android.view.View.OnClickListener;

public class FlashLightListener implements OnClickListener{
	private static boolean isLightOn = false;
    private static Camera camera;
    private Context ctx;
	
	FlashLightListener(Context ctx){
		isLightOn = false;
		this.ctx = ctx;
	}
	
	@Override
	public void onClick(View v) {
		Intent wakeLock = new Intent(ctx, WakeLockService.class);
		if (isLightOn) {
            if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                    isLightOn = false;
                    ctx.stopService(wakeLock);
            }
		 } else {
            camera = Camera.open();
            if(camera == null) {
                  
            } else {
                    Parameters param = camera.getParameters();
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    try {
                            camera.setParameters(param);
                            camera.startPreview();
                            isLightOn = true;
                            ctx.startService(wakeLock);
                    } catch (Exception e) {

                    }
            }
		 }
	}

}
