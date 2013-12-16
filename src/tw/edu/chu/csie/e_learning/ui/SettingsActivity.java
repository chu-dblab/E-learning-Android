package tw.edu.chu.csie.e_learning.ui;

import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.R.layout;
import tw.edu.chu.csie.e_learning.R.menu;
import tw.edu.chu.csie.e_learning.config.Config;
import tw.edu.chu.csie.e_learning.util.AccountUtils;
import tw.edu.chu.csie.e_learning.util.SettingUtils;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.test.PerformanceTestCase;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import tw.edu.chu.csie.e_learning.ui.dialog.LoginDialogBuilder;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceClickListener, OnPreferenceChangeListener {

	SettingUtils settingUtils;
	
	CheckBoxPreference student_modeView, learn_unfinish_backView, learn_exitView;
	EditTextPreference remote_urlView;
	ListPreference learn_modeView, sync_learn_frequencyView;
	Preference reset_all_settingsView, exitView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		settingUtils = new SettingUtils(getBaseContext());
		
		// 設定ActionBar
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// 顯示設定選項
		addPreferencesFromResource(R.xml.pref_settings);
		
		// 界面物件對應
		student_modeView = (CheckBoxPreference)findPreference("student_mode");
		student_modeView.setOnPreferenceChangeListener(this);
		student_modeView.setOnPreferenceClickListener(this);
		remote_urlView = (EditTextPreference)findPreference("remote_url");
		learn_modeView = (ListPreference)findPreference("learn_mode");
		learn_unfinish_backView = (CheckBoxPreference)findPreference("learn_unfinish_back");
		learn_exitView = (CheckBoxPreference)findPreference("learn_exit");
		sync_learn_frequencyView = (ListPreference)findPreference("sync_learn_frequency");
		reset_all_settingsView = (Preference)findPreference("reset_all_settings");
		reset_all_settingsView.setOnPreferenceClickListener(this);
		exitView = (Preference)findPreference("exit");
		exitView.setOnPreferenceClickListener(this);
		
		// 將描述部份顯示為設定值
		bindPreferenceSummaryToValue(findPreference("remote_url"));
		bindPreferenceSummaryToValue(findPreference("learn_mode"));
		
		// 顯示設定值
		updateStudentModeUI(settingUtils.isStudentMode());
		
		// 顯示學習模式設定值
		// TODO 防呆: 無此選項會例外
		learn_modeView.setSummary(
				(String)learn_modeView.getEntries()[
					learn_modeView.findIndexOfValue(settingUtils.getSharedPreferences().getString("learn_mode", Config.LEARN_MODE))]
				);
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
				if(preference.getKey().equals("remote_url")) {
					if(stringValue.equals("")) {
						preference.setSummary(Config.REMOTE_BASE_URL);
					}else {
						preference.setSummary(stringValue);
					}
				}else {
					preference.setSummary(stringValue);
				}
			}
			return true;
		}
	};
	
	// 動作======================================================================================
	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey(); 
		if(key.equals("student_mode")) {
			// 顯示對話框
			final LoginDialogBuilder dialogBuilder = new LoginDialogBuilder(SettingsActivity.this);
			dialogBuilder.setPositiveButton(android.R.string.ok,	new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 取得界面上的資料
					EditText uidView = (EditText)dialogBuilder.view.findViewById(R.id.dialog_login_uid);
					
					// TODO DEBUG 顯示使用者輸入的內容
					Toast.makeText(getBaseContext(), uidView.getText(), Toast.LENGTH_SHORT).show();
					
					// TODO 判斷是否為管理者
					
					// 確認為管理者-修改設定值
					//SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					//updateStudentModeUI( !pref.getBoolean("student_mode", Config.STUDENT_MODE) );
					updateStudentModeUI( !settingUtils.isStudentMode() );
				}
			});
			
			
			// 顯示出管理者登入Dialog
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();
		} else if(key.equals("exit")){
			// 關閉程式
			// TODO 修正會回到進入點的問題
			android.os.Process.killProcess(android.os.Process.myPid());
		}else if(key.equals("reset_all_settings")) {
			settingUtils.reset();
			
			// TODO 顯示清除完畢提示
			//Toast.makeText(getBaseContext(), "url: "+test1, Toast.LENGTH_SHORT).show();
			
			// 重新啟動設定頁面
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
		
		return false;
	}
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey(); 
		if(key.equals("student_mode")){
			// 不要設定，改由onPreferenceClick處理
			return false;
		}
		
		if(key.equals("remote_url")) {
			if(newValue.equals("")) {
				newValue = null;
			}
		}
		
		return true;
	}
	
	private void updateStudentModeUI(boolean tf) {
		//
		if(!settingUtils.getSharedPreferences().contains("learn_unfinish_back")) {
			learn_unfinish_backView.setChecked(Config.LEARNING_BACK_ENABLE);
			SharedPreferences.Editor prefEdit = settingUtils.getSharedPreferences().edit(); 
			
			prefEdit.remove("learn_unfinish_back");
			prefEdit.commit();
		}
		
		
		// 選項
		if( tf ) {
			student_modeView.setChecked(true);
			learn_unfinish_backView.setEnabled(false);
			learn_exitView.setEnabled(false);
			reset_all_settingsView.setEnabled(false);
		}else {
			student_modeView.setChecked(false);
			learn_unfinish_backView.setEnabled(true);
			learn_exitView.setEnabled(true);
			reset_all_settingsView.setEnabled(true);
		}

		// 控制是否允許離開程式
		if(!tf || PreferenceManager.getDefaultSharedPreferences(
				learn_exitView.getContext()).getBoolean(learn_exitView.getKey(),
				false)) {
			exitView.setEnabled(true);
		} else {
			exitView.setEnabled(false);
		}
		
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
