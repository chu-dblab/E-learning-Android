/*
 * 無所不在學習架構與學習導引機制
 * A Hybrid Ubiquitous Learning Framework and its Navigation Support Mechanism
 * 
 * FileName:	MainFunctionActivity.java
 *
 * Description: 使用者登入後主要的畫面（含開始學習的學習地圖、個人資訊）
 * 
 */
package tw.edu.chu.csie.e_learning.ui;

import java.io.IOException;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import tw.edu.chu.csie.e_learning.R;
import tw.edu.chu.csie.e_learning.R.id;
import tw.edu.chu.csie.e_learning.R.layout;
import tw.edu.chu.csie.e_learning.R.menu;
import tw.edu.chu.csie.e_learning.R.string;
import tw.edu.chu.csie.e_learning.config.Config;
import tw.edu.chu.csie.e_learning.scanner.QRCodeScanner;
import tw.edu.chu.csie.e_learning.scanner.NFCDetect;
import tw.edu.chu.csie.e_learning.server.exception.HttpException;
import tw.edu.chu.csie.e_learning.server.exception.LoginCodeException;
import tw.edu.chu.csie.e_learning.server.exception.PostNotSameException;
import tw.edu.chu.csie.e_learning.server.exception.ServerException;
import tw.edu.chu.csie.e_learning.ui.UserLoginActivity.UserLoginTask;
import tw.edu.chu.csie.e_learning.util.AccountUtils;
import tw.edu.chu.csie.e_learning.util.FileUtils;
import tw.edu.chu.csie.e_learning.util.HelpUtils;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class MainFunctionActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private LogoutTask mLogoutTask;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_function);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		mViewPager.setCurrentItem(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_function, menu);
		
		// DEBUG 開啟教材內容測試
		menu.add(0, 212, 0, "Tester");
		menu.add(0, 213, 0, "教材測試");
		
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_about:
			HelpUtils.showAboutDialog(this);
			break;
		case R.id.menu_material_downloader:
			Intent toTextbookDownloader = new Intent(MainFunctionActivity.this, MaterialDownloaderActivity.class);
			startActivity(toTextbookDownloader);
			break;
		case R.id.menu_logout:
			mLogoutTask = new LogoutTask();
			mLogoutTask.execute();
			//Toast.makeText(this, String.valueOf(new AccountUtils(this).islogin()), 0).show();
			break;
		case R.id.menu_qrcode_scan:
			Intent toQRScan = new Intent(MainFunctionActivity.this, QRCodeScanner.class);
			startActivity(toQRScan);
			break;
       // DEBUG 開啟教材內容測試
		case 212:
			Intent toTester = new Intent(MainFunctionActivity.this, TesterActivity.class);
			startActivity(toTester);
			break;
       case 213:
    	   Intent toLearning = new Intent(MainFunctionActivity.this, MaterialActivity.class);
    	   toLearning.putExtra("materialId", 1);
    	   startActivityForResult(toLearning, 1);
    	   break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment userStatus_fragment = new UserStatusFragment();
			Fragment learnMap_fragment = new LearnMapFragment();
			switch(position){
			case 0:
				return userStatus_fragment;
			case 1:
				return learnMap_fragment; 
			default:
				return userStatus_fragment;
			}
			/*Fragment fragment	= new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);*/
			//return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.user_status).toUpperCase(l);
			case 1:
				return getString(R.string.learn_map).toUpperCase(l);
			}
			return null;
		}
	}
	
	public class LogoutTask extends AsyncTask<Void, Integer, Boolean> {

		private AccountUtils accountUtils = new AccountUtils(getBaseContext());
		private ProgressDialog updateProgress;
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// DEBUG 當啟用無連線登入，進入學習畫面
			if(Config.DEBUG_NO_CONNECT_LOGIN) {
				return true;
			}
			else {
				try {
					accountUtils.logoutUser();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PostNotSameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LoginCodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return true;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			updateProgress = new ProgressDialog((Activity)MainFunctionActivity.this);
			// TODO 拉開成String
			updateProgress.setMessage("登出中......");
    		updateProgress.setCancelable(false);
    		updateProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		updateProgress.show();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			updateProgress.dismiss();
			((Activity)MainFunctionActivity.this).finish();
			//super.onPostExecute(result);
		}
	}

	/**
	 * 學生狀態
	 */
	public static class UserStatusFragment extends Fragment {

		public UserStatusFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_main_function_user_status, container, false);
			/*TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);*/
			return rootView;
		}
	}
	
	/**
	 * 學習地圖
	 *
	 */
	public static class LearnMapFragment extends Fragment {
		
		private FileUtils fileUtils;
		
		private View rootView;
		private ImageView mapView;
//		private Bitmap bmp;
		private TextView nextPointView, nextPointTimeView;
		
		public LearnMapFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			fileUtils = new FileUtils();
			rootView = inflater.inflate(R.layout.fragment_main_function_learn_map, container, false);
			mapView = (ImageView)rootView.findViewById(R.id.learning_map);
			// TODO DEBUG
			Bitmap bmp = BitmapFactory.decodeFile(fileUtils.getMaterialPath()+"map/map_04.jpg");
			Toast.makeText(getActivity(), "file://"+fileUtils.getMaterialPath()+"map/map_04.jpg", 1).show();
			mapView.setImageBitmap(bmp);
			
			nextPointView = (TextView)rootView.findViewById(R.id.learning_next_point);
			nextPointTimeView = (TextView)rootView.findViewById(R.id.learning_next_point_time);
			
			
			return rootView;
		}
		
		private void updateNextPointUI() {
			
		}
		
		/**
		 * 變更學習地圖的學習點
		 * @param pointNum 學習點編號
		 * @return 是否輸入正確
		 */
		private void changeMapToPoint(int pointNum) {
			//bmp = BitmapFactory.decodeFile("file://"+fileUtils.getMaterialPath()+"map/map_"+pointNum+".jpg");
			//mapView.setImageURI();
		}
		
		/*private String getMapFileNameToPoint(int pointNum) {
			String prefix = "map_";
			if(pointNum>=1 && pointNum<=15) {
				if(pointNum<=3) {
					
				}
			}
			else {
				return null;
			}
		}*/
	}

}
