package com.shtaigaway.inspirewatchface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.Gravity;

import com.ustwo.clockwise.WatchFace;
import com.ustwo.clockwise.WatchFaceTime;
import com.ustwo.clockwise.WatchMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/11/15.
 */
public class InspireWatchFaceService extends WatchFace {
    public static final int OPTIMAL_WORD_LENGTH = 6;
    private Paint timePaint = new Paint();
    private Paint textPaint = new Paint();
    private final SimpleDateFormat timeFormat12 = new SimpleDateFormat("h:mm");
    private final SimpleDateFormat timeFormat24 = new SimpleDateFormat("HH:mm");
    private final Date date = new Date();

    private String timeText;
    private List<DailyInspiration> dailyInspirations;

    private DailyInspiration dailyInspiration;

    private int backgroundColor;

    @Override
    public void onCreate() {
        super.onCreate();
        createDailyInspirations();
        createDrawingPaints();
    }

    private void createDrawingPaints() {
        timePaint.setColor(Color.WHITE);
        timePaint.setTextSize(42);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.createFromAsset(getAssets(), "nevis.ttf"));

        backgroundColor = dailyInspiration.getColor();
    }

    private void createDailyInspirations() {
        dailyInspirations = Arrays.asList(
                new DailyInspiration("inspire", Color.parseColor("#FF9800")),
                new DailyInspiration("smile", Color.parseColor("#2196F3")),
                new DailyInspiration("passion", Color.parseColor("#E91E63")),
                new DailyInspiration("fresh", Color.parseColor("#FBC02D")),
                new DailyInspiration("peace", Color.parseColor("#689F38")),
                new DailyInspiration("efficient", Color.parseColor("#212121")),
                new DailyInspiration("beautiful", Color.parseColor("#673AB7")),
                new DailyInspiration("think", Color.parseColor("#03a9f4")),
                new DailyInspiration("team", Color.parseColor("#673ab7")),
                new DailyInspiration("now", Color.parseColor("#ff5722")),
                new DailyInspiration("humble", Color.parseColor("#4caf50")),
                new DailyInspiration("grateful", Color.parseColor("#009688")),
                new DailyInspiration("teach", Color.parseColor("#3f51b5")),
                new DailyInspiration("learn", Color.parseColor("#1976D2")),
                new DailyInspiration("persevere", Color.parseColor("#ffc107"))

        );
        chooseNewDailyInspiration();
    }

    private void chooseNewDailyInspiration() {
        Random random = new Random();
        dailyInspiration = dailyInspirations.get(random.nextInt(dailyInspirations.size()));
    }

    @Override
    protected WatchFaceStyle getWatchFaceStyle() {
        return new WatchFaceStyle.Builder(this)
                .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                .setShowSystemUiTime(false)
                .setHotwordIndicatorGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                .setStatusBarGravity(Gravity.END | Gravity.TOP)
                .build();
    }

    @Override
    protected void onWatchModeChanged(WatchMode watchMode) {
        switch (watchMode) {

            case INTERACTIVE:
                textPaint.setAntiAlias(true);
                timePaint.setAntiAlias(true);
                timePaint.setStyle(Paint.Style.FILL);
                backgroundColor = dailyInspiration.getColor();
                break;

            case AMBIENT:
                backgroundColor = Color.LTGRAY;
                timePaint.setStyle(Paint.Style.FILL);
                break;

            case LOW_BIT:
                backgroundColor = Color.BLACK;
                textPaint.setAntiAlias(false);
                timePaint.setStyle(Paint.Style.FILL);
                timePaint.setAntiAlias(false);
                break;

            case BURN_IN:
            case LOW_BIT_BURN_IN:
                backgroundColor = Color.BLACK;
                timePaint.setStyle(Paint.Style.STROKE);
                timePaint.setStrokeWidth(1.0f);
                textPaint.setAntiAlias(true);
                timePaint.setAntiAlias(true);
                break;

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);
        drawInspirationText(canvas);
        canvas.drawText(timeText, getWidth() / 5f, getHeight() / 3.3f, timePaint);
    }

    private void drawInspirationText(Canvas canvas) {
        String word = dailyInspiration.getWord().toUpperCase();
        int reduceOffset = word.length() - OPTIMAL_WORD_LENGTH;
        textPaint.setTextSize(48 - reduceOffset * 2);
        float textWidth = textPaint.measureText(word);
        float textHeight = measureHeight(word);
        canvas.drawText(word, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight, textPaint);
    }

    @Override
    protected void onTimeChanged(WatchFaceTime oldTime, WatchFaceTime newTime) {
        super.onTimeChanged(oldTime, newTime);
        date.setTime(newTime.toMillis(true));
        timeText = is24HourFormat() ? timeFormat24.format(date) :
                timeFormat12.format(date);
        if (oldTime.hasDateChanged(newTime)) {
            chooseNewDailyInspiration();
        }
    }

    public int measureHeight(String text) {
        Rect result = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), result);
        return result.height();
    }
}
