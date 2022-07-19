package com.ps.pslibrary;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sanam Mijar on 14,May,2021.
 */

public class PSDateTimeUtils {
    private final Context context;
    public static final int DATE = 0; // To use only date
    public static final int DATE_TIME = 1; // To use both date and time

    public static final int DT_FROM = 0; // Use this to define the date picker start date where users can select from
    public static final int DT_TO = 1; // Use this to define the date picker end date where users can select to date.
    public static final int DT_AFTER_FROM = 2; // Use this to start the selection date that starts from the from selected date

    public static final int DAY_ZERO = 0; // Use this to specify start or end days to Zero (0)
    public static final int DAY_WEEK = 7; // Use this to specify start or end days to 1 week (7 days)
    public static final int DAY_H_MONTH = 16; // Use this to specify start or end days to half a month (16 days)
    public static final int DAY_MONTH = 32; // Use this to specify start or end days to a month (32 days)
    public static final int DAY_H_YEAR = 182; // Use this to specify start or end days to half a year (182 days)
    public static final int DAY_YEAR = 365; // Use this to specify start or end days to a year (365 days)

    public static final int DF_DMY = 0; // Use this to specify Date format dd-MM-yyy
    public static final int DF_MDY = 1; // Use this to specify Date format MM-dd-yyy
    public static final int DF_YMD = 2; // Use this to specify Date format yyy-MM-dd
    public static final int DTF_DMY_HMS = 3; // Use this to specify Date & Time format dd-MM-yyy hh:mm:ss
    public static final int DTF_MDY_HMS = 4; // Use this to specify Date & Time format MM-dd-yyy hh:mm:ss
    public static final int DTF_YMD_HMS = 5; // Use this to specify Date & Time format yyy-MM-dd hh:mm:ss
    public static final int DTF_YMD_HMS_AM = 6; // Use this to specify Date & Time format yyy-MM-dd hh:mm:ss AM/PM
    public static final int DTF_YMD_HMS_24 = 7; // Use this to specify Date & Time format yyy-MM-dd hh:mm:ss e.g.(12 = 24, 11 = 23...)

    public static final int DATE_FORMAT_DMY_HMS_AM = 6; // Use this to specify Date & Time format dd-MM-yyy hh:mm:ss AM/PM
    public static final int DATE_FORMAT_DMY_HMS_24 = 7; // Use this to specify Date & Time format dd-MM-yyy hh:mm:ss e.g.(12 = 24, 11 = 23...)

    public static final int DATE_FORMAT_MDY_HMS_AM = 8; // Use this to specify Date & Time format MM-dd-yyy hh:mm:ss AM/PM
    public static final int DATE_FORMAT_MDY_HMS_24 = 9; // Use this to specify Date & Time format MM-dd-yyy hh:mm:ss e.g.(12 = 24, 11 = 23...)

    public static final int DATE_FORMAT_YMD_HMS_AM = 10; // Use this to specify Date & Time format yyy-MM-dd hh:mm:ss AM/PM
    public static final int DATE_FORMAT_YMD_HMS_24 = 11; // Use this to specify Date & Time format yyy-MM-dd hh:mm:ss e.g.(12 = 24, 11 = 23...)

    public static final int TIME_FORMAT_AM = 1; // Use this to specify Time AM/PM
    public static final int TIME_FORMAT_24 = 2; // Use this to specify Time e.g.(12 = 24, 11 = 23...)

    public static final String DTS_SLASH = "/"; // Use this to specify date or/and time separation
    public static final String DTS_COLON = ":"; // Use this to specify date or/and time separation
    public static final String DTS_HYPHEN = "-"; // Use this to specify date or/and time separation
    public static final String DTS_SPACE = " "; // Use this to specify date or/and time separation

    public PSDateTimeUtils(Context context){ this.context = context; }


    /**
     * getGreetingForCurrentTime to show the greeting message according to the current time.
     **/
    public static String getGreetingForCurrentTime(){
        String greetingMessage = "Welcome!";
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hours = cal.get(Calendar.HOUR_OF_DAY);

        if (hours >= 1 && hours <= 12) {
            greetingMessage = "Good Morning!";
        } else if (hours >= 12 && hours <= 16) {
            greetingMessage = "Good Afternoon!";
        } else if (hours >= 16 && hours <= 21) {
            greetingMessage = "Good Evening!";
        } else if (hours >= 21 && hours <= 24) {
            greetingMessage = "Good Night!";
        }
        return greetingMessage;
    }

    /**
    * getTodayDateForHomeCardView
    * It shows the current date in "EEEE | MMM d, yyyy" format. i.e.: Tuesday | Nov 2, 2021
    **/
    public static String getTodayDateForHomeCardView(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE | MMM d, yyyy");
        Date date = new Date();
        return formatter.format(date);
    }

