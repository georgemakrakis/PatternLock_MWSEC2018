package com.example.georgemakrakis.patternlock;

public class PairMetadata
{
    private String username;
    private int attempt;
    private String resolution;
    private int patternNumberA;
    private int patternNumberB;
    private Tuple<Float> centralCoordsA;
    private Tuple<Float> centralCoordsB;
    private Tuple<Float> firstCoordsA;
    private Tuple<Float> lastCoordsB;
    private double distanceAB;
    private long intertimeAB;
    private double avgSpeedAB;
    private float avgPressure;

    public PairMetadata(String username, int attempt, String resolution, int patternNumberA,
                        int patternNumberB, Tuple<Float> centralCoordsA, Tuple<Float> centralCoordsB,
                        Tuple<Float> firstCoordsA, Tuple<Float> lastCoordsB, double distanceAB,
                        long intertimeAB, double avgSpeedAB, float avgPressure)
    {
        this.username = username;
        this.attempt = attempt;
        this.resolution = resolution;
        this.patternNumberA = patternNumberA;
        this.patternNumberB = patternNumberB;
        this.centralCoordsA = centralCoordsA;
        this.centralCoordsB = centralCoordsB;
        this.firstCoordsA = firstCoordsA;
        this.lastCoordsB = lastCoordsB;
        this.distanceAB = distanceAB;
        this.intertimeAB = intertimeAB;
        this.avgSpeedAB = avgSpeedAB;
        this.avgPressure = avgPressure;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public int getAttempt()
    {
        return attempt;
    }

    public void setAttempt(int attempt)
    {
        this.attempt = attempt;
    }

    public String getResolution()
    {
        return resolution;
    }

    public void setResolution(String resolution)
    {
        this.resolution = resolution;
    }

    public int getPatternNumberA()
    {
        return patternNumberA;
    }

    public void setPatternNumberA(int patternNumberA)
    {
        this.patternNumberA = patternNumberA;
    }

    public int getPatternNumberB()
    {
        return patternNumberB;
    }

    public void setPatternNumberB(int patternNumberB)
    {
        this.patternNumberB = patternNumberB;
    }

    public Tuple<Float> getCentralCoordsA()
    {
        return centralCoordsA;
    }

    public void setCentralCoordsA(Tuple<Float> centralCoordsA)
    {
        this.centralCoordsA = centralCoordsA;
    }

    public Tuple<Float> getCentralCoordsB()
    {
        return centralCoordsB;
    }

    public void setCentralCoordsB(Tuple<Float> centralCoordsB)
    {
        this.centralCoordsB = centralCoordsB;
    }

    public Tuple<Float> getFirstCoordsA()
    {
        return firstCoordsA;
    }

    public void setFirstCoordsA(Tuple<Float> firstCoordsA)
    {
        this.firstCoordsA = firstCoordsA;
    }

    public Tuple<Float> getLastCoordsB()
    {
        return lastCoordsB;
    }

    public void setLastCoordsB(Tuple<Float> lastCoordsB)
    {
        this.lastCoordsB = lastCoordsB;
    }

    public double getDistanceAB()
    {
        return distanceAB;
    }

    public void setDistanceAB(double distanceAB)
    {
        this.distanceAB = distanceAB;
    }

    public long getIntertimeAB()
    {
        return intertimeAB;
    }

    public void setIntertimeAB(long intertimeAB)
    {
        this.intertimeAB = intertimeAB;
    }

    public double getAvgSpeedAB()
    {
        return avgSpeedAB;
    }

    public void setAvgSpeedAB(double avgSpeedAB)
    {
        this.avgSpeedAB = avgSpeedAB;
    }

    public float getAvgPressure()
    {
        return avgPressure;
    }

    public void setAvgPressure(float avgPressure)
    {
        this.avgPressure = avgPressure;
    }

    @Override
    public String toString()
    {
        return username + ";" + attempt + ";" + resolution + ";" + patternNumberA + ";" +
                patternNumberB + ";" + centralCoordsA.x + ";" + centralCoordsA.y + ";" +
                centralCoordsB.x + ";" + centralCoordsB.y + ";" + firstCoordsA.x+ ";" +
                firstCoordsA.y+ ";" + lastCoordsB.x + ";" +lastCoordsB.y + ";" + distanceAB + ";" +
                intertimeAB + ";" + avgSpeedAB + ";" + avgPressure + "\n";
    }
}
