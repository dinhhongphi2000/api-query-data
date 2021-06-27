// package org.apiquery.shared.utils;

// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.time.ZoneId;
// import java.time.ZonedDateTime;
// import java.util.*;

// import com.google.type.DayOfWeek;

// public class ZoneDateTimeUtil {
//     public static final ZoneId defaultZoneId = ZoneId.of("Asia/Ho_Chi_Minh");

//     public static Date now() {
//         return now(defaultZoneId);
//     }

//     public static Date now(ZoneId zone) {
//         ZonedDateTime datetime = ZonedDateTime.now(zone);
//         return Date.from(datetime.toInstant());
//     };

//     public static int getYear(Date date) {
//         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
//         return Integer.parseInt(simpleDateFormat.format(date));
//     }

//     public static byte getMonth(Date date) {
//         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
//         return (byte) Integer.parseInt(simpleDateFormat.format(date));
//     }

//     public static byte getDay(Date date) {
//         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
//         return (byte) Integer.parseInt(simpleDateFormat.format(date));
//     }

//     public static String format(Date date, String formatPatten) {
//         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPatten);
//         return simpleDateFormat.format(date);
//     }

//     public static Date addDayFromNow(int numberDay) {
//         Calendar c = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         c.setTime(ZoneDateTimeUtil.now());
//         c.add(Calendar.DAY_OF_MONTH, numberDay);
//         return c.getTime();
//     }

//     public static Date addDayFrom(Date date, int numberDay) {
//         Calendar c = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         c.setTime(date);
//         c.add(Calendar.DAY_OF_MONTH, numberDay);
//         return c.getTime();
//     }

//     public static Date addMinuteFrom(Date date, int numberMinute) {
//         Calendar c = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         c.setTime(date);
//         c.add(Calendar.MINUTE, numberMinute);
//         return c.getTime();
//     }

//     public static Date addHourFrom(Date date, int numberHour) {
//         Calendar c = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         c.setTime(date);
//         c.add(Calendar.HOUR_OF_DAY, numberHour);
//         return c.getTime();
//     }

//     public static Date addSecondsFromNow(int second) {
//         Calendar calendar = Calendar.getInstance();
//         calendar.setTime(ZoneDateTimeUtil.now());
//         calendar.add(Calendar.SECOND, second);
//         return calendar.getTime();
//     }

//     public static Date addWeekFrom(Date date, int numberWeek) {
//         Calendar c = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         c.setTime(date);
//         c.add(Calendar.DAY_OF_MONTH, numberWeek * 7);
//         return c.getTime();
//     }

//     public static Date addHourFromNow(int numberHour) {
//         Calendar calendar = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         calendar.setTime(ZoneDateTimeUtil.now());
//         calendar.add(Calendar.HOUR, numberHour);
//         return calendar.getTime();
//     }

//     public static Map<Integer, String> initMonth() {
//         Map<Integer, String> mapMonths = new HashMap<>();
//         mapMonths.put(0, "-- Chọn tháng --");
//         for (int i = 1; i <= 12; i++) {
//             mapMonths.put(i, "Tháng  " + i);
//         }
//         return mapMonths;
//     }

//     public static List<Integer> initYear() {
//         List<Integer> years = new ArrayList<>();
//         for (int i = getYear(new Date()) + 1; i >= 1900; i--) {
//             years.add(i);
//         }
//         return years;
//     }

//     public static Date initTimeFromMonthAndYear(Integer month, Integer year) {
//         SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//         String initDate = "03-" + month + "-" + year;
//         try {
//             return sdf.parse(initDate);
//         } catch (Exception e) {
//             return new Date();
//         }
//     }

//     public static Date getWeekStartDate(Date date) {
//         Calendar cal = Calendar.getInstance();
//         cal.setTime(date);
//         cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//         return cal.getTime();
//     }

//     public static Date getLastDayOfMonth(Date date) {
//         Calendar cal = Calendar.getInstance();
//         cal.setTime(date);
//         cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//         int lastDay = cal.getActualMaximum(Calendar.DATE);
//         cal.set(Calendar.DAY_OF_MONTH, lastDay);
//         return cal.getTime();
//     }

//     public static Date getWeekEndDate(Date date) {
//         Date firstDayOfWeek = getWeekStartDate(date);
//         Calendar c = Calendar.getInstance();
//         c.setTime(firstDayOfWeek);
//         c.add(Calendar.DAY_OF_MONTH, 6);
//         return c.getTime();
//     }

//     public static Date groupBy(Date date, Date time) {
//         Calendar c1 = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         Calendar c2 = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         c1.setTime(date);
//         c2.setTime(time);

//         c1.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY) - 1);
//         c1.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
//         c1.set(Calendar.SECOND, c2.get(Calendar.SECOND));
//         return c1.getTime();
//     }

//     public static LocalTime toLocalTime(Date date) {
//         return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
//     }

//     public static Date setTimeToZero(Date date) {
//         Calendar c = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         c.setTime(date);
//         c.set(Calendar.HOUR_OF_DAY, 0);
//         c.set(Calendar.MINUTE, 0);
//         c.set(Calendar.SECOND, 0);
//         return c.getTime();
//     }

//     public static Calendar getCalendarInstanceByDefaultZone() {
//         return Calendar.getInstance(TimeZone.getTimeZone(ZoneDateTimeUtil.defaultZoneId), new Locale("VI", "VIETNAM"));
//     }

//     public static Date parse(String date) {
//         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//         try {
//             return simpleDateFormat.parse(date);
//         } catch (ParseException e) {
//             return null;
//         }
//     }

//     /**
//      * Value SunDay = 1 -> Saturday = 7
//      */
//     public static int getCurrentDayOfWeek() {
//         Calendar c = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         return c.get(Calendar.DAY_OF_WEEK);
//     }

//     public static int getCurrentHour() {
//         Calendar c = ZoneDateTimeUtil.getCalendarInstanceByDefaultZone();
//         return c.get(Calendar.HOUR_OF_DAY);
//     }
// }
