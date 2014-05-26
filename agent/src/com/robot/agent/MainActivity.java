package com.robot.agent;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.TabActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.os.Build;

public class MainActivity extends TabActivity {
	static final String TAG = "RobotAgent";

	private TextView display;
	private String deviceAddress;
	private LinearLayout layout;
	private ScrollView scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.activity_main);

		display = (TextView) this.findViewById(R.id.display_windows);
		layout = (LinearLayout) findViewById(R.id.tab1_layout);
		scrollView = (ScrollView) findViewById(R.id.tab1);

		buildTabView();


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

	/** change tab */
	private OnTabChangeListener tabChangeListener = new OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			Log.d(TAG, "" + tabId);
		}
	};
	
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

}
