package ehu.isad.model;

import javafx.scene.control.Hyperlink;

import java.util.Date;

public class HistoryModel {

    private final Hyperlink domain;
    private final String tab;
    private final Date date;

    public HistoryModel(String domain, String tab, Date date) {
        this.domain = new Hyperlink(domain);
        this.tab = tab;
        this.date = date;
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
}
