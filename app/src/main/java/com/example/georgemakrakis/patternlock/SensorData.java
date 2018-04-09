package com.example.georgemakrakis.patternlock;

import java.util.List;

public class SensorData
{
    private long timeStamp;
    private List<TripleData> accelData;
    private List<TripleData> gyroData;
    private List<TripleData> laccelData;

    public SensorData(long timeStamp, List<TripleData> accelData, List<TripleData> gyroData, List<TripleData> laccelData)
    {
        this.timeStamp = timeStamp;
        this.accelData = accelData;
        this.gyroData = gyroData;
        this.laccelData = laccelData;
    }
    public long getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public List<TripleData> getAccelData()
    {
        return accelData;
    }

    public void setAccelData(List<TripleData> accelData)
    {
        this.accelData = accelData;
    }

    public List<TripleData> getGyroData()
    {
        return gyroData;
    }

    public void setGyroData(List<TripleData> gyroData)
    {
        this.gyroData = gyroData;
    }

    public List<TripleData> getLaccelData()
    {
        return laccelData;
    }

    public void setLaccelData(List<TripleData> laccelData)
    {
        this.laccelData = laccelData;
    }

}
