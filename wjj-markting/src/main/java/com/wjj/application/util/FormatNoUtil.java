/**  
* <p>Title: FormatNoUtil.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年8月29日  
* @version 1.0  
*/  
package com.wjj.application.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**  
* <p>Title: FormatNoUtil.java</p>  
* <p>Description: </p>  
* <p>Copyright: 上海卫健家健康技术有限公司 (c) 2018</p>  
* <p>Company: www.waygiga.com</p>  
* @author bob
* @date 2018年8月29日  
* @version 1.0  
*/
public class FormatNoUtil {
	
	public static String formatNextNo(String no, Integer length){
		if(no == null){
			no = "1";
		}else{
			no = no.substring(no.length() - length, no.length());
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < length; i++){
			sb.append("0");
		}
		DecimalFormat df = new DecimalFormat(sb.toString());
		Integer parseInt = Integer.parseInt(no) + 1;
		return df.format(parseInt);
	}
	
	
	public static List<String> formatSontNo(String cno, Integer length, Integer size) {
		cno = cno.substring(cno.length() - length, cno.length());
		Integer no = Integer.parseInt(cno);
		
		String sonNo = String.valueOf(no);
		Integer length2 = sonNo.length();
		if(length2 == length){
			no = no + 1;
		}else{
			Integer length3 = length - length2;
			StringBuffer sb = new StringBuffer();
			sb.append(sonNo);
			for(int i = 0; i < length3; i++){
				sb.append("0");
			}
			no = Integer.parseInt(sb.toString()) + 1;
		}
		List<String> list = new ArrayList<String>();
		
		for(int i = 0; i < size; i++){
			list.add(String.valueOf(no++));
		}
		return list;
	}
	
	public static void main(String[] args) {
		System.out.println(formatSontNo("AC11111111", 8, 100));
	}

}
