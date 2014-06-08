package com.robot.socket;

import com.robot.agent.ApplicationContext;
import com.robot.agent.MainActivity;

import android.content.Intent;
import android.util.Log;
import net.roboduino.commons.BaseMsg;
import net.roboduino.commons.ProtocolConstant;

/** 消息分发�?*/
public class MsgDelegate {
	static final String TAG="MsgDelegate";
	static final String DIR_ACTION = "com.robot.agent.action.DIR_ACTION";

	public static void dispatch(BaseMsg msg) {
//		String retMsg = CommandUtil.sendDataToArduino(
//				ApplicationContext.getInstance(),
//				BlueToothConstant.DEVICE_ADDRESS, msg.getCmdType(),
//				msg.getContent());
		Log.d("MsgDelegate", "MSG:" + msg.getContent());
		
		Intent intent = new Intent();
		intent.setAction(DIR_ACTION);
		switch (msg.getCmdType()) {
		case ProtocolConstant.MSG_CMD_MOTOR_SPEED: {
			Log.d(TAG, "MSG_CMD_MOTOR_SPEED a ");
			intent.putExtra("msg", "a");
			break;
		}
		case ProtocolConstant.MSG_CMD_MOTOR_POWER: {
			Log.d(TAG, "MSG_CMD_MOTOR_POWER b");
			intent.putExtra("msg", "b");
			
			break;
		}
		case ProtocolConstant.MSG_CMD_SERVO: {
			Log.d(TAG, "MSG_CMD_SERVO c");
			intent.putExtra("msg", "c");
			
			break;
		}
		case ProtocolConstant.MSG_CMD_BUMPER: {
			Log.d(TAG, "MSG_CMD_BUMPER d");
			intent.putExtra("msg", "d");
			
			break;
		}
		case ProtocolConstant.MSG_CMD_IR: {
			Log.d(TAG, "MSG_CMD_IR e");
			intent.putExtra("msg", "e");
		
			break;
		}
		case ProtocolConstant.MSG_CMD_UR: {
			break;
		}
		}
		ApplicationContext.getInstance().sendBroadcast(intent);
	}
}
