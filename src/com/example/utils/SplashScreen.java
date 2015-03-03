package com.example.utils;


import com.example.musicplayer.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/*
 * 向导页类
 */
public class SplashScreen
{
	public static final int FADE_OUT=0;
	public static final int SLIDE_LEFT=1;
	public static final int SLIDE_UP=2;
	
	private Activity activity;
	private Dialog dialog;
	
	public SplashScreen(Activity activity)
	{
		this.activity=activity;
	}
	public void show(final int ID_img,final int exit_way)
	{
		Runnable runnable=new Runnable()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				LinearLayout root=new LinearLayout(activity);
				root.setOrientation(LinearLayout.VERTICAL);
				root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT));
				root.setBackgroundResource(ID_img);
				
				dialog=new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
				if((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)==WindowManager.LayoutParams.FLAG_FULLSCREEN)
				{
					dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
				}
				switch(exit_way)
				{
				case FADE_OUT:
					dialog.getWindow().setWindowAnimations(R.style.fade_out);break;
				case SLIDE_LEFT:
					dialog.getWindow().setWindowAnimations(R.style.slide_left);break;
				case SLIDE_UP:
					dialog.getWindow().setWindowAnimations(R.style.slide_up);break;
					default:
						break;
				}
				
				dialog.setContentView(root);
				dialog.setCancelable(false);//取消BACK键的响应
				dialog.show();
			}
		};
		activity.runOnUiThread(runnable);
	}
	
	public void remove()
	{
		if(dialog!=null && dialog.isShowing())
		{
			dialog.dismiss();
			dialog=null;
		}
	}
	
}