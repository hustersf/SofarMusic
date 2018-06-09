package com.sf.sofarmusic.util;

import android.content.Context;
import android.util.Base64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static String License = "";

	// 判断名字是否合法
	public static boolean isLegal(String s) {
		String regEx = "[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(s);
		boolean rs = mat.find();
		return rs;
	}

	// 判断是否是纯数字
	public static boolean isNumber(String s) {
		String regEx = "^[0-9\\s]*$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(s);
		boolean rs = mat.find();
		return rs;
	}

	// 判断是否是字母或者字母加数字
	public static boolean isNumAndLetter(String s) {
		String regEx = "^(?!^[0-9]+$)[a-zA-Z0-9]*$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(s);
		boolean rs = mat.find();
		return rs;
	}

	// 判断是否是身份证
	public static boolean isId(String s) {
		String regEx = "^(\\d{17})([0-9]|X|x)$|^(\\d{15})$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(s);
		boolean rs = mat.find();
		return rs;
	}

	// 判断是否是手机号
	public static boolean isPhone(String s) {
		String regEx = "^1([1-9][0-9])\\d{8}$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(s);
		boolean rs = mat.find();
		return rs;
	}

	// 判断是否是特殊符号
	public static boolean isSpecialSign(String s) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(s);
		boolean rs = mat.find();
		return rs;
	}

	// 得到要加密的md5字符串
	public static String getMapString(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (value == null) {
				value = "";
			}
			sb.append(key + "=" + value + "&");
		}
		String s = sb.toString().substring(0, sb.toString().length() - 1)
				+ "||856987";
		return s;
	}

	public static String getNewMapString(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (value == null) {
				value = "";
			}
			if (!"".equals(value))
				sb.append(key + "=" + value + "&");
		}
		String s = sb.toString().substring(0, sb.toString().length() - 1)
				+ "&key=917EA87E4375479F9D9067EAF17A4528";
		return s;
	}

	public static String map2JsonBase64(Map<String, String> map) {
		JSONObject json = new JSONObject(map);
		String s = json.toString();
		String base = "";
		try {
			String base64 = Base64.encodeToString(s.getBytes("utf-8"),
					Base64.DEFAULT);
			base = base64.replace("+", "#");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base;
	}

	// 获取随机数字
	public static String getRandInt(int length) {
		String s = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(s.length());
			sb.append(s.charAt(index));
		}
		return sb.toString();
	}

	// 读取assets下的文件
	public static String getAsset(Context context, String fileName) {
		String result = "";

		try {
			InputStreamReader reader = new InputStreamReader(context
					.getResources().getAssets().open(fileName));
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line = "";

			while ((line = bufferedReader.readLine()) != null) {
				result += line + "\n";
			}
			bufferedReader.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 求两个数组的差集
	public static String[] minus(String[] arr1, String[] arr2) {
		LinkedList<String> list = new LinkedList<String>();
		LinkedList<String> history = new LinkedList<String>();
		String[] longerArr = arr1;
		String[] shorterArr = arr2;
		// 找出较长的数组来减较短的数组
		if (arr1.length > arr2.length) {
			longerArr = arr2;
			shorterArr = arr1;
		}
		for (String str : longerArr) {
			if (!list.contains(str)) {
				list.add(str);
			}
		}
		for (String str : shorterArr) {
			if (list.contains(str)) {
				history.add(str);
				list.remove(str);
			} else {
				if (!history.contains(str)) {
					list.add(str);
				}
			}
		}

		String[] result = {};
		return list.toArray(result);
	}

	public  static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}



}
