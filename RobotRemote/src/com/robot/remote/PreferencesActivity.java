package com.robot.remote;

import com.robot.remote.socket.TCPClient;
import com.robot.remote.socket.UDPClient;

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
	private static final Logger logger = LoggerFactory
			.getLogger(PreferencesActivity.class);
	static final String TAG = "PreferencesActivity";
	private CheckBoxPreference tcpSwitchPreference;
	private CheckBoxPreference udpSwitchPreference;
	private EditTextPreference addressPreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ���ĵ�ֵ�����Զ����浽SharePreferences
		this.addPreferencesFromResource(R.xml.preferences);
		// this.getPreferenceManager().getSharedPreferences()
		// .registerOnSharedPreferenceChangeListener(this);
		// TCP���ӿ���
		tcpSwitchPreference = (CheckBoxPreference) this
				.findPreference("tcp_connect");
		tcpSwitchPreference.setOnPreferenceChangeListener(switchListener);
		// UDP���ӿ���
		udpSwitchPreference = (CheckBoxPreference) this
				.findPreference("udp_connect");
		udpSwitchPreference.setOnPreferenceChangeListener(switchListener);
		// ��ַ
		addressPreference = (EditTextPreference) this
				.findPreference("connect_address");
		addressPreference.setOnPreferenceChangeListener(addressListener);
	}

	public class SocketClt implements Runnable{
		@Override
		public void run() {
			TCPClient.init();
			try {
				TCPClient.connect(addressPreference.getText(), 9600);
			} catch (InterruptedException e) {
				Log.d(TAG, "e:" + e.getMessage());
				e.printStackTrace();
			}
		}
	};
	/** TCP/UDP���ؼ����� */
	private OnPreferenceChangeListener switchListener = new OnPreferenceChangeListener() {
		public boolean onPreferenceChange(Preference preference, Object obj) {
			if (StringUtils.equals(preference.getKey(), "tcp_connect")) {
				if ((Boolean) obj) {
					preference.setSummary(R.string.connected);
//					TCPClient.init();
//					try {
//						TCPClient.connect(addressPreference.getText(), 9600);
//					} catch (InterruptedException e) {
//						logger.error(e.getMessage(), e);
//					}
					SocketClt clt = new SocketClt();
					Thread socketclt = new Thread(clt);
					socketclt.start();
					// ����tcp����
				} else {
					// ����tcp�Ͽ�
					TCPClient.disconnect();
					preference.setSummary(R.string.disconnected);
				}
			} else if (StringUtils.equals(preference.getKey(), "udp_connect")) {
				if ((Boolean) obj) {
					preference.setSummary(R.string.connected);
					UDPClient.init();
					try {
						UDPClient.connect(addressPreference.getText(), 9800);
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					}
					// ����udp����
				} else {
					// ����udp�Ͽ�
					UDPClient.disconnect();
					preference.setSummary(R.string.disconnected);
				}
			}

			return true;
		}

	};
	/** IP��ַ������ */
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
		logger.info("key={}", key);
		if (StringUtils.equals(key, "wifi_setting")) {
			if (tcpSwitchPreference.isChecked()) {
				tcpSwitchPreference.setSummary(R.string.connected);
			} else {
				tcpSwitchPreference.setSummary(R.string.disconnected);
			}
			if (udpSwitchPreference.isChecked()) {
				udpSwitchPreference.setSummary(R.string.connected);
			} else {
				udpSwitchPreference.setSummary(R.string.disconnected);
			}
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