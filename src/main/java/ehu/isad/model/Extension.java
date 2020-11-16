package ehu.isad.model;

public class Extension {

    private String displayName;
    private String extension;

    public Extension(String displayName, String extension) {
        this.displayName = displayName;
        this.extension = extension;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getExtension() {
        return extension;
    }
}
