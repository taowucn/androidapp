package com.robot.agent;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.TabActivity;
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
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.os.Build;

public class MainActivity extends BlunoLibrary {
	static final String TAG = "RobotAgent";

	private TextView display;
	private String deviceAddress;
	private LinearLayout layout;
	private ScrollView scrollView;
	private Robot robot;
	private Button buttonScan;
	
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		setContentView(R.layout.activity_main);
		onCreateProcess();
		onResumeProcess();
		serialBegin(115200);
		
		display = (TextView) this.findViewById(R.id.display_windows);
		layout = (LinearLayout) findViewById(R.id.tab1_layout);
		scrollView = (ScrollView) findViewById(R.id.tab1);

        buttonScan = (Button) findViewById(R.id.buttonScan);					//initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
			}
		});
               
		buildTabView();
		
		//buildDrive();
		//robot = new Robot();
		
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}

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
	
	private void buildDrive() {
		ImageButton upBtn = (ImageButton) this.findViewById(R.id.up);
		upBtn.setOnTouchListener(onTouchListener);
		ImageButton downBtn = (ImageButton) this.findViewById(R.id.down);
		downBtn.setOnTouchListener(onTouchListener);
		ImageButton leftBtn = (ImageButton) this.findViewById(R.id.left);
		leftBtn.setOnTouchListener(onTouchListener);
		leftBtn.setOnClickListener(driveClickListener);
		ImageButton rightBtn = (ImageButton) this.findViewById(R.id.right);
		rightBtn.setOnTouchListener(onTouchListener);
		rightBtn.setOnClickListener(driveClickListener);
		ImageButton stopBtn = (ImageButton) this.findViewById(R.id.stop);
		stopBtn.setOnClickListener(driveClickListener);
	}

	/** change tab */
	private OnTabChangeListener tabChangeListener = new OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			Log.d(TAG, "" + tabId);
		}
	};

	private OnKeyListener driveKeyListener = new OnKeyListener() {
		public boolean onKey(View view, int keyCode, KeyEvent event) {
			switch (event.getAction()) {
			
			case KeyEvent.ACTION_DOWN: {
				Log.d(TAG, "key.id=" + keyCode);
				switch (keyCode) {
				case R.id.left:
				case R.id.right: {
					robot.stop();
				}
					break;
				}
				//display.append("down");
			}
				break;
			case KeyEvent.ACTION_UP: {
				switch (keyCode) {
				case R.id.up:
				case R.id.down:
					// robot.setSpeed((byte) 0x10, (byte) 0x10);
					break;
				case R.id.left:
				case R.id.right: {
					robot.stop();
				}
					break;
				}
				//display.append("up");
			}
				break;
			}
			return false;
		}
	};

	private OnClickListener driveClickListener = new OnClickListener() {
		public void onClick(View view) {
			Log.d(TAG, "view.id=" + view.getId());
			switch (view.getId()) {
			case R.id.left:
			case R.id.right:
			case R.id.stop:{
				robot.stop();
			}
				break;
			}
			display.append("stop\n");
			handler.post(scrollToBottom);
		}
	};
	private OnTouchListener onTouchListener = new OnTouchListener() {

		public boolean onTouch(View view, MotionEvent event) {
			Log.d(TAG, "event.action=" + view.getId());
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE: {
				dispatchMsg(view.getId());
				break;
			}
			default:
				break;
			}
			return false;
		}
	};

	private void dispatchMsg(int id) {
		String msg = "no msg";
		switch (id) {

		case R.id.up: {
			robot.changeSpeed((byte) 0x01, (byte) 0x01);
			// msg = CommandUtil.driveMotorS(this, (byte) 0xff, (byte) 0xff);
			break;
		}
		case R.id.down: {
			robot.changeSpeed((byte) (-0x01), (byte) (-0x01));
			// msg = CommandUtil.driveMotorS(this, (byte) 0x00, (byte) 0x00);
			break;
		}
		case R.id.left: {
			//robot.stop();
			robot.changeSpeed((byte) (-0x01), (byte) (0x01));
			// msg = CommandUtil.driveMotorS(this, (byte) 0x00, (byte) 0xff);
			break;
		}
		case R.id.right: {
			//robot.stop();
			robot.changeSpeed((byte) (0x01), (byte) (-0x01));
			// msg = CommandUtil.driveMotorS(this, (byte) 0xff, (byte) 0x00);
			break;
		}
		case R.id.stop: {
			robot.stop();
			// msg = CommandUtil.driveMotorS(this, (byte) 0x80, (byte) 0x80);
			break;
		}
		default:
			break;

		}
		msg = "left:" + robot.getLeftSpeed() + " Right:"
				+ robot.getRightSpeed();
		display.append("Robot Speed:" + msg + "\n");
		handler.post(scrollToBottom);
	}


	private Thread scrollToBottom = new Thread() {
		@Override
		public void run() {
			Log.d(TAG, "Y=" + scrollView.getScrollY());
			int off = layout.getMeasuredHeight() - scrollView.getHeight();
			if (off > 0) {
				scrollView.scrollTo(0, off);
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		Log.d(TAG, "onCreateOptionsMenu");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Log.d(TAG, "onOptionsItemSelected");
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
			Log.d(TAG, "onCreateView");
			
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onConectionStateChange(
			connectionStateEnum theConnectionState) {
		// TODO Auto-generated method stub
		switch (theConnectionState) {											//Four connection state
		case isConnected:
			buttonScan.setText("Connected");
			break;
		case isConnecting:
			buttonScan.setText("Connecting");
			break;
		case isToScan:
			buttonScan.setText("Scan");
			break;
		case isScanning:
			buttonScan.setText("Scanning");
			break;
		case isDisconnecting:
			buttonScan.setText("isDisconnecting");
			break;
		default:
			break;
		}
	}

	@Override
	public void onSerialReceived(String theString) {
		// TODO Auto-generated method stub
		Log.d(TAG, theString);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}
    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();														//onPause Process by BlunoLibrary
    }
    
	protected void onStop() {
		super.onStop();
		onStopProcess();														//onStop Process by BlunoLibrary
	}
    
	@Override
    protected void onDestroy() {
        super.onDestroy();	
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }
}
