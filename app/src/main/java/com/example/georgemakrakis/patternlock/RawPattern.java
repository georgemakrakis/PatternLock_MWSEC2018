package com.example.georgemakrakis.patternlock;

import java.util.List;

public class RawPattern
{
    private String numberOfActivatedPoint;
    private long timeStamp;
    private List<Tuple<Float>> coordinates;
    private List<Float> pressure;

    public RawPattern(String numberOfActivatedPoint, long timeStamp, List<Tuple<Float>> coordinates, List<Float> pressure)
    {
        this.numberOfActivatedPoint = numberOfActivatedPoint;
        this.timeStamp = timeStamp;
        this.coordinates = coordinates;
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

    public List<Tuple<Float>> getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates(List<Tuple<Float>> coordinates)
    {
        this.coordinates = coordinates;
    }

    public List<Float> getPressure()
    {
        return pressure;
    }

    public void setPressure(List<Float> pressure)
    {
        this.pressure = pressure;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for (int i=0;i<coordinates.size();i++)
        {
            str.append(numberOfActivatedPoint).append(";").append(timeStamp).append(";").
                    append(coordinates.get(i).x).append(";").append(coordinates.get(i).y).
                    append(";").append(pressure.get(i)+"\n");
        }
        return str.toString();
    }
}
