package ehu.isad.model;

public class ServerCMS {

    private String url;
    private String cms;
    private String server;
    private String version;
    private String lastUpdated;

    public ServerCMS(String url, String cms, String server, String version, String lastUpdated){
        this.url = url;
        this.cms = cms;
        this.server = server;
        this.version = version;
        this.lastUpdated = lastUpdated;
    }

    public String getUrl() {
        return url;
    }

    public String getCms() {
        return cms;
    }

    public String getServer() {
        return server;
    }

    public String getVersion() {
        return version;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
}