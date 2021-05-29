package cn.xyz.common.tools;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.xyz.common.exception.CustomException;

public class ToolsLegal {
	 /**
     * 支持转换的最小农历年份
     */
    public static final int MIN_YEAR = 1900;

    /**
     * 支持转换的最大农历年份
     */
    public static final int MAX_YEAR = 2099;

    /**
     * 公历每月前的天数
     */
    private static final int[] DAYS_BEFORE_MONTH = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};
    /**
     * 将农历日期转换为公历日期
     * @param year    农历年份
     * @param month   农历月
     * @param monthDay   农历日
     * @param isLeapMonth   该月是否是闰月(该参数可以根据本类中leapMonth()方法，先判断一下要查询的年份是否有闰月，并且闰的几月)
     * @return 返回农历日期对应的公历日期，year0, month1, day2.
     */
    /**
     * 用来表示1900年到2099年间农历年份的相关信息，共24位bit的16进制表示，其中：
     * 1. 前4位表示该年闰哪个月；
     * 2. 5-17位表示农历年份13个月的大小月分布，0表示小，1表示大；
     * 3. 最后7位表示农历年首（正月初一）对应的公历日期。
     * 以2014年的数据0x955ABF为例说明：
     * 1001 0101 0101 1010 1011 1111
     *  闰九月   农历正月初一对应公历1月31号
     */
    private static final int[] LUNAR_INFO = {
            /*1900*/
            0x84B6BF,
            /*1901-1910*/
            0x04AE53, 0x0A5748, 0x5526BD, 0x0D2650, 0x0D9544, 0x46AAB9, 0x056A4D, 0x09AD42, 0x24AEB6, 0x04AE4A,
            /*1911-1920*/
            0x6A4DBE, 0x0A4D52, 0x0D2546, 0x5D52BA, 0x0B544E, 0x0D6A43, 0x296D37, 0x095B4B, 0x749BC1, 0x049754,
            /*1921-1930*/
            0x0A4B48, 0x5B25BC, 0x06A550, 0x06D445, 0x4ADAB8, 0x02B64D, 0x095742, 0x2497B7, 0x04974A, 0x664B3E,
            /*1931-1940*/
            0x0D4A51, 0x0EA546, 0x56D4BA, 0x05AD4E, 0x02B644, 0x393738, 0x092E4B, 0x7C96BF, 0x0C9553, 0x0D4A48,
            /*1941-1950*/
            0x6DA53B, 0x0B554F, 0x056A45, 0x4AADB9, 0x025D4D, 0x092D42, 0x2C95B6, 0x0A954A, 0x7B4ABD, 0x06CA51,
            /*1951-1960*/
            0x0B5546, 0x555ABB, 0x04DA4E, 0x0A5B43, 0x352BB8, 0x052B4C, 0x8A953F, 0x0E9552, 0x06AA48, 0x6AD53C,
            /*1961-1970*/
            0x0AB54F, 0x04B645, 0x4A5739, 0x0A574D, 0x052642, 0x3E9335, 0x0D9549, 0x75AABE, 0x056A51, 0x096D46,
            /*1971-1980*/
            0x54AEBB, 0x04AD4F, 0x0A4D43, 0x4D26B7, 0x0D254B, 0x8D52BF, 0x0B5452, 0x0B6A47, 0x696D3C, 0x095B50,
            /*1981-1990*/
            0x049B45, 0x4A4BB9, 0x0A4B4D, 0xAB25C2, 0x06A554, 0x06D449, 0x6ADA3D, 0x0AB651, 0x095746, 0x5497BB,
            /*1991-2000*/
            0x04974F, 0x064B44, 0x36A537, 0x0EA54A, 0x86B2BF, 0x05AC53, 0x0AB647, 0x5936BC, 0x092E50, 0x0C9645,
            /*2001-2010*/
            0x4D4AB8, 0x0D4A4C, 0x0DA541, 0x25AAB6, 0x056A49, 0x7AADBD, 0x025D52, 0x092D47, 0x5C95BA, 0x0A954E,
            /*2011-2020*/
            0x0B4A43, 0x4B5537, 0x0AD54A, 0x955ABF, 0x04BA53, 0x0A5B48, 0x652BBC, 0x052B50, 0x0A9345, 0x474AB9,
            /*2021-2030*/
            0x06AA4C, 0x0AD541, 0x24DAB6, 0x04B64A, 0x6a573D, 0x0A4E51, 0x0D2646, 0x5E933A, 0x0D534D, 0x05AA43,
            /*2031-2040*/
            0x36B537, 0x096D4B, 0xB4AEBF, 0x04AD53, 0x0A4D48, 0x6D25BC, 0x0D254F, 0x0D5244, 0x5DAA38, 0x0B5A4C,
            /*2041-2050*/
            0x056D41, 0x24ADB6, 0x049B4A, 0x7A4BBE, 0x0A4B51, 0x0AA546, 0x5B52BA, 0x06D24E, 0x0ADA42, 0x355B37,
            /*2051-2060*/
            0x09374B, 0x8497C1, 0x049753, 0x064B48, 0x66A53C, 0x0EA54F, 0x06AA44, 0x4AB638, 0x0AAE4C, 0x092E42,
            /*2061-2070*/
            0x3C9735, 0x0C9649, 0x7D4ABD, 0x0D4A51, 0x0DA545, 0x55AABA, 0x056A4E, 0x0A6D43, 0x452EB7, 0x052D4B,
            /*2071-2080*/
            0x8A95BF, 0x0A9553, 0x0B4A47, 0x6B553B, 0x0AD54F, 0x055A45, 0x4A5D38, 0x0A5B4C, 0x052B42, 0x3A93B6,
            /*2081-2090*/
            0x069349, 0x7729BD, 0x06AA51, 0x0AD546, 0x54DABA, 0x04B64E, 0x0A5743, 0x452738, 0x0D264A, 0x8E933E,
            /*2091-2099*/
            0x0D5252, 0x0DAA47, 0x66B53B, 0x056D4F, 0x04AE45, 0x4A4EB9, 0x0A4D4C, 0x0D1541, 0x2D92B5
    };
    public static final int[] lunarToSolar(int year, int month, int monthDay, boolean isLeapMonth) throws CustomException {
        int dayOffset;
        int leapMonth;
        int i;
        if (year < MIN_YEAR || year > MAX_YEAR || month < 1 || month > 12
                || monthDay < 1 || monthDay > 30) {
            throw new CustomException(
                    "Illegal lunar date, must be like that:\n\t" +
                            "year : 1900~2099\n\t" +
                            "month : 1~12\n\t" +
                            "day : 1~30");
        }
        dayOffset = (LUNAR_INFO[year - MIN_YEAR] & 0x001F) - 1;
        if (((LUNAR_INFO[year - MIN_YEAR] & 0x0060) >> 5) == 2) {
            dayOffset += 31;
        }
        for (i = 1; i < month; i++) {
            if ((LUNAR_INFO[year - MIN_YEAR] & (0x80000 >> (i - 1))) == 0) {
                dayOffset += 29;
            } else {
                dayOffset += 30;
            }
        }
        dayOffset += monthDay;
        leapMonth = (LUNAR_INFO[year - MIN_YEAR] & 0xf00000) >> 20;
        // 这一年有闰月
        if (leapMonth != 0) {
            if (month > leapMonth || (month == leapMonth && isLeapMonth)) {
                if ((LUNAR_INFO[year - MIN_YEAR] & (0x80000 >> (month - 1))) == 0) {
                    dayOffset += 29;
                } else {
                    dayOffset += 30;
                }
            }
        }
        if (dayOffset > 366 || (year % 4 != 0 && dayOffset > 365)) {
            year += 1;
            if (year % 4 == 1) {
                dayOffset -= 366;
            } else {
                dayOffset -= 365;
            }
        }
        int[] solarInfo = new int[3];
        for (i = 1; i < 13; i++) {
            int iPos = DAYS_BEFORE_MONTH[i];
            if (year % 4 == 0 && i > 2) {
                iPos += 1;
            }
            if (year % 4 == 0 && i == 2 && iPos + 1 == dayOffset) {
                solarInfo[1] = i;
                solarInfo[2] = dayOffset - 31;
                break;
            }
            if (iPos >= dayOffset) {
                solarInfo[1] = i;
                iPos = DAYS_BEFORE_MONTH[i - 1];
                if (year % 4 == 0 && i > 2) {
                    iPos += 1;
                }
                if (dayOffset > iPos) {
                    solarInfo[2] = dayOffset - iPos;
                } else if (dayOffset == iPos) {
                    if (year % 4 == 0 && i == 2) {
                        solarInfo[2] = DAYS_BEFORE_MONTH[i] - DAYS_BEFORE_MONTH[i - 1] + 1;
                    } else {
                        solarInfo[2] = DAYS_BEFORE_MONTH[i] - DAYS_BEFORE_MONTH[i - 1];
                    }
                } else {
                    solarInfo[2] = dayOffset;
                }
                break;
            }
        }
        solarInfo[0] = year;
        return solarInfo;
    }

    /**
     * 传回农历 year年闰哪个月 1-12 , 没闰传回 0
     * @param year 将要计算的年份
     * @return 传回农历 year年闰哪个月1-12, 没闰传回 0
     */
    public static int leapMonth(int year) {
        return ((LUNAR_INFO[year - MIN_YEAR] & 0xF00000)) >> 20;
    }

    /**
     * 返回清明节
     * @param year
     * @return
     */
    public static String qing(int year) throws CustomException {
        if (year == 2232) {
            return year + "-04-0" + 4;
        }
        if (year < 1700) {
            throw new CustomException("1700年以前暂时不支持");
        }
        if (year >= 3100) {
            throw new CustomException("3100年以后暂时不支持");
        }
        double[] coefficient = {5.15, 5.37, 5.59, 4.82, 5.02, 5.26, 5.48, 4.70, 4.92, 5.135, 5.36, 4.60, 4.81, 5.04,
                5.26};
        int mod = year % 100;
        return year + "-04-0" + (int) (mod * 0.2422 + coefficient[year / 100 - 17] - mod / 4);
    }

    public static Map<String, String> getLegal(int year) throws CustomException {
        Map<String, String> map = new HashMap<>();
        //元旦
        map.put(year + "-01-01", "元旦");
        //春节
        int[] ints1 = lunarToSolar(year, 1, 1, false);
        LocalDate localDate = LocalDate.of(ints1[0], ints1[1], ints1[2]);
        map.put(localDate.toString(), "春节");
        map.put(localDate.plusDays(1).toString(), "春节");
        map.put(localDate.plusDays(2).toString(), "春节");
        //清明
        map.put(qing(year), "清明节");
        //劳动
        map.put(year + "-05-01", "劳动节");
        //端午
        int[] ints2 = lunarToSolar(year, 5, 5, false);
        map.put(LocalDate.of(ints2[0], ints2[1], ints2[2]).toString(), "端午节");
        //中秋
        int[] ints3 = lunarToSolar(year, 8, 15, false);
        map.put(LocalDate.of(ints3[0], ints3[1], ints3[2]).toString(), "中秋节");
        //国庆
        map.put(year + "-10-01", "国庆节");
        map.put(year + "-10-02", "国庆节");
        map.put(year + "-10-03", "国庆节");
        return map;
    }


    public static void main(String[] args) {
        //阴历转阳历
        try {
            System.out.println(Arrays.toString(lunarToSolar(2028,5,5,leapMonth(2009) == 5)));
            System.out.println(qing(2021));
            for (int i = 0; i < 10; i++) {
                System.out.println(getLegal(2020+i));
            }
            System.out.println(Arrays.toString(lunarToSolar(2028,1,1,false)));
            System.out.println(Arrays.toString(lunarToSolar(2028,5,5,false)));
            System.out.println(Arrays.toString(lunarToSolar(2028,8,15,false)));
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }
}
