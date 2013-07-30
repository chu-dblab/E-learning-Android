/*
 * 無所不在學習架構與學習導引機制
 * A Hybrid Ubiquitous Learning Framework and its Navigation Support Mechanism
 * 
 * FileName:	HelpUtils.java
 *
 * 作者:	元兒～
 * 更新資訊:
 *   2013.4.29
 *    - 針對本專案修改一下
 * 
 *   以元兒～之前的RandomNumber所寫的為基礎挪過來用
 *   └─ v1.0 -2012.9.25
 *      ├─ 將原本在MainActivity.java裡的showAboutDialog()內容抽出成這個class
 *      └─ 解決在Android 2.x手機上因佈景主題而呈現暗底黑字的問題（改成白字）
 * 
 * Description: 所有有關"支援"資訊
 */
package tw.edu.chu.csie.e_learning.util;

import tw.edu.chu.csie.e_learning.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class HelpUtils {
	//顯示"關於"資訊的對話框	
	public static void showAboutDialog(Context context){
		//建立對話方塊AlertDialog
		AlertDialog about_AlertDialog = new AlertDialog.Builder(context).create();
		about_AlertDialog.setTitle(R.string.about);	//設定AlertDialog標題
		
		//"關於"視窗內容裡建立Layout面板
				ScrollView about_AlertDialog_scrollView = new ScrollView(context); 
				LinearLayout about_AlertDialog_content = new LinearLayout(context);
				about_AlertDialog_content.setOrientation(LinearLayout.VERTICAL);	//設定為直向的layout
				about_AlertDialog_content.setPadding(
						context.getResources().getDimensionPixelSize(R.dimen.about_dialog_margin), 
						context.getResources().getDimensionPixelSize(R.dimen.about_dialog_margin), 
						context.getResources().getDimensionPixelSize(R.dimen.about_dialog_margin), 
						context.getResources().getDimensionPixelSize(R.dimen.about_dialog_margin));	//設定layout的邊界大小（左、上、右、下）
		
		//"關於"視窗內容裡面內容字串
		TextView content_textView = new TextView(context);
		content_textView.setTextAppearance(context, android.R.style.TextAppearance_Medium);	//指定文字樣式為中等大小
		//content_textView.setAutoLinkMask(Linkify.ALL);	//設定成會自動加上連結
		
		try{
			//宣告"取得套件資訊"的物件
			PackageInfo package_info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			/*PackageManager package_manager = this.getPackageManager();
			PackageInfo package_info = package_manager.getPackageInfo(this.getPackageName(), 0);*/
			
			content_textView.setText(
					context.getString(R.string.app_name) + "\n\n"
					+ context.getString(R.string.package_name) +"\n"+ package_info.packageName + "\n"
					+ context.getString(R.string.version) + package_info.versionName + "\n"
					+ "\n");
		} catch (NameNotFoundException ex) {
			content_textView.setText(context.getString(R.string.getPackageInfo_error)+ "\n");
			//e.printStackTrace();
		} catch(Exception ex){
			Toast.makeText(context, context.getString(R.string.inside_process_error), Toast.LENGTH_LONG).show();
			//e.printStackTrace();
		}
		about_AlertDialog_content.addView(content_textView);
		
		//指定這個面板到這個對話框
		about_AlertDialog_scrollView.addView(about_AlertDialog_content);
		about_AlertDialog.setView(about_AlertDialog_scrollView);
		
		about_AlertDialog.setButton(0,context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		about_AlertDialog.show();
	}
	
}
