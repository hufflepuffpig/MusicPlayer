package com.example.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class AlwaysMarqueeView extends TextView
{

	public AlwaysMarqueeView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setSingleLine();
		setHorizontallyScrolling(true);
		setEllipsize(TextUtils.TruncateAt.MARQUEE);
		setMarqueeRepeatLimit(-1);
		
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	public AlwaysMarqueeView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		setSingleLine();
		setHorizontallyScrolling(true);
		setEllipsize(TextUtils.TruncateAt.MARQUEE);
		setMarqueeRepeatLimit(-1);
		
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	public AlwaysMarqueeView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setSingleLine();
		setHorizontallyScrolling(true);
		setEllipsize(TextUtils.TruncateAt.MARQUEE);
		setMarqueeRepeatLimit(-1);
		
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused()
	{
		// TODO Auto-generated method stub
		return true;
	}


}
