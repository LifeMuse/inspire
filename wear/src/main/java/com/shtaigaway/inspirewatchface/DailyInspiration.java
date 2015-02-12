package com.shtaigaway.inspirewatchface;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/12/15.
 */
public class DailyInspiration {
    private final String word;
    private final int color;

    public DailyInspiration(String word, int color) {
        this.word = word;
        this.color = color;
    }

    public String getWord() {
        return word;
    }

    public int getColor() {
        return color;
    }
}
