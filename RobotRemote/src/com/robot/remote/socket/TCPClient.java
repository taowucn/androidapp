package com.robot.remote.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

//import net.roboduino.commons.BaseMsg;
//import net.roboduino.commons.CodecFactory;


//import org.apache.mina.core.future.ConnectFuture;
//import org.apache.mina.core.session.IoSession;
//import org.apache.mina.filter.codec.ProtocolCodecFilter;
//import org.apache.mina.transport.socket.nio.NioSocketConnector;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import android.util.Log;

public class TCPClient {
	final static String TAG = "TCPClient";
	private static final String SERVERIP = "192.168.1.2";
	private static final int SERVERPORT = 54321;
	
	private static Socket mSocket = null;
	private static BufferedReader mBufferedReader = null;
	private static PrintWriter mPrintWriter = null;
	
	public static void init() {
//		connector = new NioSocketConnector();
//		connector.getFilterChain().addLast("codec",
//				new ProtocolCodecFilter(new CodecFactory()));
//		connector.setHandler(new RobotCtrlHandler());
//		connector.getSessionConfig().setReuseAddress(true);
		try 
		{
			//连接服务器
			mSocket = new Socket(SERVERIP, SERVERPORT);	
			
			//取得输入、输出流
			mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			mPrintWriter=new PrintWriter(mSocket.getOutputStream(), true);
			Log.d(TAG, "init ok");
		}
		catch (Exception e) 
		{
			Log.e(TAG, "ERROR: " + e.toString());
		}
	}

	public static void sendMsg(String str) {
		if (mSocket != null && mSocket.isConnected()) {
			//mSocket.write(str);
			mPrintWriter.print(str);
	    	mPrintWriter.flush();
		}
	}

	public static void disconnect() {
		if (mSocket != null && mSocket.isConnected()) {
			try {
				mSocket.close();
			} catch (IOException e) {
				Log.d(TAG, "" + e.getMessage());
				e.printStackTrace();
			}
			Log.d(TAG, "session closed");
		}
	}

	public static String getIp() {
		return SERVERIP;
	}

	public static int getPort() {
		return SERVERPORT;
	}
	
	public static String getLocalIpAddress() {
		return mSocket.getLocalAddress().toString();
	}
}
