package com.example.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.text.TextUtils;

public class StringHelper
{
	public static String getPingYin(String inputString)
	{
		if(TextUtils.isEmpty(inputString))
		{
			return "";
		}
		HanyuPinyinOutputFormat outputFormat=new HanyuPinyinOutputFormat();
		outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		
		char[] input=inputString.trim().toCharArray();
		String output="";
		
		for(int i=0;i<input.length;i++)
		{
			if(java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+"))
			{
				try
				{
					String temp[]=PinyinHelper.toHanyuPinyinStringArray(input[i], outputFormat);
					if(temp==null || TextUtils.isEmpty(temp[0]))
					{
						continue;
					}
					output+=temp[0].replaceFirst(temp[0].substring(0, 1), temp[0].substring(0, 1).toUpperCase());
				} catch (BadHanyuPinyinOutputFormatCombination e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				output+=java.lang.Character.toString(input[i]);
			}
		}
		return output;
	}
}
