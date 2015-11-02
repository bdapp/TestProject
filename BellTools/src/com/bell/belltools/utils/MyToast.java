package com.bell.belltools.utils;  

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.bell.belltools.R;
import com.bell.belltools.R.anim;
import com.bell.belltools.R.drawable;
import com.bell.belltools.R.id;
import com.bell.belltools.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*  
 * @info   
 * @author GuoJiang 
 * @time 2015年10月28日 下午3:09:26  
 * @version    
 */
public class MyToast {
	static Toast toast = null;
    static Handler handler = new Handler();
    static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            toast.cancel();
        }
    };

    static Runnable rl = new Runnable() {
        @Override
        public void run() {
            toastClass.cancel();
        }
    };

   
    static ToastClass toastClass = null ;
    
    public static void showToast(Context mContext, String msg){
    	showToast3(mContext, msg, 2000);
    }
    
    public static void showToast3(Context mContext, String msg, int time){
        handler.removeCallbacks(rl);
        if (toastClass != null)
            toastClass.cancel();
        toastClass = new ToastClass(mContext, msg);
        toastClass.show();
        handler.postDelayed(rl, time);

    }
    
    
   public static class ToastClass extends Toast {

        private static Object mTNObject;
        private static Method showMethod;
        private static Toast toast;

        private static WindowManager wdm;
        private double time;
        private static View mView = null;
        private WindowManager.LayoutParams params;


        public ToastClass(Context context) {
            super(context);
        }

        @Override
        public void show() {
//            super.show();
            try {
                if (showMethod != null)
                    cancel();

                // 其作用主要是使用handler去post调用handlerShow与handlerHide
                showMethod = mTNObject.getClass().getDeclaredMethod("show", null);

                // 直接调用显示就可以了
                showMethod.invoke(mTNObject, null);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void cancel() {
//            super.cancel();
            try {

                // 其作用主要是使用handler去post调用handlerShow与handlerHide
                showMethod = mTNObject.getClass().getDeclaredMethod("hide", null);
                // 直接调用显示就可以了
                showMethod.invoke(mTNObject, null);
                showMethod = null;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public ToastClass(Context mContext, CharSequence msg) {
            super(mContext);
            try {
                // 创建一个Toast对象
                toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                View view = inflater.inflate(R.layout.toast_layout, null);
                ImageView msgImg = (ImageView) view.findViewById(R.id.id_toast_img);
                msgImg.setImageResource(R.drawable.ic_launcher);
                TextView msgText = (TextView) view.findViewById(R.id.id_toast_text);
                msgText.setText(msg);
                view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_toast));

                toast.setView(view);
                // 获取toast的view
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                mView = toast.getView();


                // mTN在Toast的构造方法中就创建了
                Field mTN = toast.getClass().getDeclaredField("mTN");
                mTN.setAccessible(true);
                mTNObject = mTN.get(toast);

                // 只有在3.0以上的TN中才有这个成员
                if (Build.VERSION.SDK_INT > 11) {
                    Field viewField = mTNObject.getClass().getDeclaredField("mNextView");
                    viewField.setAccessible(true);
                    viewField.set(mTNObject, mView);
                }
                // TN类两个主要的方法，show与hide
                // 其作用主要是使用handler去post调用handlerShow与handlerHide
//                Method showMethod = mTNObject.getClass().getDeclaredMethod("show", null);
//                final Method hideMethod = mTNObject.getClass().getDeclaredMethod("hide", null);

                // 直接调用显示就可以了
//                showMethod.invoke(mTNObject, null);

                // 隐藏
                //hideMethod.invoke(mTNObject, null);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
  