package com.bell.belltools.utils;  

import java.io.DataOutputStream;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/*  
 * @info   
 * @author GuoJiang 
 * @time 2015年10月30日 下午4:45:11  
 * @version    
 */
public class RootPermission {
	
	public static boolean getRoot(Context mContext, String file) {
//		Log.d("pkgCodePath", "pkgCodePath=" + file);  
        Process process = null;  
        DataOutputStream os = null;  
        try {  
            String cmd = "chmod 777 " + file;  
            process = Runtime.getRuntime().exec("su"); //切换到root帐号  
            os = new DataOutputStream(process.getOutputStream());  
            os.writeBytes(cmd + "\n");  
            os.writeBytes("exit\n");  
            os.flush();  
            process.waitFor();  
        } catch (Exception e) {  
            Toast.makeText(mContext, "root error!" + file, Toast.LENGTH_SHORT).show();  
            e.printStackTrace();  
            return false;  
        } finally {  
            try {  
                if (os != null) {  
                    os.close();  
                }  
                process.destroy();  
  
  
            } catch (Exception e) {  
            }  
        }  
        Toast.makeText(mContext, "root success!" + file, Toast.LENGTH_SHORT).show();  
        return true;  
	}
}
  