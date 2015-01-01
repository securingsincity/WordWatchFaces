package com.jameshrisho.wordsbattery;

import android.content.Context;

/**
 * Created by james on 1/1/15.
 */
public class WordDateHelpers {
    private String mAmString;
    private String mPmString;

    public WordDateHelpers(Context c) {
        mAmString = c.getString(R.string.digital_am);
        mPmString = c.getString(R.string.digital_pm);
    }
    public String formatTwoDigitNumber(int hour) {
        return String.format("%02d", hour);
    }

    public int convertTo12Hour(int hour) {
        int result = hour % 12;
        return (result == 0) ? 12 : result;
    }

    public String getAmPmString(int hour) {
        return (hour < 12) ? mAmString : mPmString;
    }

}
