package cn.xyz.test.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.lang3.time.DateUtils;

public class ToolsDate {
	public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	public final static String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	public static String[] patterns = {
			"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm", 
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss.SSS", "yyyy/MM/dd HH:mm",
			"yyyy-MMM-dd", "dd-MMM-yyyy"};
	
	public static Date getDate(String date) throws Exception {
		if (Tools.isEmpty(date)) {
			return null;
		}
		return DateUtils.parseDate(date.trim(), Locale.ENGLISH, patterns);
	}
	public static String getString() {
		return getString(DEFAULT_DATE_TIME_PATTERN, new Date());
	}
	public static String getString(String pattern) {
		return getString(pattern, new Date());
	}
	public static String getString(String pattern, String date) throws Exception {
		return getString(pattern, getDate(date));
	}
	public static String getString(String pattern, Date date) {
		if (date == null) {
			return null;
		}
		DateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
		return format.format(date);
		//return DateFormatUtils.format(date, pattern, Locale.ENGLISH);
	}
	
	public static String getDatePart(String part) {
		return getDatePart(part, new Date());
	}
	public static String getDatePart(String part,String date) throws Exception {
		return getDatePart(part, getDate(date));
	}
	public static String getDatePart(String part,Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		switch (part) {
			case "y":
				return Integer.toString(cal.get(Calendar.YEAR));
			case "M":
				return Integer.toString(cal.get(Calendar.MONTH) + 1);
			case "d":
				return Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
			case "H":
				return Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
			case "h":
				return Integer.toString(cal.get(Calendar.HOUR));
			case "m":
				return Integer.toString(cal.get(Calendar.MINUTE));
			case "s":
				return Integer.toString(cal.get(Calendar.SECOND));
			case "S":
				return Integer.toString(cal.get(Calendar.MILLISECOND));
			case "season":
				return Integer.toString(cal.get(Calendar.MONTH) / 3 + 1);
			case "w":
				Integer week = cal.get(Calendar.WEEK_OF_YEAR);
				if(week == 1)
				{
					if(cal.get(Calendar.MONTH) > 1)
					{
						cal.add(Calendar.DATE, -7);
						week = cal.get(Calendar.WEEK_OF_YEAR) + 1;
					}
				}
				return Integer.toString(week);
			default:
				break;
		}
		return null;
	}
	public static String getDatePart(Integer part) {
		return getDatePart(part, new Date());
	}
	public static String getDatePart(Integer part,String date) throws Exception {
		return getDatePart(part, getDate(date));
	}
	public static String getDatePart(Integer part,Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(part == 44) {
			return Integer.toString(cal.get(Calendar.MONTH) / 3 + 1);
		}
		Integer result = cal.get(part);
		if(part == Calendar.MONTH) {
			result += 1;
		}
		if(part == Calendar.WEEK_OF_YEAR) {
			if(result == 1)
			{
				if(cal.get(Calendar.MONTH) > 1)
				{
					cal.add(Calendar.DATE, -7);
					result = cal.get(Calendar.WEEK_OF_YEAR) + 1;
				}
			}
		}
		return result.toString();
	}
	
	public static Date add(String field, int amount, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if("y".equals(field)) {
			cal.add(Calendar.YEAR, amount);
		}
		if("M".equals(field)) {
			cal.add(Calendar.MONTH, amount);
		}
		if("d".equals(field)) {
			cal.add(Calendar.DATE, amount);
		}
		/*switch (field) {
			case "y":
				cal.add(Calendar.YEAR, amount);//没有break
			case "M":
				cal.add(Calendar.MONTH, amount);
			case "d":
				cal.add(Calendar.DATE, amount);
			case "H":
				cal.add(Calendar.HOUR_OF_DAY, amount);
			case "h":
				cal.add(Calendar.HOUR, amount);
			case "m":
				cal.add(Calendar.MINUTE, amount);
			case "s":
				cal.add(Calendar.SECOND, amount);
			case "S":
				cal.add(Calendar.MILLISECOND, amount);
			case "w":
				cal.add(Calendar.WEEK_OF_YEAR, amount);
			default:
				break;
		}*/
		return cal.getTime();
	}
	public static String addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		date = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		return format.format(date);
	}
	public static String getMonthEn(Integer i) {
		return months[i-1];
	}
	

	public static String createNo(String split) throws Exception {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		if(Tools.isEmpty(split)) {
			return sdf.format(new Date()) + (new Random().nextInt(900) + 100);
		}
		return sdf.format(new Date()) + split + (new Random().nextInt(900) + 100);
	}
	
	public static void main(String[] args) {
		try {
			/*for (int i = 0; i < 17; i++) {
				System.out.println(getDatePart(i, new Date()));
			}*/
			System.out.println(new Date(1541088000000l));
			System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", new Date(1541088000000l)));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", addDays(new Date(),4)));
			System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", add("M",-3, getDate("2019-05-31"))));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", add("d",4, new Date())));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", add("H",50, new Date())));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", add("h",50, new Date())));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", add("m",50, new Date())));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", add("s",50, new Date())));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", add("S",50, new Date())));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", add("w",50, new Date())));
			
			//System.out.println(getDatePart("h"));
			/*Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, 9);
			System.out.println(String.valueOf(5));
			System.out.println(cal.getTime());
			System.out.println(DateUtils.addDays(new Date(), 5));
			System.out.println(getDate("2019-APR-25"));
			System.out.println(getString("yyyy年MMM月dd日",new Date()));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*System.out.println(getString("yy-MMM-dd").toUpperCase());
		try {
			System.out.println(getDatePart("周","2000-01-01"));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		/*for (int i = 2000; i < 4000; i++) {
			String a = i + "-01-01";
			String b = i + "-01-02";
			if("1".equals(getDatePart("周",a))&&"2".equals(getDatePart("周",b))) {
				System.out.println(i);
			}
		}*/
		//Calendar cal = Calendar.getInstance();
		//cal.setTime(getDate("2028-12-30"));
		//System.out.println(cal.get(Calendar.WEEK_OF_YEAR));
		//System.out.println(getDatePart("周","2028-12-31"));
	}
}
