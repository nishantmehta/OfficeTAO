package com.tao.utility;

import android.text.format.DateFormat;

import java.util.Calendar;

public class HelperFunctions {

	//date format function
	public static String timeFormater(long time){




        String date = DateFormat.format("MMM dd yyyy - hh:mm a", time).toString();

        return date;
    }

    public static String dateFormatter(Long time){
        return DateFormat.format("MMM-dd-yyyy", time).toString();
    }

    public static String onlyTimeFormatter(Long time){
        return DateFormat.format("hh:mm a", time).toString();
    }
    public static String makeDBCompact(String string){
        String data = string.replaceAll("'","`");
        return data;
    }
    public static String makeAppCompact(String string){
        String data = string.replaceAll("`","'");
        return data;
    }
}
