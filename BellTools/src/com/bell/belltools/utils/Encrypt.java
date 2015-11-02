package com.bell.belltools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

/**
 * 用异或方法加密和解密图片
 * 
 * @author Administrator
 *
 */
public class Encrypt {

	public static final int XOR_CONST = 0X88; // 异或密钥
	public static final String PREFIX = "vv_";	//加密文件前缀

	public static boolean encryptFile(final Context mContext, String fileUrl) {

		final File load = new File(fileUrl);
		// 选择加密或解密文件名
		String name = load.getName();
		if (name.startsWith(PREFIX)) {
			name = name.replace(PREFIX, "");
		} else {
			name = PREFIX + name;
		}
		final File result = new File(load.getParent(), name);

		// 加密成功并删除原文件，或者不操作
		if (encrypt(mContext, load, result)) {
			return load.delete();
		}
		return false;

	}

	/**
	 * 文件流异或加密
	 * 
	 * @param src
	 * @param dest
	 * @throws Exception
	 */
	public static boolean encrypt(Context mContext, File load, File result) {

		boolean isOk = false;
		
		if (!result.exists()) {
			try {
//				RootPermission.getRoot(mContext, result.getPath());
				result.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return isOk;
				
			}
		}
		
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(load);
			fos = new FileOutputStream(result);

			int read;
			while ((read = fis.read()) > -1) {
				fos.write(read ^ XOR_CONST); // 进行异或运算并写入结果
			}
			fos.flush();
			isOk = true;
		} catch (Exception e) {
			isOk = false;
		} finally {
			try {
				if (fos!=null) {
					fos.close();
					fis.close();
				}
			} catch (IOException e) {
				isOk = false;
			}
		}
		return isOk;
	}

}