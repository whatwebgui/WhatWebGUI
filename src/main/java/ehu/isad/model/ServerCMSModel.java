package ehu.isad.model;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.Date;
import javafx.scene.control.Hyperlink;
import javafx.scene.paint.Paint;

public class ServerCMSModel {

    private Hyperlink url;
    private String cms;
    private String server;
    private String versionc;
    private String versions;
    private Date lastUpdated;
    private FontAwesomeIconView star;

    public ServerCMSModel(String url, String cms, String versionc, String server, String versions, Date lastUpdated, int fav){
        this.url = new Hyperlink(url);
        this.cms = cms;
        if (cms==null) this.cms = "unknown";
        this.versionc = versionc;
        if (versionc==null) this.versionc = "unknown";
        this.server = server;
        if (server==null) this.server = "unknown";
        this.versions = versions;
        if (versions==null) this.versions = "unknown";
        this.lastUpdated = lastUpdated;
        this.star = new FontAwesomeIconView(FontAwesomeIcon.STAR);
        this.star.setSize("15");
        setStar(fav);
    }

    public FontAwesomeIconView getStar() {
        return star;
    }

    public void setStar(int fav){
        if (fav==1){
            this.star.setFill(Paint.valueOf("#ffff00"));
        } else {
            this.star.setFill(Paint.valueOf("#eaeaea"));
        }
        this.star.setStroke(Paint.valueOf("#aaaaaa"));
    }

    public Hyperlink getUrl() {
        return url;
    }

    public String getCms() {
        return cms;
    }

    public String getVersionc() { return versionc; }

    public String getServer() {
        return server;
    }

    public String getVersions() { return versions; }

    public Date getLastUpdated() { return lastUpdated; }

}