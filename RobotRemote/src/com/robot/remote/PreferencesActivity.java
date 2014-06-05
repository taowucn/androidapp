package com.robot.remote;

import com.robot.remote.socket.TCPClient;
//import com.robot.remote.socket.UDPClient;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

public class PreferencesActivity extends PreferenceActivity {
//	private static final Logger logger = LoggerFactory
//			.getLogger(PreferencesActivity.class);
	final static String TAG = "PreferencesActivity";
	private CheckBoxPreference tcpSwitchPreference;
//	private CheckBoxPreference udpSwitchPreference;
	private EditTextPreference addressPreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// �?��的�?将会自动保存到SharePreferences
		this.addPreferencesFromResource(R.xml.preferences);
		// this.getPreferenceManager().getSharedPreferences()
		// .registerOnSharedPreferenceChangeListener(this);
		// TCP连接控制
		tcpSwitchPreference = (CheckBoxPreference) this
				.findPreference("tcp_connect");
		tcpSwitchPreference.setOnPreferenceChangeListener(switchListener);
		// UDP连接控制
//		udpSwitchPreference = (CheckBoxPreference) this
//				.findPreference("udp_connect");
//		udpSwitchPreference.setOnPreferenceChangeListener(switchListener);
		// 地址
		addressPreference = (EditTextPreference) this
				.findPreference("connect_address");
		addressPreference.setOnPreferenceChangeListener(addressListener);

	}

	/** TCP/UDP�?��监听�?*/
	private OnPreferenceChangeListener switchListener = new OnPreferenceChangeListener() {

		public boolean onPreferenceChange(Preference preference, Object obj) {
			if (StringUtils.equals(preference.getKey(), "tcp_connect")) {
				if ((Boolean) obj) {
					preference.setSummary(R.string.connected);
					TCPClient.init();
					Log.d(TAG, "TCPClient init ok");
					// 处理tcp连接
				} else {
					// 处理tcp断开
					TCPClient.disconnect();
					Log.d(TAG, "TCPClient disconnect ok");
					preference.setSummary(R.string.disconnected);
				}
			} 

			return true;
		}

	};
	
	/** IP地址监听�?*/
	private OnPreferenceChangeListener addressListener = new OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object obj) {
			String address = (String) obj;
			addressPreference.setSummary(address);
			return true;
		}
	};

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		String key = preference.getKey();
		Log.d(TAG, "key=" + key);
		if (StringUtils.equals(key, "wifi_setting")) {
			if (tcpSwitchPreference.isChecked()) {
				tcpSwitchPreference.setSummary(R.string.connected);
			} else {
				tcpSwitchPreference.setSummary(R.string.disconnected);
			}
//			if (udpSwitchPreference.isChecked()) {
//				udpSwitchPreference.setSummary(R.string.connected);
//			} else {
//				udpSwitchPreference.setSummary(R.string.disconnected);
//			}
			addressPreference.setSummary(addressPreference.getText());
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	protected void onDestroy() {
		// getPreferenceManager().getSharedPreferences()
		// .unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();

	}
}