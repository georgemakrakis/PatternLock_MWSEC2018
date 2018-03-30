package com.example.georgemakrakis.patternlock;

public class RawPattern
{
    private String numberOfActivatedPoint;
    private long timeStamp;
    private String xyPoints;
    private float pressure;

    public RawPattern(String numberOfActivatedPoint, long timeStamp, String xyPoints, float pressure)
    {
        this.numberOfActivatedPoint = numberOfActivatedPoint;
        this.timeStamp = timeStamp;
        this.xyPoints = xyPoints;
        this.pressure = pressure;
    }

    public String getNumberOfActivatedPoint()
    {
        return numberOfActivatedPoint;
    }

    public void setNumberOfActivatedPoint(String numberOfActivatedPoint)
    {
        this.numberOfActivatedPoint = numberOfActivatedPoint;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getXyPoints()
    {
        return xyPoints;
    }

    public void setXyPoints(String xyPoints)
    {
        this.xyPoints = xyPoints;
    }

    public float getPressure()
    {
        return pressure;
    }

    public void setPressure(float pressure)
    {
        this.pressure = pressure;
    }
}
