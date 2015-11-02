package com.bell.belltools.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import com.bell.belltools.R;
import com.bell.belltools.R.id;
import com.bell.belltools.R.layout;
import com.bell.belltools.adapter.FileAdapter;
import com.bell.belltools.bean.FileBean;
import com.bell.belltools.utils.Encrypt;
import com.bell.belltools.utils.MyToast;
import com.bell.belltools.utils.ProgressDialog;
import com.bell.belltools.utils.RootPermission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**  
 * @info   
 *  
 * @author	GuoJiang  
 *  	
 */
@SuppressLint({ "SimpleDateFormat", "HandlerLeak", "DefaultLocale", "InflateParams" })
public class FileManagerActivity extends Activity {
	
	private Context mContext;
	private LinearLayout backLayout;
	private TextView mTextView;
	private TextView nullTextView;
	private ImageView closeImg;
	private ListView mListView;
	private ArrayList<FileBean> fileList = new ArrayList<FileBean>();
	private FileAdapter adapter;
	
	private AlertDialog dialog;
	
	private String openUrl = "";
	private boolean isEncrypt = false;
	private final int REFRESH = 0;
	private final int ENCRYPT_SUCCESS = 1;
	private final int ENCRYPT_FAIL = 2;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			
			case REFRESH:
				mTextView.setText(openUrl);
				adapter.notifyDataSetChanged();
				break;
				
			case ENCRYPT_SUCCESS:
				MyToast.showToast(mContext, "操作成功");
				getFileList(openUrl);
				break;
				
			case ENCRYPT_FAIL:
				MyToast.showToast(mContext, "操作失败，请检查文件或目录权限");
				break;

			}
		}};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_filemanager);
		mContext = this;
		
		init();
		
		getFileList(getSDCardPath());
		
	}

	private void init() {
		backLayout = (LinearLayout) findViewById(R.id.id_back_layout);
		mTextView = (TextView) findViewById(R.id.id_title);
		backLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Backup();
				
			}
		});
		
		closeImg = (ImageView) findViewById(R.id.id_close_img);
		closeImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		nullTextView = (TextView) findViewById(R.id.id_null_text);
		nullTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Backup();
			}
		});
		mListView = (ListView) findViewById(R.id.id_listview);
		adapter = new FileAdapter(mContext, fileList);
		mListView.setAdapter(adapter);
		mListView.setEmptyView(nullTextView);
		
		// 点击item进行加密判断
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!fileList.get(position).isFile()) {
					//打开下一级目录
					getFileList(fileList.get(position).getFilePath());
				} else {
					if (new File(fileList.get(position).getFilePath()).canWrite()) {
						//弹出加密框 
						showDialog(fileList.get(position).getFilePath());
					} else {
						
						MyToast.showToast(mContext, "文件无操作权限");
					}
				}
			}
			
		});
	}

	/**
	 * 弹出加密提示框
	 * 
	 * @param url
	 */
	private void showDialog(final String url){
		View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_encrypt, null);

		dialog = new AlertDialog.Builder(mContext).create();
		dialog.setView(dialogView,0,0,0,0);
		dialog.setCancelable(false);
		dialog.show();
		
		TextView dTitle = (TextView) dialogView.findViewById(R.id.id_dialog_title);
		TextView dContent = (TextView) dialogView.findViewById(R.id.id_dialog_content);
		Button dCancel = (Button) dialogView.findViewById(R.id.id_dialog_cancel);
		Button dEnter = (Button) dialogView.findViewById(R.id.id_dialog_enter);
		
		dTitle.setText("是否要对此文件进行加密处理");
		dContent.setText(url);
		//取消加密
		dCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		//确定加密
		dEnter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EncryptTask task = new EncryptTask();
				task.execute(url);
			}
		});
		
	}
	
	
	/**  
	 * @info   异步加密操作
	 *  
	 * @author	GuoJiang  
	 *  	
	 */
	class EncryptTask extends AsyncTask<String, String, String>{
		//进度效果
		Dialog progressDialog = ProgressDialog.showProgress(mContext, "", true);
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.cancel();
			progressDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			//调用加密
			if (Encrypt.encryptFile(mContext, params[0])) {
				isEncrypt = true;
			} else {
				isEncrypt = false;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.cancel();
			if (isEncrypt) {
				mHandler.sendEmptyMessage(ENCRYPT_SUCCESS);
			} else {
				mHandler.sendEmptyMessage(ENCRYPT_FAIL);
			}
			
		}
		
	}
	
	/**
	 * 返回上一页
	 */
	protected void Backup() {
		if (!mTextView.getText().toString().equals("/")) {
			getFileList(new File(mTextView.getText().toString()).getParent());
		} 
	}

	/**
	 * 查询URL地址下的文件列表
	 * @param url
	 */
	private void getFileList(String url){
		File file = new File(url);
		//文件存在并可读
		if (file.exists() && file.canRead()) {
			File[] files = file.listFiles();
			if (files!=null) {
				//文件排序
				files = sortFiles(files);
				fileList.clear();
				for (int i = 0; i < files.length; i++) {
					FileBean bean = new FileBean();
					bean.setFileName(files[i].getName());
					bean.setFilePath(files[i].getPath());
					if (files[i].isFile()) {
						bean.setFileSize(fileSizeFormat(files[i].length()));
						bean.setFileTime(createTime(files[i]));
						bean.setFile(true);
					} else {
						bean.setFileSize("");
						bean.setFileTime("");
						bean.setFile(false);
					}
					fileList.add(bean);
				}
				
				openUrl = url;
				mHandler.sendEmptyMessage(REFRESH);
				
			} else {
				MyToast.showToast(mContext, "文件夹为空");
			}
		} else {
			MyToast.showToast(mContext, "文件夹无访问权限");
		}
	}

	
	/**
	 * 文件列表排序
	 * 
	 * @param files
	 * @return
	 */
	private File[] sortFiles(File[] files) {
		ArrayList<File> list1 = new ArrayList<File>();
		ArrayList<File> list2 = new ArrayList<File>();
		ArrayList<File> list3 = new ArrayList<File>();
		
		for (File file : files) {
			if (file.isFile()) {
				list1.add(file);
			} else {
				list2.add(file);
			}
		}
		
		Collections.sort(list1);
		Collections.sort(list2);
		
		list3.addAll(list2);
		list3.addAll(list1);
		
		File[] result = new File[list3.size()];
		for (int i = 0; i < list3.size(); i++) {
			result[i] = list3.get(i);
		}
		return result;
		
	}
	
	/**
	 * 文件大小格式化
	 * 
	 * @param size
	 * @return
	 */
	private String fileSizeFormat(long size){
		long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
 
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
	}

	/**
	 * 获取默认SDCard路径
	 * @return
	 */
	public String getSDCardPath(){
		File cardDir = null;
		boolean isExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		//存在SDCard时取card目录，不存在时取根目录 
		if (isExist) {
			cardDir = Environment.getExternalStorageDirectory();
		} else {
			cardDir = new File("/");
		}
		
		return cardDir.toString();
	}
	
	/**
	 * 获取文件最后修改时间
	 * 
	 * @param file
	 * @return
	 */
	private String createTime(File file){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(file.lastModified());
		return sdf.format(cal.getTime());
	}

	
	
	/**
	 * 屏蔽返回键按钮
	 * 
	 * @param keyCode
	 * @param event
	 * @return
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if (openUrl.equals("/")) {
				finish();
			} else {
				Backup();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}
