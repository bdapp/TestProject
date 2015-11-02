package com.bell.belltools.adapter;  

import java.util.ArrayList;

import com.bell.belltools.R;
import com.bell.belltools.R.drawable;
import com.bell.belltools.R.id;
import com.bell.belltools.R.layout;
import com.bell.belltools.bean.FileBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*  
 * @info   
 * @author GuoJiang 
 * @time 2015年10月27日 下午5:18:52  
 * @version    
 */
public class FileAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<FileBean> fileList;

	public FileAdapter(Context mContext, ArrayList<FileBean> fileList) {
		super();
		this.mContext = mContext;
		this.fileList = fileList;
	}
	
	
	@Override
	public int getCount() {
		
		return fileList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return fileList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView==null) {
			holder = new Holder();
			convertView = View.inflate(mContext, R.layout.item_file_list, null);
			holder.fileImg = (ImageView) convertView.findViewById(R.id.item_img);
			holder.fileName = (TextView) convertView.findViewById(R.id.item_title);
			holder.fileSize = (TextView) convertView.findViewById(R.id.item_size);
			holder.fileTime = (TextView) convertView.findViewById(R.id.item_time);
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		//赋值
		if (fileList.get(position).isFile()) {
			holder.fileImg.setImageResource(R.drawable.ico_file);
		} else {
			holder.fileImg.setImageResource(R.drawable.ico_folder);
		}
		
		holder.fileName.setText(fileList.get(position).getFileName());
		holder.fileSize.setText(fileList.get(position).getFileSize());
		holder.fileTime.setText(fileList.get(position).getFileTime());
		
		return convertView;
	}
	
	
	private class Holder {
		ImageView fileImg;
		TextView fileName;
		TextView fileSize;
		TextView fileTime;
		
	}

	

}
  