package tw.edu.chu.csie.e_learning.scanner;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.os.Bundle;

public class NFCDetect extends Activity 
{
	public NFCDetect()
	{
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	public NdefMessage[] getNdefMessages(Intent intent)
	{
		return null;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
	
	
}
