package com.fuyou.play.util.HORUtils;

/**
 * Created by Administrator on 2017/9/28 0028.
 */

public class PlanetOffsetEntity {

    private boolean isChanged;

    private float offset;
    private int offsetChange;

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public int getOffsetChange() {
        return offsetChange;
    }

    public void setOffsetChange(int offsetChange) {
        this.offsetChange = offsetChange;
    }

    @Override
    public String toString() {
        return "{" +
                "isChanged=" + isChanged +
                ", offset=" + offset +
                ", offsetChange=" + offsetChange +
                '}';
    }
}
