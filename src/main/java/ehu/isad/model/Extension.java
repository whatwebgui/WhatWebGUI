package ehu.isad.model;

public class Extension {

    private String displayName;
    private String extension;
    private String type;

    public Extension(String displayName, String extension, String type) {
        this.displayName = displayName;
        this.extension = extension;
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getExtension() {
        return extension;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() { return displayName; }
}
