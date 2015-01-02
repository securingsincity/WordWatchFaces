package com.jameshrisho.wordsbattery;

import android.content.Context;

/**
 * Created by james on 1/1/15.
 */
public class WordDateHelpers {
    private String mAmString;
    private String mPmString;
    private String[] ones;
    private String[] tens;
    private String[] teens;
    private String[] months;
    private String ohClock;
    private String oh;

    public String getNoon() {
        return noon;
    }

    public String getMidnight() {
        return midnight;
    }

    private String noon;
    private String midnight;

    public WordDateHelpers(Context c) {
        mAmString = c.getString(R.string.digital_am);
        mPmString = c.getString(R.string.digital_pm);
        ohClock = c.getString(R.string.oh_clock);
        oh = c.getString(R.string.oh);
        noon = c.getString(R.string.noon);
        midnight = c.getString(R.string.midnight);
        ones = c.getResources().getStringArray(R.array.ones);
        tens = c.getResources().getStringArray(R.array.tens);
        teens = c.getResources().getStringArray(R.array.teens);
        months = c.getResources().getStringArray(R.array.months);
    }
    public String formatTwoDigitNumber(int hour) {
        return String.format("%02d", hour);
    }

    public int convertTo12Hour(int hour) {
        int result = hour % 12;
        return (result == 0) ? 12 : result;
    }
    public String getTensMinutes(int minutes) {
       int firstDigit = Integer.parseInt(Integer.toString(minutes).substring(0, 1));
       return tens[firstDigit];
    }
    public String getMinutes(int minutes) {
       String minutesText = "";
       if (minutes > 19 || minutes == 10 ) {
           minutesText += getTensMinutes(minutes) + "\n";
           int secondDigit = Integer.parseInt(Integer.toString(minutes).substring(1, 2));
           if(secondDigit != 0) {
               minutesText += ones[secondDigit];
           }
       } else if (minutes > 10) {
           switch (minutes) {
               case 11:
                   return teens[1];
               case 12:
                   return teens[2];
               case 13:
                   return teens[3];
               case 14:
                   return teens[4];
               case 15:
                   return teens[5];
               case 16:
                   return teens[6];
               case 17:
                   return teens[7];
               case 18:
                   return teens[8];
               case 19:
                   return teens[9];
           }
       } else {
           if(minutes != 0) {
               minutesText += oh +ones[minutes];
           } else {
               minutesText += ohClock;
           }
       }
       return minutesText;
    }

    public String getHour(int hour) {
        int hour12Hour = convertTo12Hour(hour);
        if (hour12Hour == 0 || hour12Hour == 12) {
            return teens[2];
        } else if (hour12Hour == 11) {
            return teens[1];
        } else if (hour12Hour == 10) {
            return tens[1];
        } else {
            return ones[hour12Hour];
        }
    }
    public String getAmPmString(int hour) {
        return (hour < 12) ? mAmString : mPmString;
    }



}
