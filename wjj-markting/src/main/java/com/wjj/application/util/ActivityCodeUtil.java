package com.wjj.application.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ActivityCodeUtil {
	public static String createCode() {
		String sources = "0123456789"; // 加上一些字母，就可以生成pc站的验证码了
		Random rand = new Random();
		StringBuffer flag = new StringBuffer();
		flag.append("YFQ");
		Date d = new Date();
		System.out.println(d);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateNowStr = sdf.format(d);
		flag.append(dateNowStr);
		for (int j = 0; j < 6; j++) 
		{
			int i=rand.nextInt(9);
			flag.append(sources.charAt(i) + "");
		}
		return flag.toString();
	}
	public static void main(String[] args) {
		ActivityCodeUtil.createCode();
	}
}
