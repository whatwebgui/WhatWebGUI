package ehu.isad.model;

import javafx.scene.control.Hyperlink;


public class SecurityModel {

    private Hyperlink url;
    private String IP;
    private String country;
    private boolean vulnerable;

    public SecurityModel(String url, String IP, String country, boolean vuln){
        this.url = new Hyperlink(url);
        this.IP = IP;
        this.country = country;
        vulnerable = vuln;
    }

    public Hyperlink getUrl() {
        return url;
    }

    public String getIP() {
        return IP;
    }

    public String getCountry() {
        return country;
    }

    public boolean isVulnerable() {
        return vulnerable;
    }
}