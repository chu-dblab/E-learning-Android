package tw.edu.chu.csie.e_learning.scanner;

import tw.edu.chu.csie.e_learning.ui.MaterialActivity;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class NFCDetect extends Activity 
{
	private PendingIntent gNfcPendingIntent;
	private NfcAdapter gNfcAdapter;
	private IntentFilter[] gNdefExchangeFilters;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 註冊讓該Activity負責處理所有接收到的NFC Intents。
		gNfcPendingIntent = PendingIntent.getActivity(this, 0,
		        // 指定該Activity為應用程式中的最上層Activity
		        new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		// 建立要處理的Intent Filter負責處理來自Tag的資料。
		IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
		    ndefDetected.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) { }
		gNdefExchangeFilters = new IntentFilter[] { ndefDetected };
	}
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		gNfcAdapter.disableForegroundDispatch(this);
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
	         String url = new String(payload);
	        // 往下送出該intent給其他的處理對象
	        setIntent(new Intent()); 
	    }
	    // 啟動前景模式支持Nfc intent處理
	    enableNdefExchangeMode();
	}
	
	private void enableNdefExchangeMode() {

	    // 讓NfcAdapter啟動能夠在前景模式下進行intent filter的dispatch。
	    gNfcAdapter.enableForegroundDispatch(this, gNfcPendingIntent, gNdefExchangeFilters, null);
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
	    // 覆寫該Intent用於補捉如果有新的Intent進入時，可以觸發的事件任務。
	    // NDEF exchange mode
	    if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			NdefMessage[] msgs = getNdefMessages(intent);
	    }
	}
	
	public void searchFileInDatabase(final NdefMessage msg)
	{
		String str = new String(msg.getRecords()[0].getPayload());
		//發送Intent給webview
		Intent startLearning = new Intent(NFCDetect.this,MaterialActivity.class);
		startLearning.putExtra("ID", str);
		startActivityForResult(startLearning,1);
	}
}
