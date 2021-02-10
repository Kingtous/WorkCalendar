package com.kingtous.workcalendar.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

import com.kingtous.workcalendar.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 这是一个时间转换的工具类
 * <p/>
 * 计算机能识别下面的字母："yyyy-MM-dd DD HH:mm:ss SSS"
 * y代表的是年份，M代表的是月份，d代表的当月的第几天，D代表的是当年的第几天，
 * H代表的是小时数，m代表的是分钟数，s代表的秒数，S代表的是毫秒数。这个常识是需要我们记住的。
 * <p/>
 * *方法1：long getTimeLong()
 * 获取当前时间的毫秒数
 * <p/>
 * *方法2：int getTimeInt(String filter)
 * 输入某种时间格式的字符串，显示当前时间它的数据，比如输入getTimeInt（“DD”）获取今日是当前月的天数
 * <p/>
 * * 方法3：String getTimeString()
 * 获取当前时间的完整的格式，比如2016-11-11 8：20：20
 * <p/>
 * * 方法4：String getTimeString(long time)
 * 输入一个long类型的数据，获取一个完整格式的时间字符串，获取到的格式和方法三一样
 * <p/>
 * * 方法5： String getTimeString(long time, String filter)
 * 输入一个long类型的数据和一个自定义时间的格式的字符串，获取一个自定义的时间字符串，
 * 比如getTimeString（1111111L，“MM-dd”）获取的是毫秒数11111111的月分数和天数
 * <p/>
 * * 方法6：String getTimeString( String filter)
 * 输入某种时间格式的字符串，显示当前时间它的数据，比如输入getTimeString（“DD”）获取今日是当前月的天数
 * 这个方法和方法2相似，不过这里获取到的是字符串，只能用来做显示，而方法二获取到的是数字，可以用来显示和做相关运算。
 * * 方法7：String getTimeLong( String filter,String date)
 * 输入某种时间格式的字符串和对应时间格式的时间，显示它的毫秒数，比如输入getTimeString（“MM-dd”，“8-12”）获取8月12日的毫秒数
 * 这个方法一般用于对两个时间进行比较，从而得出哪一个时间比较久
 */

public class TimeUtil {

    private static String[] workStatus={"上班","下班","休息"};
    /**
     * 获取今天的工作情况：休息、晚班、白班
     * 传入 1999-08-14 即可
     */
    public static String getWorkStatus(Context context,String currentDay){
        SharedPreferences sharedPreferences=context.getSharedPreferences(CodeDef.WORK_SETTING,Context.MODE_PRIVATE);
        int year=sharedPreferences.getInt(CodeDef.YEAR,0);
        int month=sharedPreferences.getInt(CodeDef.MONTH,0);
        int day=sharedPreferences.getInt(CodeDef.DAY,0);
        
        if (year==0 && month==0 && day==0){
            return context.getString(R.string.NO_SETTING);
        }

        try {
            Date nowdate = new SimpleDateFormat("yyyy-MM-dd").parse(currentDay);
            Date former_date = new SimpleDateFormat("yyyy-MM-dd").parse(String.format("%02d",year)+'-'+String.format("%02d",month)+'-'+String.format("%02d",day));
            long between = (nowdate.getTime()-former_date.getTime())/86400000; //86400000代表一天，以ms为单位
            if (between > 0){
                // 白班、晚班、休息、休息
                between = (int)(between % 3);
                return workStatus[(int) between];
            }
            else if (between < 0){
                between = - between;
                between = (int)(between % 3) - 1;
                if (between == -1){
                    // 循环至workstatus[0]
                    return workStatus[0];
                }
                else
                return workStatus[(int) (2-between)];
            }
            else {
                return workStatus[0];
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获得某年某月有多少天
     * @param year
     * @param month
     * @return
     */
    public static int getMonthOfDay(int year,int month){
        int day = 0;
        if(year%4==0&&year%100!=0||year%400==0){
            day = 29;
        }else{
            day = 28;
        }
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return day;

        }

        return 0;
    }



    /**
     * 获取当前时间的毫秒数
     */
    public static long getTimeLong() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当时时间的年，月，日，时分秒
     * 这里获得的时int类型的数据，要输入对应的格式
     * 比如要获得现在时间的天数值，
     * 使用getTime（“MM”）,如果现在是2016年5月20日，取得的数字是5；
     */
    public static int getTimeInt(String filter) {
        //
        SimpleDateFormat format = new SimpleDateFormat(filter);
        String time = format.format(new Date());
        return Integer.parseInt(time);
    }

    /**
     * 获取指定时间的年，月，日，时分秒
     * 这里获得的时int类型的数据，要输入完整时间的字符串和对应的格式
     * 比如要获得时间2016-12-12 14：12：10的小时的数值，
     * 使用getTime（“2016-12-12 14：12：10”，“HH”）；得到14
     */
    public static int getTimeInt(String StringTime, String filter) {
        //
        SimpleDateFormat format = new SimpleDateFormat(filter);
        String time = format.format(new Date(getTimeLong("yyyy-MM-dd HH:mm:ss", StringTime)));
        return Integer.parseInt(time);
    }


    /**
     * 获取当前时间的完整显示字符串
     */
    public static final String getTimeString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(getTimeLong()));
    }

