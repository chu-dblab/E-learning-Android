package tw.edu.chu.csie.e_learning.ui.dialog;

import tw.edu.chu.csie.e_learning.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class LoginDialogBuilder extends AlertDialog.Builder implements OnClickListener{
	String dialogTitle = "登入視窗";
	
	Context context;
	LayoutInflater inflater;
	public final View view;
	
	public LoginDialogBuilder(Context context) {
		super(context);
		this.context = context;
		
		inflater = LayoutInflater.from(this.context);
		view = inflater.inflate(R.layout.dialog_user_login, null);
		
		this.setTitle(dialogTitle);
		this.setView(view);
		this.setPositiveButton(android.R.string.ok, null);
		this.setNegativeButton(android.R.string.cancel, this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		// 按下取消紐
		case AlertDialog.BUTTON_NEGATIVE:
			dialog.dismiss();
		}
	}


}
