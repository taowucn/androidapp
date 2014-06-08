package com.robot.socket;

import android.util.Log;
import net.roboduino.commons.BaseMsg;
import net.roboduino.commons.ProtocolConstant;

/** 消息分发�?*/
public class MsgDelegate {

	public static void dispatch(BaseMsg msg) {
//		String retMsg = CommandUtil.sendDataToArduino(
//				ApplicationContext.getInstance(),
//				BlueToothConstant.DEVICE_ADDRESS, msg.getCmdType(),
//				msg.getContent());
		Log.d("MsgDelegate", "MSG:" + msg.getContent());
		switch (msg.getCmdType()) {
		case ProtocolConstant.MSG_CMD_MOTOR_SPEED: {
			break;
		}
		case ProtocolConstant.MSG_CMD_MOTOR_POWER: {
			break;
		}
		case ProtocolConstant.MSG_CMD_SERVO: {
			break;
		}
		case ProtocolConstant.MSG_CMD_BUMPER: {
			break;
		}
		case ProtocolConstant.MSG_CMD_IR: {
			break;
		}
		case ProtocolConstant.MSG_CMD_UR: {
			break;
		}
		}
	}
}
