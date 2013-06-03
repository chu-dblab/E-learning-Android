/*
 * 無所不在學習架構與學習導引機制
 * A Hybrid Ubiquitous Learning Framework and its Navigation Support Mechanism
 * 
 * FileName:	AccountUtils.java
 * 
 * Description: 帳號控制管理（登入、驗證、登入狀況...）的程式
 * 
 */
package tw.edu.chu.csie.e_learning.util;

public class AccountUtils {
	/**
	 * 
	 */
	private static boolean isLogined;
	private static String loginedId;
	
	
	/**
	 * 是否是已登入狀態
	 */
	public boolean islogin(){
		return isLogined;
	}
	
	/**
	 * 察看已登入的ID
	 */
	public String getLoginId() {
		return loginedId;
	}
	/**
	 * 登入帳號
	 * @param inputLoginId 使用者輸入的ID
	 * @param inputLoginPasswd 使用者輸入的密碼
	 * @return 是否登入成功
	 */
	public boolean loginUser(String inputLoginId, String inputLoginPasswd){
		//TODO 騰入登入動作
		
		return false;
	}
	/**
	 * 登出帳號
	 * @return 是否登出成功
	 */
	public boolean logoutUser(){
		isLogined = false;
		//TODO 清除登入資訊
		
		return true;
	}
}
