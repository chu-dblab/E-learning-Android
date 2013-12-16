package tw.edu.chu.csie.e_learning.util;

import tw.edu.chu.csie.e_learning.config.Config;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingUtils {
	
	public static final int NON_LINE_LEARN = 101;
	public static final int HERF_LINE_LEARN = 102;
	public static final int LINE_LEARN = 103;
	
	
	private Context context;
	private SharedPreferences pref;
	
	public SettingUtils(Context context) {
		this.context = context;
		this.pref = PreferenceManager.getDefaultSharedPreferences(this.context);
	}
	
	public SharedPreferences getSharedPreferences() {
		return this.pref;
	}
	
	public void reset() {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		prefEdit.clear();
		prefEdit.commit();
	}
	
	// ============================================================================
	
	public String getRemoteURL() {
		return pref.getString("remote_url", Config.REMOTE_BASE_URL);
	}
	
	public void setRemoteURL(String value) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		if(value.isEmpty() || value.equals("")) {
			prefEdit.remove("remote_url");
			prefEdit.commit();
		} else {
			prefEdit.putString("remote_url", value);
			prefEdit.commit();
		}
	}
	
	public String getRemoteMaterialURL() {
		return pref.getString("remote_material_url", Config.REMOTE_TEXTBOOK_URL);
	}
	public void setRemoteMaterialURL(String value) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		if(value.isEmpty() || value.equals("")) {
			prefEdit.remove("remote_material_url");
			prefEdit.commit();
		} else {
			prefEdit.putString("remote_material_url", value);
			prefEdit.commit();
		}
	}
	
	// ----------------------------------------------------------------------------
	
	public boolean isStudentMode() {
		return this.pref.getBoolean("student_mode", Config.STUDENT_MODE);
	}
	
	public void setStudentMode(boolean value) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		prefEdit.putBoolean("student_mode", value);
		prefEdit.commit();
	}
	
	public int getLearnMode() {
		String key = pref.getString("learn_mode", Config.LEARN_MODE);
		
		if(key.equals("non-line-learn")) {
			return NON_LINE_LEARN;
		} else if(key.equals("harf-line-learn")) {
			return HERF_LINE_LEARN;
		} else if(key.equals("line-learn")) {
			return LINE_LEARN;
		} else {
			return 0;
		}
	}
	
	public boolean setLearnMode(int value) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		switch(value) {
		case NON_LINE_LEARN:
			prefEdit.putString("learn_mode", "non-line-learn");
			break;
		case HERF_LINE_LEARN:
			prefEdit.putString("learn_mode", "harf-line-learn");
			break;
		case LINE_LEARN:
			prefEdit.putString("learn_mode", "line-learn");
			break;
		default:
			return false;
		}
		prefEdit.commit();
		return true;
	}

	// ----------------------------------------------------------------------------
	
	public boolean isLearningBackEnable() {
		return this.pref.getBoolean("learn_unfinish_back", Config.LEARNING_BACK_ENABLE);
	}
	public void setLearningBackEnable(boolean value) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		prefEdit.putBoolean("learn_unfinish_back", value);
		prefEdit.commit();
	}
	public boolean isExitEnable() {
		return this.pref.getBoolean("learn_exit", Config.EXIT_ENABLE);
	}
	public void setExitEnable(boolean value) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		prefEdit.putBoolean("learn_exit", value);
		prefEdit.commit();
	}
	
	
}
