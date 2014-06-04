package com.robot.remote.socket;

import android.util.Log;
import net.roboduino.commons.BaseMsg;

/**消息分发�*/
public class MsgDelegate {
	final static String TAG = "MsgDelegate";
	
	public static void dispatch(BaseMsg msg){
		Log.d(TAG, "msg:" + msg.getContent());
	}
}
