package com.herbalife.myapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sahara on 2016/5/10.
 *
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    try {
        Date d = format.parse("2016-08-02 12:30");
        System.out.println(d.getTime());
        System.out.println(d.getDate());
        System.out.println("年"+(d.getYear()+1900));
        System.out.println("月"+(d.getMonth()+1));
        System.out.println("日"+d.getDay());
        System.out.println("时"+d.getHours());
        System.out.println("分"+d.getMinutes());
    } catch (ParseException e) {
        e.printStackTrace();
    }
 */
public class UtilDate {

    public static UtilDate utilDate;

    public static UtilDate getInstance(){
        if (utilDate==null){
            utilDate = new UtilDate();
        }
        return utilDate;
    }

    /**
     * 将日期格式转换成秒
     * @param formatStr
     * @return
     */
    public static long getSec(String formatStr){
        if (formatStr==null){
            return 0;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//如果是小写的hh那转换出来的就是 12小时制的时间  大写的就是24小时制的
        Date d ;
        long time = 0;
        try {
            d = format.parse(formatStr);
            if (d!=null){
                time = d.getTime();//单位毫秒
            }else{
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time/1000;
    }

    /**
     * 将日期格式转换成秒
     * @param formatStr
     * @return
     */
    public static long getSec2(String formatStr){
        if (formatStr==null){
            return 0;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d ;
        long time = 0;
        try {
            d = format.parse(formatStr);
            if (d!=null){
                time = d.getTime();//单位毫秒
            }else{
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time/1000;
    }

    public static long getSecDate(String formatStr){
        if (formatStr==null){
            return 0;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d ;
        long time = 0;
        try {
            d = format.parse(formatStr);
            if (d!=null){
                time = d.getTime();//单位毫秒
            }else{
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time/1000;
    }

    public static long getSecTime(String formatStr){
        if (formatStr==null){
            return 0;
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date d ;
        long time = 0;
        try {
            d = format.parse(formatStr);
            if (d!=null){
                time = d.getTime();//单位毫秒
            }else{
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time/1000;
    }

    /**
     * 根据秒计算出日期
     * @param sec
     * @return
     */
    public static String formatDuring2(long sec) {
        long days = sec / (60 * 60 * 24);
        long hours = (sec % (60 * 60 * 24)) / (60 * 60);
        long minutes = (sec % (60 * 60)) / (60);
        long seconds = (sec % (60));
        if (days!=0){//天数不为0就直接显示天数
            return days+"天前";
        }
        if (hours!=0){
            return hours+"小时前";
        }
        if (minutes!=0){
            return minutes+"分钟前";
        }
        if (seconds!=0){
            return seconds+"秒前";
        }
        return "刚刚";
    }

    /**
     * 根据秒计算出日期
     * @return
     */
    public static String[] formatDuring(long sec) {
        String[] s = new String[4];
        long days = sec / (60 * 60 * 24);
        long hours = (sec % (60 * 60 * 24)) / (60 * 60);
        long minutes = (sec % (60 * 60)) / (60);
        long seconds = (sec % (60));
        if (days<10){
            s[0] = "0"+days;
        }else{
            s[0] = days+"";
        }
        if (hours<10){
            s[1] = "0"+hours;
        }else{
            s[1] = hours+"";
        }
        if (minutes<10){
            s[2] = "0"+minutes;
        }else{
            s[2] = minutes+"";
        }
        if (seconds<10){
            s[3] = "0"+seconds;
        }else{
            s[3] = seconds+"";
        }
        return s;
//        return seconds+"秒/"+minutes + "分钟/"+ hours + "小时/"+days + "天";
    }

    /**
     * 根据时间差，按照规则计算出应该显示的时间形式，一般计算的是开始时间
     * @return
     */
    public static String countTime(String endTime){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        String yearStr;
        String monthStr;
        String dayStr;
        String hoursStr;
        String minStr;
        try {
            date = format.parse(endTime);
            calendar.setTime(date);
            yearStr = (calendar.get(Calendar.YEAR)+"").substring(2);
            if ((calendar.get(Calendar.MONTH)+1)<10){
                monthStr = "0"+(calendar.get(Calendar.MONTH)+1);
            }else{
                monthStr = (calendar.get(Calendar.MONTH)+1)+"";
            }
            if (calendar.get(Calendar.DAY_OF_MONTH)<10){
                dayStr = "0"+calendar.get(Calendar.DAY_OF_MONTH);
            }else{
                dayStr = calendar.get(Calendar.DAY_OF_MONTH)+"";
            }
            if (calendar.get(Calendar.HOUR_OF_DAY)<10){
                hoursStr = "0"+calendar.get(Calendar.HOUR_OF_DAY);
            }else{
                hoursStr = calendar.get(Calendar.HOUR_OF_DAY)+"";
            }
            if (calendar.get(Calendar.MINUTE)<10){
                minStr = "0"+calendar.get(Calendar.MINUTE);
            }else{
                minStr = calendar.get(Calendar.MINUTE)+"";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return endTime;
        }
        long sec = getSec(getDate()) - getSec(endTime);
        int day = (int) ((getSecDate(getDate()) - getSecDate(endTime)))/(24*60*60);//计算出相差几天
        if (sec<60){
            return "1分钟内";
        }else if (sec<3600){//1小时内，就显示多少分钟前
            long minutes = (sec % (60 * 60)) / (60);
            return minutes+"分钟前";
        }else if (sec<21600){//6小时内
            long hours = (sec % (60 * 60 * 24)) / (60 * 60);
            return hours+"小时前";
        }else if (day==0){//超过6小时并且是当天，就显示HH：mm
            return hoursStr+":"+minStr;
        }else if (day==1){//相差的天数是一天，就显示: 昨天 HH：mm
            return "昨天 "+hoursStr+":"+minStr;
        }else if (day==2){//相差的天数是两天，就显示： 前天HH：mm
            return "前天 "+hoursStr+":"+minStr;
        }else{//否则就显示： 月：日
            return yearStr+"-"+monthStr+"-"+dayStr+" "+hoursStr+":"+minStr;
        }
    }

    /**
     * 根据时间差，按照规则计算出应该显示的时间形式,计算的是结束时间
     * @return
     */
    public static String countEndTime(String endTime){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        String yearStr;
        String monthStr;
        String dayStr;
        String hoursStr;
        String minStr;
        try {
            date = format.parse(endTime);
            calendar.setTime(date);
            yearStr = (calendar.get(Calendar.YEAR)+"").substring(2);
            if ((calendar.get(Calendar.MONTH)+1)<10){
                monthStr = "0"+(calendar.get(Calendar.MONTH)+1);
            }else{
                monthStr = (calendar.get(Calendar.MONTH)+1)+"";
            }
            if (calendar.get(Calendar.DAY_OF_MONTH)<10){
                dayStr = "0"+calendar.get(Calendar.DAY_OF_MONTH);
            }else{
                dayStr = calendar.get(Calendar.DAY_OF_MONTH)+"";
            }
            if (calendar.get(Calendar.HOUR_OF_DAY)<10){
                hoursStr = "0"+calendar.get(Calendar.HOUR_OF_DAY);
            }else{
                hoursStr = calendar.get(Calendar.HOUR_OF_DAY)+"";
            }
            if (calendar.get(Calendar.MINUTE)<10){
                minStr = "0"+calendar.get(Calendar.MINUTE);
            }else{
                minStr = calendar.get(Calendar.MINUTE)+"";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return endTime;
        }
        long sec = getSec(getDate()) - getSec(endTime);
        int day = (int) ((getSecDate(getDate()) - getSecDate(endTime)))/(24*60*60);//计算出相差几天
        if (day==0){//超过6小时并且是当天，就显示HH：mm
            return "今天 "+hoursStr+":"+minStr;
        }else if (day==1){//相差的天数是一天，就显示: 昨天 HH：mm
            return "昨天 "+hoursStr+":"+minStr;
        }else if (day==2){//相差的天数是两天，就显示： 前天HH：mm
            return "前天 "+hoursStr+":"+minStr;
        }else if(day == -1){
            return "明天 "+hoursStr+":"+minStr;
        }else if(day == -2){
            return "后天 "+hoursStr+":"+minStr;
        }else{//否则就显示： 月：日
            return yearStr+"-"+monthStr+"-"+dayStr+" "+hoursStr+":"+minStr;
        }
    }

    /**
     * 获取系统当前时间
     */
    public static String getDate(){
        SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
        Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        return str;
    }

}