    /**
     * 获得某个时间的完整格式的字符串
     * 输入的是某个时间的毫秒数，
     * 有些时候文件保存的时间是毫秒数，这时就需要转换来查看时间了！
     */
    public static final String getTimeString(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    /**
     * 获得自定义格式的时间字符串
     * 输入的是某个时间的毫秒数，显示的可以是时间字符串的某一部分
     * 比如想要获得某一个时间的月和日，getTimeString(1111111111111,"MM-dd");
     */
    public static final String getTimeString(long time, String filter) {
        SimpleDateFormat format = new SimpleDateFormat(filter);
        return format.format(new Date(time));
    }

    /**
     * 获得自定义格式的当前的时间的字符串
     * 比如当前时间2016年8月8日12时8分21秒，getTimeString("yyyy-MM-dd"),可以得到 2016-8-12
     */
    public static final String getTimeString(String filter) {
        SimpleDateFormat format = new SimpleDateFormat(filter);
        return format.format(new Date(getTimeLong()));
    }

    public static final String getChineseYMD(String year,String month,String day){
        return year+"年"+month+"月"+day+"日";
    }

    /**
     * 获取某个时间的毫秒数，
     * 一般作用于时间先后的对比
     * 第一个参数是时间的格式，第二个参数是时间的具体值
     * 比如需要知道2016-6-20的毫秒数（小时和分钟默认为零），
     * getTimeLong("yyyy-MM-dd","2016-6-20")
     * 有时只有月日也是可以的，比如  getTimeLong("MM-dd","6-20") ，一般这个用来比较时间先后
     * 记住获得的毫秒数越大，时间就越近你
     */
    public static Long getTimeLong(String filter, String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(filter);
            Date dateTime = format.parse(date);
            return dateTime.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0L;
    }


    /**
     * 获得某一个时间字符串中的局部字符串
     * 比如：String data= "2016-5-20 12：12：10"，要获得后面的时间：5-20或 12：10
     * 使用：getTimeLocalString("yyyy-MM-dd HH:mm:ss",data,"MM-dd")   ，可以获得5-20
     * 如果是data="2016-5-20"，要获得后面的5-20，
     * 也是一样的用法getTimeLocalString("yyyy-MM-dd ",data,"MM-dd")！
     */
    public static String getTimeLocalString(String filter, String data, String filterInside) {
        Long timeLong = getTimeLong(filter, data);
        return getTimeString(timeLong, filterInside);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowTime() {
        String timeString = null;
        Time time = new Time();
        time.setToNow();
        String year = thanTen(time.year);
        String month = thanTen(time.month + 1);
        String monthDay = thanTen(time.monthDay);
        String hour = thanTen(time.hour);
        String minute = thanTen(time.minute);

        timeString = year + "-" + month + "-" + monthDay + " " + hour + ":" + minute;
        return timeString;
    }

    /**
     * 十一下加零
     *
     * @param str
     * @return
     */
    private static String thanTen(int str) {

        String string = null;

        if (str < 10) {
            string = "0" + str;
        } else {

            string = "" + str;

        }
        return string;
    }

    /**
     * 计算时间差
     *
     * @param starTime 开始时间
     * @param endTime  结束时间
     *                 返回类型 ==1----天，时，分。 ==2----时
     * @return 返回时间差
     */
    public static String getTimeDifference(String starTime, String endTime) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();

            long day = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - day * 24);
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +"秒");
            long hour1 = diff / (60 * 60 * 1000);
            String hourString = hour1 + "";
            long min1 = ((diff / (60 * 1000)) - hour1 * 60);
            timeString = hour1 + "小时" + min1 + "分";//String.valueOf(day);
            System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeString;

    }

    /*
     * 将时间戳转换为时间
     *
     * s就是时间戳
     */

    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt * 1000);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @return
     */
    public static String getDateToString(long milSecond) {
        // 10位的秒级别的时间戳
        String result1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(milSecond * 1000));
        System.out.println("10位数的时间戳（秒）--->Date:" + result1);
        Date date1 = new Date(milSecond * 1000);   //对应的就是时间戳对应的Date
        // 13位的秒级别的时间戳
        // double time2 = 1515730332000d;
        // String result2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time2);
        // System.out.println("13位数的时间戳（毫秒）--->Date:" + result2);


        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return result1;
    }

}