    /**
     * showDatePicker params
     * int type         = This is the show the date and time picker type e.g.: Date only or date and time also.
     * int dateType     = This is to define the pick date type e.g.: Date starting from, date ending at and date after given date
     * TextView         = placeToKeepDate is to put the picked date and time.
     * int dateFormat   = To specify the date and time format e.g.: yyyy-MM-dd, MM-dd-yyy, etc.
     * String separator = To specify the date and time separator for expected out put format.
     * int minDate      = To specify the date to select from. Here you can specify integer value e.g.: 365 to pick date a year before.
     * int maxDate      = To specify the date to select up to. Here you can specify integer value e.g.: 365 to pick date a year after minDate.
     **/
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDatePicker(final int type, int dateType, final TextView placeToKeepDate, int dateFormat, String separator, int minDate, int maxDate){
        final Calendar cl = Calendar.getInstance();
        Calendar tenDaysAgo = (Calendar) cl.clone();
        if (dateType == DT_FROM || dateType == DT_TO){
            tenDaysAgo.add(Calendar.DATE, -minDate);
        }else{
            tenDaysAgo.add(Calendar.DATE, -minDate + 1);
        }
        if (dateType == DT_AFTER_FROM){
            cl.add(Calendar.DATE, -minDate+1);
        }

        Calendar today = (Calendar) cl.clone();
        today.add(Calendar.DATE,maxDate);

        final int day = cl.get(Calendar.DAY_OF_MONTH);
        final int month = cl.get(Calendar.MONTH);
        final int year = cl.get(Calendar.YEAR);
        @SuppressLint("SetTextI18n")
        DatePickerDialog picker = new DatePickerDialog(context,
                (view, pickedYear, pickedMonth, pickedDay) -> placeToKeepDate.setText(getFormattedDate(pickedYear, pickedMonth, pickedDay, dateFormat, separator)), year, month, day);
        switch (type) {
            case DATE:
                picker.getDatePicker().setMinDate(tenDaysAgo.getTimeInMillis());
                picker.getDatePicker().setMaxDate(today.getTimeInMillis());
                break;
            case DATE_TIME:
                picker.getDatePicker().setMaxDate(today.getTimeInMillis());
                break;
        }
        picker.show();
    }


    /**
     * showTimePicker params
     * TextView placeToKeepTime = To specify where you want to store the picked time.
     * String separator         = To specify the time separator e.g.: ":", "-", "/", etc.
     **/
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showTimePicker(final TextView placeToKeepTime, String separator){
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, (timePicker, selectedHour, selectedMinute) -> placeToKeepTime.setText( selectedHour + separator + selectedMinute), hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle(context.getString(R.string.select_time));
        mTimePicker.show();
    }


    /**
     * getDateDifference params
     * Date d1 = First Date you want to get difference from.
     * Date d2 = Last Date you want to get difference from.
     **/
    public static long getDateDifference(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }


    /**
     * getTotalDaysBWDays params
     * String initialDate   = First Date you want to get difference from and the params should must be in string format.
     * String lastDate      = Last Date you want to get difference from and the params should must be in string format.
     **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long getTotalDaysBWDays(String initialDate, String lastDate){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(getDateTimeFormatType(DF_DMY, DTS_SLASH,""));
        LocalDateTime date1 = LocalDateTime.from(LocalDate.parse(initialDate, dtf));
        LocalDateTime date2 = LocalDateTime.from(LocalDate.parse(lastDate, dtf));
        return Duration.between(date1, date2).toDays();
    }

    /**
     * getTotalDaysTillOrFromToday params
     * String inputDate   = Date you want to get total days from to till date.
     * int dateFormat     = Format of the date you want as a result e.g.: yyy-MM-dd, etc.
     * String separate    = Use this to specify the date separator as your expectation.
     **/
    public static int getTotalDaysTillOrFromToday(String inputDate, int dateFormat, String separate){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(getDateTimeFormatType(dateFormat, separate,""));
        Date todayDate = new Date();
        Date itemFoundDate = null;
        try {
            itemFoundDate = format.parse(inputDate);
        } catch (ParseException e) { e.printStackTrace(); }

        return (int) getDateDifference(itemFoundDate, todayDate);
    }


