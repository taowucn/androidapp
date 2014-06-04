package com.robot.socket;

import android.util.Log;

import com.robot.agent.ApplicationContext;
import com.robot.agent.BlueToothConstant;
import com.robot.agent.CommandUtil;

import net.roboduino.commons.BaseMsg;
import net.roboduino.commons.ProtocolConstant;

public class MsgDelegate {
	final static String TAG = "MsgDelegate";
	public static void dispatch(BaseMsg msg) {
		
		String retMsg = CommandUtil.sendDataToArduino(
				ApplicationContext.getInstance(),
				BlueToothConstant.DEVICE_ADDRESS, msg.getCmdType(),
				msg.getContent());
		Log.d(TAG, "msg:" + msg.getContent());
		
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
