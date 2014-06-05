package com.robot.remote;

import net.roboduino.commons.BaseMsg;
import net.roboduino.commons.ProtocolConstant;

import com.robot.remote.socket.TCPClient;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.os.Build;

public class RemoteActivity extends ActionBarActivity {
	
	final static String TAG = "RobotRemote";
	private TextView display;
	private String deviceAddress;
	private LinearLayout layout;
	private ScrollView scrollView;
	private Button buttonScan;
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote);

//		serialBegin(115200);
//		onResumeProcess();
		display = (TextView) this.findViewById(R.id.display_windows);
		layout = (LinearLayout) findViewById(R.id.tab1_layout);
		scrollView = (ScrollView) findViewById(R.id.tab1);

        buttonScan = (Button) findViewById(R.id.buttonScan);					//initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 ClientStart clientStart =  new ClientStart();
				 clientStart.start();
			}
		});

        buildTabView();
        Log.d(TAG, "buildTabView Done");
        buildDrive();
        
//        
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}
	
	class ClientStart extends Thread{
		@Override
		public void run() {
	        TCPClient.init();
	        Log.d(TAG, "TCPClient init Down");
		}
	}
	
	/** create tab list view */
	private void buildTabView() {
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); //this.getTabHost();
		tabHost.setup();
		//TabWidget tabWidget = tabHost.getTabWidget();

		tabHost.addTab(tabHost
				.newTabSpec("tag_text")
				.setIndicator("View")
				.setContent(R.id.tab1));
		tabHost.addTab(tabHost
				.newTabSpec("tag_camera")
				.setIndicator("Camera")
				.setContent(R.id.tab2));
		tabHost.addTab(tabHost
				.newTabSpec("tag_console")
				.setIndicator("Other")
				.setContent(R.id.tab3));
		tabHost.setCurrentTab(0);
		tabHost.setOnTabChangedListener(tabChangeListener);
	}
	
	/** change tab */
	private OnTabChangeListener tabChangeListener = new OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			Log.d(TAG, "" + tabId);
		}
	};

	private void buildDrive() {
//		ImageButton upBtn = (ImageButton) this.findViewById(R.id.up);
//		// upBtn.setOnClickListener(driveClickListener);
//		// upBtn.setLongClickable(longClickable);
//		 upBtn.setOnTouchListener(onTouchListener);
//		//upBtn.setOnKeyListener(driveKeyListener);
//		ImageButton downBtn = (ImageButton) this.findViewById(R.id.down);
//		// downBtn.setOnClickListener(driveClickListener);
//		// downBtn.setOnKeyListener(driveKeyListener);
//		downBtn.setOnTouchListener(onTouchListener);
//		ImageButton leftBtn = (ImageButton) this.findViewById(R.id.left);
//		// leftBtn.setOnClickListener(driveClickListener);
//		// leftBtn.setOnKeyListener(driveKeyListener);
//		leftBtn.setOnTouchListener(onTouchListener);
//		leftBtn.setOnClickListener(driveClickListener);
//		ImageButton rightBtn = (ImageButton) this.findViewById(R.id.right);
//		// rightBtn.setOnClickListener(driveClickListener);
//		// rightBtn.setOnKeyListener(driveKeyListener);
//		rightBtn.setOnTouchListener(onTouchListener);
//		rightBtn.setOnClickListener(driveClickListener);
//		
		ImageButton stopBtn = (ImageButton) this.findViewById(R.id.stop);
		stopBtn.setOnClickListener(driveClickListener);
	}

	private OnKeyListener driveKeyListener = new OnKeyListener() {
		public boolean onKey(View view, int keyCode, KeyEvent event) {
			switch (event.getAction()) {
			
			case KeyEvent.ACTION_DOWN: {
				//Log.d(TAG, "key.id=" + keyCode);
				switch (keyCode) {
				case R.id.left:
				case R.id.right: {
					//robot.stop();
				}
					break;
				}
			}
				break;
			case KeyEvent.ACTION_UP: {
				switch (keyCode) {
				case R.id.up:
				case R.id.down:
				case R.id.left:
				case R.id.right: {
					Log.d(TAG,"===========OnKeyListener = stop ");
//					serialSend("e");
					//robot.stop();
				}
					break;
				}
				
			}
				break;
			}
			return false;
		}
	};

	private OnClickListener driveClickListener = new OnClickListener() {
		public void onClick(View view) {
			//Log.d(TAG, "view.id=" + view.getId());
			switch (view.getId()) {
			case R.id.left:
			case R.id.right:
			case R.id.stop:{
				Log.d(TAG,"===========OnClickListener = stop ");
				TCPClient.sendMsg("hi, tao");
//				serialSend("e");
				//robot.stop();
			}
				break;
			}
			display.append("Stop\n");
			handler.post(scrollToBottom);
		}
	};
	
	private OnTouchListener onTouchListener = new OnTouchListener() {
		public boolean onTouch(View view, MotionEvent event) {
			String msg = "";
			//Log.d(TAG, "event.action=" + view.getId());
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE: {
				Log.d(TAG,"===========OnTouchListener:ACTION_MOVE = ? ");
				dispatchMsg(view.getId());
				break;
			}
			case MotionEvent.ACTION_UP: {
				Log.d(TAG,"===========OnTouchListener:ACTION_CANCEL = stop");
				msg = "Stop";
//				serialSend("e");
				break;
			}
			default:
				break;
			}
			display.append("" + msg + "\n");
			handler.post(scrollToBottom);
			return false;
		}
	};

	private void dispatchMsg(int id) {
		String msg = "";
		switch (id) {

		case R.id.up: {
			Log.d(TAG,"===========ACTION_MOVE = a");
			msg = "Foward";
//			serialSend("a");
			//robot.changeSpeed((byte) 0x01, (byte) 0x01);
			// msg = CommandUtil.driveMotorS(this, (byte) 0xff, (byte) 0xff);
			break;
		}
		case R.id.down: {
			Log.d(TAG,"===========ACTION_MOVE = b");
			msg = "Back";
//			serialSend("b");
			//robot.changeSpeed((byte) (-0x01), (byte) (-0x01));
			// msg = CommandUtil.driveMotorS(this, (byte) 0x00, (byte) 0x00);
			break;
		}
		case R.id.left: {
			Log.d(TAG,"===========ACTION_MOVE = d");
			msg = "Left";
//			serialSend("d");
			//robot.stop();
			//robot.changeSpeed((byte) (-0x01), (byte) (0x01));
			// msg = CommandUtil.driveMotorS(this, (byte) 0x00, (byte) 0xff);
			break;
		}
		case R.id.right: {
			Log.d(TAG,"===========ACTION_MOVE = c");
			msg = "Right";
//			serialSend("c");
			//robot.stop();
			//robot.changeSpeed((byte) (0x01), (byte) (-0x01));
			// msg = CommandUtil.driveMotorS(this, (byte) 0xff, (byte) 0x00);
			break;
		}
		case R.id.stop: {
			Log.d(TAG,"===========ACTION_MOVE = e");
			msg = "Stop";
//			serialSend("e");
			//robot.stop();
			// msg = CommandUtil.driveMotorS(this, (byte) 0x80, (byte) 0x80);
			break;
		}
		default:
			break;

		}
//		msg = "left:" + robot.getLeftSpeed() + " Right:"
//				+ robot.getRightSpeed();
		display.append("" + msg + "\n");
		handler.post(scrollToBottom);
	}
	
	private Thread scrollToBottom = new Thread() {
		@Override
		public void run() {
//			Log.d(TAG, "Y=" + scrollView.getScrollY());
			int off = layout.getMeasuredHeight() - scrollView.getHeight();
			if (off > 0) {
				scrollView.scrollTo(0, off);
			}
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.remote, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, PreferencesActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_remote,
					container, false);
			return rootView;
		}
	}

	@Override
    protected void onDestroy() {
        super.onDestroy();
        TCPClient.disconnect();
        Log.d(TAG, "TCPClient disconnect OK");
    }

}
