package com.example.georgemakrakis.patternlock;

public class PatternMetadata
{
    private String username;
    private int attempt;
    private String sequence;
    private int sequenceLength;
    private long timeToComplete;
    private double patternLength;
    private double avgSpeed;
    private float avgPressure;
    private float highestPressure;
    private float lowestPressure;
    private int handNum;
    private int fingerNum;

    public PatternMetadata(String username, int attempt, String sequence, int sequenceLength,
                           long timeToComplete, double patternLength, double avgSpeed, float avgPressure,
                           float highestPressure, float lowestPressure, int handNum, int fingerNum)
    {
        this.username = username;
        this.attempt = attempt;
        this.sequence = sequence;
        this.sequenceLength = sequenceLength;
        this.timeToComplete = timeToComplete;
        this.patternLength = patternLength;
        this.avgSpeed = avgSpeed;
        this.avgPressure = avgPressure;
        this.highestPressure = highestPressure;
        this.lowestPressure = lowestPressure;
        this.handNum = handNum;
        this.fingerNum = fingerNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public long getTimeToComplete() {
        return timeToComplete;
    }

    public void setTimeToComplete(long timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public double getPatternLength() {
        return patternLength;
    }

    public void setPatternLength(double patternLength) {
        this.patternLength = patternLength;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public float getAvgPressure() {
        return avgPressure;
    }

    public void setAvgPressure(float avgPressure) {
        this.avgPressure = avgPressure;
    }

    public float getHighestPressure() {
        return highestPressure;
    }

    public void setHighestPressure(float highestPressure) {
        this.highestPressure = highestPressure;
    }

    public float getLowestPressure() {
        return lowestPressure;
    }

    public void setLowestPressure(float lowestPressure) {
        this.lowestPressure = lowestPressure;
    }

    public int getHandNum() {
        return handNum;
    }

    public void setHandNum(int handNum) {
        this.handNum = handNum;
    }

    public int getFingerNum() {
        return fingerNum;
    }

    public void setFingerNum(int fingerNum) {
        this.fingerNum = fingerNum;
    }

}
