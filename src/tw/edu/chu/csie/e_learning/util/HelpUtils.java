/*
 * �⊥�銝摮貊��嗆��飛蝧�撘���
 * A Hybrid Ubiquitous Learning Framework and its Navigation Support Mechanism
 * 
 * FileName:	HelpUtils.java
 *
 * 雿�:	��嚚�
 * �湔鞈�:
 *   2013.4.29
 *    - ���砍�獢耨�嫣�銝�
 * 
 *   隞亙���銋��andomNumber��神��箇��芷�靘
 *   �� v1.0 -2012.9.25
 *      �� 撠��砍MainActivity.java鋆∠�showAboutDialog()�批捆�賢���lass
 *      �� 閫�捱�杗ndroid 2.x��銝�雿銝駁����暹�摨�摮���嚗�摮�
 * 
 * Description: �����"�舀"鞈�
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
	//憿舐內"�"鞈���閰望�	
	@SuppressWarnings("deprecation")
	public static void showAboutDialog(Context context){
		//撱箇�撠店�孵�AlertDialog
		AlertDialog about_AlertDialog = new AlertDialog.Builder(context).create();
		about_AlertDialog.setTitle(R.string.about);	//閮剖�AlertDialog璅�
		
		//"�"閬��批捆鋆∪遣蝡ayout�Ｘ
				ScrollView about_AlertDialog_scrollView = new ScrollView(context); 
				LinearLayout about_AlertDialog_content = new LinearLayout(context);
				about_AlertDialog_content.setOrientation(LinearLayout.VERTICAL);	//閮剖��箇��layout
				about_AlertDialog_content.setPadding(
						context.getResources().getDimensionPixelSize(R.dimen.about_dialog_margin), 
						context.getResources().getDimensionPixelSize(R.dimen.about_dialog_margin), 
						context.getResources().getDimensionPixelSize(R.dimen.about_dialog_margin), 
						context.getResources().getDimensionPixelSize(R.dimen.about_dialog_margin));	//閮剖�layout���之撠�撌艾�銝��喋�銝�
		
		//"�"閬��批捆鋆⊿�批捆摮葡
		TextView content_textView = new TextView(context);
		content_textView.setTextAppearance(context, android.R.style.TextAppearance_Medium);	//����璅���箔葉蝑之撠�
		//content_textView.setAutoLinkMask(Linkify.ALL);	//閮剖����芸������
		
		try{
			//摰��"��憟辣鞈�"�隞�
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
		
		//�����Ｘ�圈���閰望�
		about_AlertDialog_scrollView.addView(about_AlertDialog_content);
		about_AlertDialog.setView(about_AlertDialog_scrollView);
		
		about_AlertDialog.setButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		about_AlertDialog.show();
	}
	
}