    /**
     * getDateTimeFormatType params
     * int format               = Date format as your expectations e.g.: yyy-MM-dd, MM-dd-yyyy, etc.
     * String separate          = Use this to specify the date separator as your expectation.
     * String timeSeparator     = Use this to specify the time separator as your expectation e.g.: ":", "-", etc.
     **/
    private static String getDateTimeFormatType(int format, String separator, String timeSeparator){
        String formattedDateType = "";
        switch (format){
            case DF_DMY:
                formattedDateType = "dd"+separator+"MM"+separator+"yyyy";
                break;
            case DF_MDY:
                formattedDateType = "MM"+separator+"dd"+separator+"yyyy";
                break;
            case DF_YMD:
                formattedDateType = "yyyy"+separator+"MM"+separator+"dd";
                break;
            case DTF_DMY_HMS:
                formattedDateType = "dd"+separator+"MM"+separator+"yyyy hh"+timeSeparator+"mm"+timeSeparator+"ss"+getTimeFormat(PSDateTimeUtils.TIME_FORMAT_AM);
                break;
            case DTF_MDY_HMS:
                formattedDateType = "MM"+separator+"dd"+separator+"yyyy hh"+timeSeparator+"mm"+timeSeparator+"ss"+getTimeFormat(PSDateTimeUtils.TIME_FORMAT_AM);
                break;
            case DTF_YMD_HMS:
                formattedDateType = "yyyy"+separator+"MM"+separator+"dd hh"+timeSeparator+"mm"+timeSeparator+"ss"+getTimeFormat(PSDateTimeUtils.TIME_FORMAT_AM);
                break;
        }
        return formattedDateType;
    }


    /**
     * getDateTimeFormatType params
     * int timeFormat = Time format as your expectations e.g.: yyy-MM-dd, MM-dd-yyyy, etc.
     **/
    private static String getTimeFormat(int timeFormat){
        if (timeFormat == TIME_FORMAT_24){
            return " A";
        }else{
            return " a";
        }
    }


