package ehu.isad.model;

import javafx.scene.control.Hyperlink;

import java.util.Date;

public class HistoryModel {

    private final Hyperlink domain;
    private final String tab;
    private final Date date;
    private final String path;

    public HistoryModel(String domain, String tab, Date date, String path) {
        this.domain = new Hyperlink(domain);
        this.tab = tab;
        this.date = date;
        this.path = path;
    }

    public Hyperlink getDomain() {
        return domain;
    }

    public String getTab() {
        return tab;
    }

    public Date getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }
}
