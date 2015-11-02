package com.bell.belltools.utils;  

import com.bell.belltools.R;
import com.bell.belltools.R.drawable;
import com.bell.belltools.R.id;
import com.bell.belltools.R.layout;
import com.bell.belltools.R.style;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/*  
 * @info   
 * @author GuoJiang 
 * @time 2015年10月28日 下午5:10:03  
 * @version    
 */
public class ProgressDialog {
	
	/**
	 * 进度加载效果
	 * 
	 * @param mContext
	 * @param msg
	 * @param isClose
	 * @return
	 */
	public static Dialog showProgress(Context mContext, String msg, boolean isClose){
		int width = mContext.getResources().getDrawable(R.drawable.loading1).getIntrinsicWidth();
		int height = mContext.getResources().getDrawable(R.drawable.loading1).getIntrinsicHeight();
		
		View inflater = LayoutInflater.from(mContext).inflate(
				R.layout.progress_loading, null);
		TextView msgText = (TextView) inflater.findViewById(R.id.loading_msg);
		msgText.setText(msg);
		ProgressBar progressBar = (ProgressBar) inflater
				.findViewById(R.id.loading_progress);
		progressBar.getLayoutParams().height = height;
		progressBar.getLayoutParams().width = width;
		progressBar.invalidate();
		Dialog dialog = new Dialog(mContext, R.style.progressDialog);
		dialog.setCancelable(isClose);
		dialog.setContentView(inflater, new LinearLayout.LayoutParams(width*4, height*3));

		return dialog;
	}
}
  