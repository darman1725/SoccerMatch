package org.aplas.soccermatch;

public class LogItem {
    private String name;
    private String time;
    private String player;

    public LogItem(String n, String t, String p) {
        name = n;
        time = t;
        player = p;
    }

    public String getName() {
        return name;
    }

    public void setName(String val) {
        this.name = val;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String val) {
        this.time = val;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String val) {
        this.player = val;
    }
}
