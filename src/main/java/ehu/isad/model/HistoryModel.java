package ehu.isad.model;

import java.util.Date;

public class HistoryModel {

    private String domain;
    private String tab;
    private Date date;
    private String path;

    public HistoryModel(String domain, String tab, Date date, String path) {
        this.domain = domain;
        this.tab = tab;
        this.date = date;
        this.path = path;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
