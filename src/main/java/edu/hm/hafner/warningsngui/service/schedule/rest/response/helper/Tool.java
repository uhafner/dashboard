package edu.hm.hafner.warningsngui.service.schedule.rest.response.helper;

public class Tool {
    private String id;
    private String latestUrl;
    private String name;
    private int size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatestUrl() {
        return latestUrl;
    }

    public void setLatestUrl(String latestUrl) {
        this.latestUrl = latestUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}