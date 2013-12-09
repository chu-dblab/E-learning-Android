package tw.edu.chu.csie.e_learning.ui;

import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.R.layout;
import tw.edu.chu.csie.e_learning.R.menu;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.app.Activity;
import android.test.PerformanceTestCase;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceClickListener {

	CheckBoxPreference student_modeView, learn_unfinish_backView, learn_exitView;
	EditTextPreference remote_urlView;
	ListPreference learn_modeView, sync_learn_frequencyView;
	Preference reset_all_settingsView, exitView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 設定ActionBar
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// 顯示設定選項
		addPreferencesFromResource(R.xml.pref_settings);
		
		// 界面物件對應
		student_modeView = (CheckBoxPreference)findPreference("student_mode");
		remote_urlView = (EditTextPreference)findPreference("remote_url");
		learn_modeView = (ListPreference)findPreference("learn_mode");
		learn_unfinish_backView = (CheckBoxPreference)findPreference("learn_unfinish_back");
		learn_exitView = (CheckBoxPreference)findPreference("learn_exit");
		sync_learn_frequencyView = (ListPreference)findPreference("sync_learn_frequency");
		reset_all_settingsView = (Preference)findPreference("reset_all_settings");
		exitView = (Preference)findPreference("exit");
		exitView.setOnPreferenceClickListener(this);
		
		// 將描述部份顯示為設定值
		bindPreferenceSummaryToValue(findPreference("remote_url"));
		bindPreferenceSummaryToValue(findPreference("learn_mode"));
		
	}
	

	// SummaryToValue=================================================================
	
	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference
				.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}
	
	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);

			}else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};
	
	// 動作======================================================================================
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if(preference.getKey().equals("exit")){
			// 關閉程式
			// TODO 修正會回到進入點的問題
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return false;
	}
	
	// 選單======================================================================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preference, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


}
