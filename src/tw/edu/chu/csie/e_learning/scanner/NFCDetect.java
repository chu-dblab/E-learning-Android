package tw.edu.chu.csie.e_learning.scanner;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

public class NFCDetect extends Activity 
{
	public NFCDetect()
	{
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    // 處理由Android系統送出應用程式處理的intent filter內容
	    if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
	        // 取得NdefMessage
	        NdefMessage[] messages = getNdefMessages(getIntent());
	        // 取得實際的內容
	        byte[] payload = messages[0].getRecords()[0].getPayload();
	        //setNoteBody(new String(payload));
	        // 往下送出該intent給其他的處理對象
	        setIntent(new Intent()); 
	    }
	}
	
	public NdefMessage[] getNdefMessages(Intent intent)
	{
	    // Parse the intent
	    NdefMessage[] msgs = null;
	    String action = intent.getAction();
	    // 識別目前的action為何
	    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) 
	    {
	        // 取得parcelabelarrry的資料
	        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	        // 取出的內容如果不為null，將parcelable轉成ndefmessage

	        if (rawMsgs != null) {
	            msgs = new NdefMessage[rawMsgs.length];
	            for (int i = 0; i < rawMsgs.length; i++) {
	                msgs[i] = (NdefMessage) rawMsgs[i];
	            }
	        } 
	       else {
	            // Unknown tag type
	            byte[] empty = new byte[] {};
	            NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
	            NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
	            msgs = new NdefMessage[] { msg };
	        }
	     } 
	    else {
	       // Log.d(TAG, "Unknown intent.");
	        finish();
	    }
	    return msgs;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
	
	
}
