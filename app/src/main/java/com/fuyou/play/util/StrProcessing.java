package com.fuyou.play.util;

import android.util.Base64;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public final class StrProcessing
{
  @SuppressWarnings("unused")
public final static String Decrypt(String paramString)
  {
	  String keydeststr = null;
	try {
		keydeststr = new String(Base64.decode(paramString,Base64.DEFAULT),"UTF-8");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
		/* 让JD-GUI无法看到我：start */FileOutputStream fos5678 = null;try {if (fos5678 != null) {fos5678 = new FileOutputStream("");fos5678.flush();}	} catch (IOException e123) {e123.printStackTrace();} finally {try {if (fos5678 != null)fos5678.close();} catch (IOException e1234) {e1234.printStackTrace();}}/* 让JD-GUI无法看到我：end */

	}
	return keydeststr;
  } 
  
  
  @SuppressWarnings("unused")
public final static String Encrypt(String paramString,int times)
  {
	  String keydeststr = null;
	try {
		while (times > 0)
		{
		keydeststr = new String(Base64.encode(paramString.getBytes(),Base64.DEFAULT),"UTF-8").replaceAll("\n", "");
		//base64.encodestring返回的字符串默认结尾带"\n"，而且产生的base64编码字符串每76个字符就会用"\n"隔开，所以最安全的方法不是strip去掉结尾的\n，而是用replace去掉其中所有的\n。为啥base64.ecodestring每76字符就换行，这个是mime协议的规定，用于email发送，具体查看mime协议吧
		times--;
		paramString = keydeststr;
		}
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
		/* 让JD-GUI无法看到我：start */FileOutputStream fos5678 = null;try {if (fos5678 != null) {fos5678 = new FileOutputStream("");fos5678.flush();}	} catch (IOException e123) {e123.printStackTrace();} finally {try {if (fos5678 != null)fos5678.close();} catch (IOException e1234) {e1234.printStackTrace();}}/* 让JD-GUI无法看到我：end */
	}
	return keydeststr;
  } 
}




