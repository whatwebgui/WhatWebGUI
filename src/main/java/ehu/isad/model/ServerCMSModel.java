package ehu.isad.model;

public class ServerCMSModel {

    private String url;
    private String cms;
    private String server;
    private String versionc;
    private String versions;
    private String lastUpdated;

    public ServerCMSModel(String url, String cms, String versionc, String server, String versions, String lastUpdated){
        this.url = url;
        this.cms = cms;
        this.versionc = versionc;
        this.server = server;
        this.versions = versions;
        this.lastUpdated = lastUpdated;
    }

    public String getUrl() {
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

    public String getLastUpdated() { return lastUpdated; }

}