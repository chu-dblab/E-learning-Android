package tw.edu.chu.csie.e_learning.scanner;

import java.io.IOException;
import java.nio.charset.Charset;

import com.google.zxing.maxicode.MaxiCodeReader;

import tw.edu.chu.csie.e_learning.ui.MaterialActivity;
import tw.edu.chu.csie.e_learning.util.AccountUtils;
import tw.edu.chu.csie.e_learning.util.FileUtils;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

public class NFCDetect extends Activity
{
	private NfcManager manager;
	private NfcAdapter adapter;
	private IntentFilter nfc_tech;
	private IntentFilter[] nfcFilter;
	private PendingIntent nfcPendingIntent;
	private Intent nfc_intent;
	private String[][] tech_list;
	private String materialID;
	private static final String TAG = NFCDetect.class.getSimpleName();
	private FileUtils filepath = new FileUtils();
	private AccountUtils logincheck = new AccountUtils(getBaseContext());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(!logincheck.islogin()) {
			finish();
		}
		else InitialDetect();
	}
	
	public void InitialDetect()
	{
		//manager = (NfcManager)getSystemService(Context.NFC_SERVICE);
		adapter = NfcAdapter.getDefaultAdapter(this);
		nfc_intent = new Intent(this,getClass());
		nfcPendingIntent = PendingIntent.getActivity(this, 0, nfc_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		nfc_tech = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			nfc_tech.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) { }
		nfcFilter = new IntentFilter[]{nfc_tech};
		tech_list = new String[][]{new String[]{NfcA.class.getName()}};
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		callMaterial(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		adapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
	        // 取得NdefMessage
	        NdefMessage[] messages = getNdefMessages(getIntent());
	        // 取得實際的內容
	        byte[] payload = messages[0].getRecords()[0].getPayload();
	        // 往下送出該intent給其他的處理對象
	        setIntent(new Intent()); 
	    }
		adapter.enableForegroundDispatch(this, nfcPendingIntent, nfcFilter, tech_list);
	}
	
	public void callMaterial(Intent intent)
	{
		String action = intent.getAction();
		Log.d("Tag_type",action);
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
		{
			NdefMessage[] msg = getNdefMessages(intent);
			materialID = new String(msg[0].getRecords()[0].getPayload());
			sentIntentToMaterial(materialID);
		}
	}
	
	private NdefMessage[] getNdefMessages(Intent intent) {
	    // Parse the intent
	    NdefMessage[] msgs = null;
	    String action = intent.getAction();
	    // 識別目前的action為何
	    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
	            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
	        // 取得parcelabelarrry的資料
	        Parcelable[] rawMsgs = 
	            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	        // 取出的內容如果不為null，將parcelable轉成ndefmessage
	        if (rawMsgs != null) {
	            msgs = new NdefMessage[rawMsgs.length];
	            for (int i = 0; i < rawMsgs.length; i++) {
	                msgs[i] = (NdefMessage) rawMsgs[i];
	            }
	        } else {
	            // Unknown tag type
	            byte[] empty = new byte[] {};
	            NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
	            NdefMessage msg = new NdefMessage(new NdefRecord[] {
	                record
	            });
	            msgs = new NdefMessage[] {
	                msg
	            };
	        }
	    } else {
	        Log.d(TAG, "Unknown intent.");
	        finish();
	    }
	    return msgs;
	}
	
	private void sentIntentToMaterial(String targetID)
	{
		Intent learningTarget = new Intent(this,MaterialActivity.class);
		learningTarget.putExtra("materialID", filepath.getMaterialPath()+targetID);
		startActivityForResult(learningTarget,1);
	}
}
