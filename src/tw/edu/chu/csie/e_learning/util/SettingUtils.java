package tw.edu.chu.csie.e_learning.util;

import tw.edu.chu.csie.e_learning.config.Config;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 偏好設定類別
 * @author yuan
 *
 */
public class SettingUtils {
	
	public static final int NON_LINE_LEARN = 101;
	public static final int HERF_LINE_LEARN = 102;
	public static final int LINE_LEARN = 103;
	
	
	private Context context;
	private SharedPreferences pref;
	
	/**
	 * 偏好設定類別建構子
	 * @param context 帶入Android基底Context
	 */
	public SettingUtils(Context context) {
		this.context = context;
		this.pref = PreferenceManager.getDefaultSharedPreferences(this.context);
	}
	
	/**
	 * 取得Android SharedPreferences物件
	 * @return SharedPreferences物件
	 */
	public SharedPreferences getSharedPreferences() {
		return this.pref;
	}
	
	/**
	 * 重設所有設定過的偏好設定選項
	 */
	public void reset() {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		prefEdit.clear();
		prefEdit.commit();
	}
	
	// ============================================================================
	
	/**
	 * 取得連結後端的網址
	 * @return 後端的網址
	 */
	public String getRemoteURL() {
		return pref.getString("remote_url", Config.REMOTE_BASE_URL);
	}
	
	/**
	 * 設定連結後端的網址
	 * @param setURL 後端的網址
	 */
	public void setRemoteURL(String setURL) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		if(setURL.isEmpty() || setURL.equals("")) {
			prefEdit.remove("remote_url");
			prefEdit.commit();
		} else {
			prefEdit.putString("remote_url", setURL);
			prefEdit.commit();
		}
	}
	
	/**
	 * 取得教材下載網址
	 * @return 教材下載網址
	 */
	public String getRemoteMaterialURL() {
		return pref.getString("remote_material_url", Config.REMOTE_MATERIAL_URL);
	}
	/**
	 * 設定教材下載網址
	 * @param setURL 教材下載網址
	 */
	public void setRemoteMaterialURL(String setURL) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		if(setURL.isEmpty() || setURL.equals("")) {
			prefEdit.remove("remote_material_url");
			prefEdit.commit();
		} else {
			prefEdit.putString("remote_material_url", setURL);
			prefEdit.commit();
		}
	}
	
	// ----------------------------------------------------------------------------
	
	/**
	 * 是否為學生模式
	 * <p>
	 * 當啟用學生模式時，部份功能（如更改是否鎖定）將會鎖住，不讓學生更改。
	 * 
	 * @return <code>true</code>目前為學生模式
	 */
	public boolean isStudentMode() {
		return this.pref.getBoolean("student_mode", Config.STUDENT_MODE);
	}
	
	/**
	 * 更改是否為學生模式
	 * @param setTF <code>true</code>設定為學生模式
	 */
	public void setStudentMode(boolean setTF) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		prefEdit.putBoolean("student_mode", setTF);
		prefEdit.commit();
	}
	
	/**
	 * 取得目前是什麼學習模式
	 * 
	 * <p>
	 * 提供的選項有: 
	 * <ul>
	 * <li>SettingUtils.LINE_LEARN 線性
	 * <li>SettingUtils.HERF_LINE_LEARN 半線性(尚未實作)
	 * <li>SettingUtils.NON_LINE_LEARN 非線性(尚未實作)
	 * </ul>
	 * 
	 * @return 目前是什麼學習模式代號
	 */
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
	
	/**
	 * 設定目前是什麼學習模式
	 * 
	 * <p>
	 * 提供的選項有: 
	 * <ul>
	 * <li>SettingUtils.LINE_LEARN 線性
	 * <li>SettingUtils.HERF_LINE_LEARN 半線性(尚未實作)
	 * <li>SettingUtils.NON_LINE_LEARN 非線性(尚未實作)
	 * @param setValue 設定是什麼學習模式代號
	 * @return 是否有設定成功
	 */
	public boolean setLearnMode(int setValue) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		switch(setValue) {
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
	
	/**
	 * 是否允許還沒作答即離開此學習點（當作此學習點學完）
	 * @return 是否允許離開此學習點
	 */
	public boolean isLearningBackEnable() {
		return this.pref.getBoolean("learn_unfinish_back", Config.LEARNING_BACK_ENABLE);
	}
	/**
	 * 設定是否允許還沒作答即離開此學習點（當作此學習點學完）
	 * @param setTF 是否允許離開此學習點
	 */
	public void setLearningBackEnable(boolean setTF) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		prefEdit.putBoolean("learn_unfinish_back", setTF);
		prefEdit.commit();
	}
	
	/**
	 * 是否允許結束離開此應用程式
	 * @return <code>true</code>允許結束離開此應用程式
	 */
	public boolean isExitEnable() {
		return this.pref.getBoolean("learn_exit", Config.EXIT_ENABLE);
	}
	
	/**
	 * 設定是否允許結束離開此應用程式
	 * @param setTF <code>true</code>允許結束離開此應用程式
	 */
	public void setExitEnable(boolean setTF) {
		SharedPreferences.Editor prefEdit = this.pref.edit();
		prefEdit.putBoolean("learn_exit", setTF);
		prefEdit.commit();
	}
	
	
}
