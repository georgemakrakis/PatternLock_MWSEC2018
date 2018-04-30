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

    public String toString()
    {
        StringBuilder str = new StringBuilder();

        //Losing some data here cause accelData list is bigger than laccelData and gyroData
        for (int i=0;i<laccelData.size();i++)
        {
            str.append(timeStamp).append(";").
                    append(accelData.get(i).x).append(";").append(accelData.get(i).y).append(";").
                    append(accelData.get(i).z).append(";").append(gyroData.get(i).x).append(";").
                    append(gyroData.get(i).y).append(";").append(gyroData.get(i).z).append(";").
                    append(laccelData.get(i).x).append(";").append(laccelData.get(i).y).append(";").
                    append(laccelData.get(i).z).append("\n");
        }
        return str.toString();
    }

}
