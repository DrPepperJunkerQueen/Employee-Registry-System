package com.example.ersapp;

public class GroupRatingStats {
    private String groupName;
    private Long count;
    private Double average;

    public GroupRatingStats(String groupName, Long count, Double average) {
        this.groupName = groupName;
        this.count = count;
        this.average = average;
    }

    public String getGroupName() {
        return groupName;
    }

    public Long getCount() {
        return count;
    }

    public Double getAverage() {
        return average;
    }
}