package tw.edu.chu.csie.e_learning.scanner;

import tw.edu.chu.csie.e_learning.ui.MaterialActivity;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		InitialDetect();
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
		adapter.enableForegroundDispatch(this, nfcPendingIntent, nfcFilter, tech_list);
	}

	public void InitialDetect()
	{
		manager = (NfcManager)getSystemService(Context.NFC_SERVICE);
		adapter = manager.getDefaultAdapter();
		nfc_intent = new Intent(this,getClass());
		nfcPendingIntent = PendingIntent.getActivity(this, 0, nfc_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		nfc_tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		nfcFilter = new IntentFilter[]{nfc_tech};
		tech_list = new String[][]{new String[]{NfcA.class.getName()}};
	}
	
	public void callMaterial(Intent intent)
	{
		String action = intent.getAction();
		Log.d("Tag_type",action);
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
		{
			Tag tagContent = intent.getParcelableExtra(action);
			materialID = readTagContent(tagContent);
			sentIntentToMaterial(materialID);
		}
		else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
		{
			Tag tagContent = intent.getParcelableExtra(action);
			materialID = readTagContent(tagContent);
			sentIntentToMaterial(materialID);
		}
		else if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action))
		{
			Tag tagContent = intent.getParcelableExtra(action);
			materialID = readTagContent(tagContent);
			sentIntentToMaterial(materialID);
		}
		else
		{
			//do nothing
		}
	}
	
	private String readTagContent(Tag tag)
	{
		return null;
	}
	
	private void sentIntentToMaterial(String targetID)
	{
		Intent learningTarget = new Intent(this,MaterialActivity.class);
		learningTarget.putExtra("materialID", targetID);
		startActivityForResult(learningTarget,1);
	}
}
