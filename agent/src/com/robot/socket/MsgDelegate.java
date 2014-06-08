package com.robot.socket;

import android.util.Log;
import net.roboduino.commons.BaseMsg;
import net.roboduino.commons.ProtocolConstant;

/** 消息分发�?*/
public class MsgDelegate {
	static final String TAG="MsgDelegate";
	
	public static void dispatch(BaseMsg msg) {
//		String retMsg = CommandUtil.sendDataToArduino(
//				ApplicationContext.getInstance(),
//				BlueToothConstant.DEVICE_ADDRESS, msg.getCmdType(),
//				msg.getContent());
		Log.d("MsgDelegate", "MSG:" + msg.getContent());
		switch (msg.getCmdType()) {
		case ProtocolConstant.MSG_CMD_MOTOR_SPEED: {
			Log.d(TAG, "MSG_CMD_MOTOR_SPEED a ");
			break;
		}
		case ProtocolConstant.MSG_CMD_MOTOR_POWER: {
			Log.d(TAG, "MSG_CMD_MOTOR_POWER b");
			break;
		}
		case ProtocolConstant.MSG_CMD_SERVO: {
			Log.d(TAG, "MSG_CMD_SERVO c");
			break;
		}
		case ProtocolConstant.MSG_CMD_BUMPER: {
			Log.d(TAG, "MSG_CMD_BUMPER d");
			break;
		}
		case ProtocolConstant.MSG_CMD_IR: {
			Log.d(TAG, "MSG_CMD_IR e");
			break;
		}
		case ProtocolConstant.MSG_CMD_UR: {
			break;
		}
		}
	}
}