    /**
     * getFormattedDate params
     * String dateString = Date in string format to format.
     **/
    public static String getFormattedDate(String dateString) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date = inputFormat.parse(dateString);
        String outputText = outputFormat.format(date);
        return outputText;
    }


    /**
     * getFormattedDate params
     * int year         = Integer value for year e.g.: 2021
     * int month        = Integer value for month e.g.: 01
     * int day          = Integer value for day e.g.: 01
     * int format       = Integer value for formatting order of expected output. (Choose from the static variable mentioned above.)
     * String separator = Use this to specify the time separator as your expectation e.g.: "-", "/", etc. can also choose from the static variable mentioned above.
     **/
    private String getFormattedDate(int year, int month, int day, int format, String separator){
        String formattedDate = "";
        switch (format){
            case DF_MDY:
                formattedDate = (month + 1) + separator + day + separator + year;
                break;
            case DF_YMD:
                formattedDate = year + separator + (month + 1) + separator + day;
                break;
            default:
                formattedDate = day + separator + (month + 1) + separator + year;
                break;
        }
        return formattedDate;
    }


    /**
     * getFormattedDate params
     * String date          = String format date
     * String dateSeparator = Use this to specify the time separator as your expectation e.g.: "-", "/", etc. can also choose from the static variable mentioned above.
     **/
    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDate(String date, String dateSeparator) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("MM"+dateSeparator+"dd"+dateSeparator+"yyyy HH:mm:ss", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        Date formatDate = inputFormat.parse(date);
        String outputText = outputFormat.format(formatDate);
        return outputText;

//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat dateExtractor;
//        String formattedDate = "";
//        Date mDate;
//        dateExtractor = new SimpleDateFormat(getDateTimeFormatType(formatType, dateSeparator, DTS_COLON), Locale.US);
//        mDate = dateExtractor.parse(date);
//        if (mDate != null) {
//            formattedDate = mDate.toString();
//        }
//        return formattedDate;
    }


    /**
     * getFormattedDateAndTime params
     * String date          = String format date
     * String dateSeparator = Use this to specify the time separator as your expectation e.g.: "-", "/", etc. can also choose from the static variable mentioned above.
     **/
    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDateAndTime(String date, String dateSeparator) throws ParseException {
        DateFormat inputFormat = new SimpleDateFormat("MM"+dateSeparator+"dd"+dateSeparator+"yyyy HH:mm:ss", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        Date formatDate = inputFormat.parse(date);
        if (formatDate != null) {
            return outputFormat.format(formatDate);
        }else{
            return "N/A";
        }
    }


    /**
     * timeAgo params
     * long time_ago = Use long value to show the time ago
     **/
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String timeAgo(long time_ago) {
        String timeAgo;
        long cur_time = (Calendar.getInstance().getTimeInMillis()) / 1000;
        long time_elapsed = cur_time - time_ago;
        long seconds = time_elapsed;
        int minutes = Math.round(time_elapsed / 60);
        int hours = Math.round(time_elapsed / 3600);
        int days = Math.round(time_elapsed / 86400);
        int weeks = Math.round(time_elapsed / 604800);
        int months = Math.round(time_elapsed / 2600640);
        int years = Math.round(time_elapsed / 31207680);

        // Seconds
        if (seconds <= 60) { timeAgo = "Just now"; }
        //Minutes
        else if (minutes <= 60) { if (minutes == 1) { timeAgo = "Minute ago"; } else { timeAgo = minutes + " minutes ago"; } }
        //Hours
        else if (hours <= 24) { if (hours == 1) { timeAgo = "Hour ago"; } else { timeAgo = hours + " hrs ago"; } }
        //Days
        else if (days <= 7) { if (days == 1) { timeAgo = "yesterday"; } else { timeAgo = days + " days ago"; } }
        //Weeks
        else if (weeks <= 4.3) { if (weeks == 1) { timeAgo = "a week ago"; } else { timeAgo = weeks + " weeks ago"; } }
        //Months
        else if (months <= 12) { if (months == 1) { timeAgo = "a month ago"; } else { timeAgo = months + " months ago"; } }
        //Years
        else { if (years == 1) { timeAgo = "one year ago"; } else { timeAgo = years + " years ago"; } }
        return timeAgo;
    }


    /**
     * isTimeAlreadyGone params
     * String startDateTime = Start date and time to get the time already gone from. (This date and time should be less than end date and time)
     * String endDateTime   = End date and time to get the time already gone up to. (This date and time should be greater than start date and time)
     **/
    public static String isTimeAlreadyGone(String startDateTime, String endDateTime) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateExtractor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date olcStartTime = dateExtractor.parse(startDateTime);
        Date olcEndDateTime = dateExtractor.parse(endDateTime);
        if(System.currentTimeMillis() >= olcStartTime.getTime()){
            if (System.currentTimeMillis() > olcEndDateTime.getTime()) {
                return "Finished";
            }else{
                    return "On Going";
            }
        }else{
            return "Not Started";
        }
    }


    /**
     * timeAgoGenerator params
     * String inpDate = Pass the date to get the time ago from. (This should not be the future date.)
     **/
    public static String timeAgoGenerator(String inpDate) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateExtractor = new SimpleDateFormat("MM/dd/yyyy");
        Date pastDate = dateExtractor.parse(inpDate);
        Date currentDate = new Date();

        String returnTimeAgo;

        long milliSecPerMinute = 60 * 1000;
        long milliSecPerHour = milliSecPerMinute * 60;
        long milliSecPerDay = milliSecPerHour * 24;
        long milliSecPerMonth = milliSecPerDay * 30;
        long milliSecPerYear = milliSecPerDay * 365;
        //Difference in Milliseconds between two dates
        long msExpired = 0;
        if (pastDate != null) {
            msExpired = currentDate.getTime() - pastDate.getTime();
        }
        //Second or Seconds ago calculation
        if (msExpired < milliSecPerMinute) {
            if (Math.round(msExpired / 1000) == 1) {
                returnTimeAgo = "Just now.";
            } else {
                returnTimeAgo = Math.round(msExpired / 1000) + " seconds ago.";
            }
        }
        //Minute or Minutes ago calculation
        else if (msExpired < milliSecPerHour) {
            if (Math.round(msExpired / milliSecPerMinute) == 1) {
                returnTimeAgo = Math.round(msExpired / milliSecPerMinute) + " minute ago.";
            } else {
                returnTimeAgo = Math.round(msExpired / milliSecPerMinute) + " minutes ago.";
            }
        }
        //Hour or Hours ago calculation
        else if (msExpired < milliSecPerDay) {
            if (Math.round(msExpired / milliSecPerHour) == 1) {
                returnTimeAgo = Math.round(msExpired / milliSecPerHour) + " hour ago.";
            } else {
                returnTimeAgo = Math.round(msExpired / milliSecPerHour) + " hours ago.";
            }
        }
        //Day or Days ago calculation
        else if (msExpired < milliSecPerMonth) {
            if (Math.round(msExpired / milliSecPerDay) == 1) {
                returnTimeAgo = Math.round(msExpired / milliSecPerDay) + " day ago.";
            } else {
                returnTimeAgo = Math.round(msExpired / milliSecPerDay) + " days ago.";
            }
        }
        //Month or Months ago calculation
        else if (msExpired < milliSecPerYear) {
            if (Math.round(msExpired / milliSecPerMonth) == 1) {
                returnTimeAgo = Math.round(msExpired / milliSecPerMonth) + "  month ago.";
            } else {
                returnTimeAgo = Math.round(msExpired / milliSecPerMonth) + "  months ago.";
            }
        }
        //Year or Years ago calculation
        else {
            if (Math.round(msExpired / milliSecPerYear) == 1) {
                returnTimeAgo = Math.round(msExpired / milliSecPerYear) + " year ago.";
            } else {
                returnTimeAgo = Math.round(msExpired / milliSecPerYear) + " years ago.";
            }
        }
        System.out.println("Date for: "+inpDate+", Parsed Date: "+pastDate+", Returned: "+returnTimeAgo);
        return returnTimeAgo;
    }
}