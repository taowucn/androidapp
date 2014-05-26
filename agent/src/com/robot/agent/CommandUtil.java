package com.robot.agent;

import net.roboduino.commons.BaseMsg;
import net.roboduino.commons.ProtocolConstant;

import org.apache.mina.core.buffer.IoBuffer;

import android.content.Context;
import at.abraxas.amarino.Amarino;

public class CommandUtil {

	public static String driveMotorP(Context context, String address, byte m1p,
			byte m2p) {
		return sendDataToArduino(context, address,
				ProtocolConstant.MSG_CMD_MOTOR_POWER, new byte[] { m1p, m2p });
	}

	public static String driveMotorS(Context context, String address, byte m1s,
			byte m2s) {
		return sendDataToArduino(context, address,
				ProtocolConstant.MSG_CMD_MOTOR_SPEED, new byte[] { m1s, m2s });
	}

	public static String sendDataToArduino(Context context, String address,
			byte cmdType, byte[] content) {
		BaseMsg msg = new BaseMsg(cmdType, content);
		int length=ProtocolConstant.MSG_LENGTH_INI+ content.length;
		IoBuffer buffer = IoBuffer.allocate(length);

		msg.serialize(buffer);

		buffer.flip();
		byte[] bytes = new byte[length];
		buffer.get(bytes);
		Amarino.sendDataToArduino(context, address, (char) cmdType,
				bytes);
		return msg.toString();
	}
}
