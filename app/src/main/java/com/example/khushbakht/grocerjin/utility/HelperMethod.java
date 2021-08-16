package com.example.khushbakht.grocerjin.utility;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Khushbakht on 8/10/2017.
 */

public class HelperMethod {

    public static String convertJavaToJson(final Object object){
        Gson gson = new Gson();
        return  gson.toJson(object);
    }



    public static String getResponse(final String jsonInString){
        try{
            JSONObject jsonObject  = new JSONObject(jsonInString);
            return  jsonObject.getString("response");
        }catch (Exception ex){

        }
        return null;
    }

    /*  public static String convertStringToDate(final String  dateStr){

          try{
              SimpleDateFormat parseFormat =
                      new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
              Date date = parseFormat.parse(dateStr);
              SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy");
              CharSequence charSequence= DateUtils.getRelativeTimeSpanString(date.getTime(),
                      new Date().getTime(), DateUtils.MINUTE_IN_MILLIS);
             String dateStrs = format.format(date);

              return   charSequence.toString();
          }catch (Exception ex){

          }
          return  "";
      }
  */
    public static String convertStringToDate(final String  dateStr){

        try{
            SimpleDateFormat parseFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = parseFormat.parse(dateStr);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            CharSequence charSequence= DateUtils.getRelativeTimeSpanString(date.getTime(),
                    new Date().getTime(), DateUtils.MINUTE_IN_MILLIS);
            String dateStrs = format.format(date);

            return   charSequence.toString();
        }catch (Exception ex){

        }
        return  "";
    }

    public static String convertExpToDate(final Context context, final String  dateStr){

        try{
            SimpleDateFormat parseFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            parseFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = parseFormat.parse(dateStr);


            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
            String datePattern      = ((SimpleDateFormat)dateFormat).toPattern();

            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
            String timePattern      = ((SimpleDateFormat)timeFormat).toPattern();
            final String localFormateformat = datePattern+ " "+ timePattern;
            SimpleDateFormat format = null;
            if(!TextUtils.isEmpty(localFormateformat)){
                format = new SimpleDateFormat(localFormateformat);
            }else{
                format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            }
            format.setTimeZone(TimeZone.getDefault());
            String dateStrs = format.format(date);

            return   dateStrs;
        }catch (Exception ex){

        }
        return  "";
    }

    public static Date convertToDate(final String  dateStr){
        Date date =null;
        try{
            SimpleDateFormat parseFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            date = parseFormat.parse(dateStr);

            return   date;
        }catch (Exception ex){
            //LogUtil.error("TAG" , ex.getMessage());
        }
        return  date;
    }

    /**
     *
     * @param value
     * @return
     */
    public static double convertStringToDouble(final String value){
        double amount = 0;
        try{
            amount = Double.parseDouble(value);
        }catch (Exception ex){
            amount = 0;
        }

        return  amount;
    }

    public static double convertStringToLong(final String value){
        double amount = 0;
        try{
            amount = Long.parseLong(value);
        }catch (Exception ex){
            amount = 0;
        }

        return  amount;
    }

    public  static boolean isPhotoMsg(final String msg){
        return msg.startsWith("(Photo)");
    }

    public  static boolean isLocationMsg(final String msg){
        return msg.startsWith("(Location)");
    }

    public  static String getPhotoUrl(final String msg){
        String msgStr = msg.replace("(Photo)"," ");
        return msgStr.trim();
    }
}
