package cn.xyz.common.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class ToolsDate {
	//加入数据库，启动加入缓存（设置能修改与不能修改）
	public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	public final static String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	//加入数据库，启动加入缓存
	//数据字典添加格式时要把"/"换成"-"(或提示分隔符用"-")
	public static String[] patterns = {
			"yyyy-MM-dd", "yyyy-MMM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm", 
			"dd-MMM-yyyy"};
	public static Date getDate(String date) throws Exception {
		if (Tools.isEmpty(date)) {
			return null;
		}
		String _date = date.replaceAll("/", "-");
		String pattern ="";
		for (int i = 0; i < _date.length(); i++) {
			String s = _date.substring(i,i+1);
			if(Pattern.matches("[0-9a-zA-Z]", s)) {
				pattern += "[0-9a-zA-Z]";
			}else {
				pattern += s;
			}
		}
		for (int i = 0; i < patterns.length; i++) {
			if(Pattern.matches(pattern, patterns[i])) {
				pattern = patterns[i];
				break;
			}
		}
		return getDate(_date, pattern);
		//return DateUtils.parseDate(date.trim(), Locale.ENGLISH, patterns);
	}
	public static Date getDate(String date, String pattern) throws Exception {
		DateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
		return format.parse(date);
	}
	public static String getString() {
		return getString(new Date(), DEFAULT_DATE_PATTERN);
	}
	public static String getString(Date date) {
		return getString(date, DEFAULT_DATE_PATTERN);
	}
	public static String getLongString() {
		return getString(new Date(), DEFAULT_DATE_TIME_PATTERN);
	}
	public static String getLongString(Date date) {
		return getString(date, DEFAULT_DATE_TIME_PATTERN);
	}
	public static String getString(String pattern) {
		return getString(new Date(), pattern);
	}
	public static String getString(String date, String pattern) throws Exception {
		return getString(getDate(date), pattern);
	}
	public static String getString(Date date, String pattern) {
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
		if(part == 44) {//季度
			return Integer.toString(cal.get(Calendar.MONTH) / 3 + 1);
		}
		Integer result = cal.get(part);
		if(part == Calendar.MONTH) {//月
			result += 1;
		}
		if(part == Calendar.WEEK_OF_YEAR) {//周
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
		Date date2 = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		return format.format(date2);
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
	//前后相差几月
	public static int diffMonth(String from, String to) throws Exception {
		Calendar datefrom = Calendar.getInstance();
		Calendar dateto = Calendar.getInstance();
		datefrom.setTime(getDate(from));
		dateto.setTime(getDate(to));
		int y = dateto.get(Calendar.YEAR) - datefrom.get(Calendar.YEAR);
		return dateto.get(Calendar.MONTH) - datefrom.get(Calendar.MONTH) + y * 12;	
	}
	public static int diffDay(String from, String to) throws Exception {
		return (int) ((getDate(from).getTime() - getDate(to).getTime()) / (1000*3600*24));
	}
	public static String firstDay() {
		Calendar c = Calendar.getInstance();    
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        return getString(c.getTime());
	}
	public static String lastDay() {
		Calendar c = Calendar.getInstance();    
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getString(c.getTime());
	}
	public static String preFirstDay() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        return getString(c.getTime());
	}
	public static String preLastDay() {
		Calendar c = Calendar.getInstance();    
		c.set(Calendar.DAY_OF_MONTH,0);//设置为0,为上月最后一天 
        return getString(c.getTime());
	}
	//比较大小
	public static boolean compare(Date date1, Date date2) {
		
		return false;
	}
	//返回大的时间
	public static Date max(String date1, String date2) throws Exception {
		return max(getDate(date1),getDate(date2));
	}
	public static Date max(Date date1, Date date2) throws Exception {
		if(Tools.isEmpty(date1)) return date2;
		if(Tools.isEmpty(date2)) return date1;
		if(date1.before(date2)) {
			return date2;
		}
		return date1;
	}
	public static void main(String[] args) {
		try {
		
//			System.out.println(firstDay());
//			System.out.println(lastDay());
//			System.out.println(preFirstDay());
//			System.out.println(preLastDay());
			/*for (int i = 0; i < 17; i++) {
				System.out.println(getDatePart(i, new Date()));
			}*/
			//System.out.println(getLongString(getDate("2020/Nov/16")));
			//System.out.println(new Date(1541088000000l));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", new Date(1541088000000l)));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", addDays(new Date(),4)));
			//System.out.println(getString("yyyy-MM-dd HH:mm:ss.SSS", add("M",-3, getDate("2019-05-31"))));
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
