package com.example.musicplayer;

import java.util.ArrayList;
import java.util.List;

import com.example.fragment.LeftFragment;
import com.example.fragment.MenuBackgroundFragment;
import com.example.fragment.RightFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MenuActivity extends FragmentActivity
{

	private ViewPager viewPager;
	private BgViewPagerAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.menuactivity_main);
		
		mAdapter=new BgViewPagerAdapter(getSupportFragmentManager());
		viewPager=(ViewPager) findViewById(R.id.menu_bg_viewPager);
		viewPager.setAdapter(mAdapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setCurrentItem(1);
	}

	private class BgViewPagerAdapter extends FragmentPagerAdapter
	{

		List<Fragment> fragments=new ArrayList<>();
		public BgViewPagerAdapter(FragmentManager fm)
		{
			super(fm);
			// TODO Auto-generated constructor stub
			fragments.add(new LeftFragment());
			fragments.add(new MenuBackgroundFragment());
			fragments.add(new RightFragment());
		}

		@Override
		public Fragment getItem(int arg0)
		{
			// TODO Auto-generated method stub
			return fragments.get(arg0);
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return fragments.size();
		}
		
	}

	private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
	{

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			// TODO Auto-generated method stub
			if(arg0==0 && (viewPager.getCurrentItem()==0 || viewPager.getCurrentItem()==2))
			{
				MenuActivity.this.finish();
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0)
		{
			// TODO Auto-generated method stub
			
		}
		
	}
}
