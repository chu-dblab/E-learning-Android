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
import android.util.Log;

public class NFCDetect
{
	private Context baseContext;
	private NfcManager manager;
	private NfcAdapter adapter;
	private IntentFilter nfc_tech;
	private IntentFilter[] nfcFilter;
	private PendingIntent nfcPendingIntent;
	private Intent nfc_intent;
	private String[][] tech_list;
	private Activity act;
	public NFCDetect(Context context)
	{
		this.baseContext = context;
	}
	
	public void InitialDetect()
	{
		manager = (NfcManager)act.getSystemService(Context.NFC_SERVICE);
		adapter = manager.getDefaultAdapter();
		nfc_intent = new Intent(this.baseContext,getClass());
		nfcPendingIntent = PendingIntent.getActivity(this.baseContext, 0, nfc_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
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
		}
		else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
		{
			
		}
		else if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action))
		{
			
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
		Intent learningTarget = new Intent(this.baseContext,MaterialActivity.class);
		learningTarget.putExtra("materialID", targetID);
		act.startActivityForResult(learningTarget,1);
	}
	
	public void setForegroundDispatchOnPause(Activity activity)
	{
		adapter.disableForegroundDispatch(activity);
	}
	
	public void setForegroundDispatchOnResume(Activity activity)
	{
		adapter.enableForegroundDispatch(activity, nfcPendingIntent, nfcFilter, tech_list);
	}
}
