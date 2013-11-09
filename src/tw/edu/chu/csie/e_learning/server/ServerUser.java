package tw.edu.chu.csie.e_learning.server;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerUser {

	private String logincode;
	private String login_time;
	private String uid;
	private String gid;
	private String gname;
	private String realname, nickname;
	private String email;
	
	/*public ServerUser(String logincode) {
		
	}*/
	
	/*public ServerUser(String uid, String passwd) {
		
	}*/
	
	public ServerUser(JSONObject jsonData) throws JSONException {
		this.logincode = jsonData.getString("ucode");
		this.uid = jsonData.getString("uid");
		this.gid = jsonData.getString("ugid");
		this.gname = jsonData.getString("ugname");
		this.realname = jsonData.getString("urealname");
		this.nickname = jsonData.getString("unickname");
		this.email = jsonData.getString("uemail");
		this.login_time = jsonData.getString("ulogin_time");
	}
	
	public String getLoginCode() {
		return this.logincode;
	}
	
	public String getLoginTime()
	{
		return this.login_time;
	}
	
	public String getID() {
		return this.uid;
	}
	
	public String getGroupID() {
		return this.gid;
	}
	
	public String getGroupName() {
		return this.gname;
	}
	
	public String getRealName() {
		return this.realname;
	}
	
	public String getNickName() {
		return this.nickname;
	}
	
	public String getEmail() {
		return this.email;
	}

}
