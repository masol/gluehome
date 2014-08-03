package org.spolo.glue;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class StrRegex {

	// 定义正则变量
	public static String errorStr = ".+ (ERROR).+";
	public static String failedStr = ".+(failed).+";
	public static String count1Str = "Length";
	public static String lengthStr = "Length:\\s*(\\d+).+";
	public static String savingStr = "Saving\\s*to:\\s*`(.+)'";
	public static String downloadingStr = "\\s*(\\d+)K\\s*[.\\s]*(\\d+)%\\s*([\\d\\.\\w]*)\\s*(.+)";
	public static String newerStr = "newer";
	public static int process, len;
	public static String time,speed;
	public static boolean exit = true;

	// 正则方法
	
	// 剩余时间和下载速度
	public static void downloadingInfo(String strPattern, String line) {
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(line);

		if (matcher.find()) {
			speed=new String(matcher.group(3));
			time=new String(matcher.group(4));
			System.out.print(time + " ");
			System.out.println();
		}
	}

	// 文件大小
	public static void fileSize(String strPattern, String line) {
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(line);

		if (matcher.find()) {
			String str = new String(matcher.group(1));
			len = Integer.parseInt(str);
			System.out.print(len + " ");
			System.out.println();
		}
	}

	// 文件个数
	public static boolean isDownloadNewFile(String line) {
		Pattern pattern1 = Pattern.compile(count1Str);
		Matcher matcher1 = pattern1.matcher(line);
		if (matcher1.find()) {
			return true;
		}
		return false;
	}

	// 下载进度
	public static void downloadingProcess(String strPattern, String line) {
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(line);
		int x = 0;
		if (matcher.find()) {
			String str = new String(matcher.group(2));
			x = Integer.parseInt(str);
			process = x;
		}
	}

	// 错误提示
	public static void errorMessage(String strPattern, String line) {
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(line);

		if (matcher.find()) {
			String str = new String(matcher.group(1));
			if (str.equals("ERROR")) {
				System.out.println("\u4E0B\u8F7D\u6587\u4EF6\u4E0D\u5B58\u5728");
				JOptionPane.showMessageDialog(null, "\u4E0B\u8F7D\u6587\u4EF6\u4E0D\u5B58\u5728");
			} else {
				System.out.println("\u7F51\u7EDC\u8FDE\u63A5\u4E0D\u53EF\u7528");
				JOptionPane.showMessageDialog(null, "\u7F51\u7EDC\u8FDE\u63A5\u4E0D\u53EF\u7528");
			}
		}
	}

	// newer
	public static void existingFile(String strPattern, String line) {
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			process = 100;
			System.out.println(process);
		}
	}

	// 接收logoutput line 匹配下载信息
	public static void regex(String s) throws IOException {

		downloadingInfo(downloadingStr, s);
		fileSize(lengthStr, s);
		downloadingProcess(downloadingStr, s);
//		errorMessage(errorStr, s);
//		errorMessage(failedStr, s);
	}

	public static void exit() {
		StrRegex.exit = false;
	}

}
