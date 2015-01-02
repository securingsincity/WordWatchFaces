package com.jameshrisho.wordsbattery;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.TimeZone;

public class WordBatteryWFService extends AbstractFaceService {
    private static final String TAG = "WordBatteryWFService";



    public WordBatteryWFService() {
    }

    @Override
    public Engine onCreateEngine() {
        /* provide your watch face implementation */
        return new Engine();
    }

    /* implement service callback methods */
    private class Engine extends AbstractFaceService.Engine {

        /* device features */
        boolean mLowBitAmbient;

        /* graphic objects */
        Bitmap mBackgroundBitmap;
        Bitmap mBackgroundScaledBitmap;
        Paint mHourPaint;
        Paint mAMPMPaint;
        Paint mMinutePaint;
        Paint mTickPaint;
        boolean mMute;

        @Override
        public void onCreate(SurfaceHolder holder) {
            /* initialize your watch face */
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onCreate");
            }
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(WordBatteryWFService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

//            Resources resources = AnalogWatchFaceService.this.getResources();
//            Drawable backgroundDrawable = resources.getDrawable(R.drawable.bg);
//            mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();
            mHourPaint = createTextPaint(Color.parseColor("WHITE"), Typeface.DEFAULT_BOLD);
            mMinutePaint = createTextPaint(Color.parseColor("WHITE"));
            mAMPMPaint = createTextPaint(Color.parseColor("WHITE"));



            mTime = new Time();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            /* get device features (burn-in, low-bit ambient) */
        }



        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            /* the wearable switched between modes */
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            /* draw your watch face */
            mTime.setToNow();

            int width = bounds.width();
            int height = bounds.height();

//            // Draw the background, scaled to fit.
//            if (mBackgroundScaledBitmap == null
//                    || mBackgroundScaledBitmap.getWidth() != width
//                    || mBackgroundScaledBitmap.getHeight() != height) {
//                mBackgroundScaledBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap,
//                        width, height, true /* filter */);
//            }
            // Draw the background.
            Paint mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundPaint);


            // Find the center. Ignore the window insets so that, on round watches with a
            // "chin", the watch face is centered on the entire screen, not just the usable
            // portion.
            float centerX = width / 2f;
            float centerY = height / 2f;
            //let's build a set of text so if its 4:30 it reads FourThirty
            WordDateHelpers wordDateHelpers = new WordDateHelpers(getApplicationContext());
            // it's midnight!
            if (mTime.minute == 0 && mTime.hour == 0) {
                canvas.drawText(wordDateHelpers.getMidnight(),centerX-70,centerY-40,mHourPaint);
            } else if (mTime.minute == 0 && mTime.hour == 12) {
                //it's noon
                canvas.drawText(wordDateHelpers.getNoon(),centerX-70,centerY-40,mHourPaint);
            } else {
                String hour = wordDateHelpers.getHour(mTime.hour);

                String minutes = wordDateHelpers.getMinutes(mTime.minute);
//            String AMPMString = wordDateHelpers.getAmPmString(mTime.hour);
                canvas.drawText(hour, centerX-80, centerY-50, mHourPaint);
                float minuteY = centerY;
                for (String line: minutes.split("\n")) {
                    canvas.drawText(line, centerX-80, minuteY, mHourPaint);
                    minuteY += -mHourPaint.ascent() + mHourPaint.descent();
                }
            }

//            canvas.drawText(AMPMString, centerX-20, centerY+70, mAMPMPaint);


        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            WordBatteryWFService.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            WordBatteryWFService.this.unregisterReceiver(mTimeZoneReceiver);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onVisibilityChanged: " + visible);
            }

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();
            } else {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

    }
}
