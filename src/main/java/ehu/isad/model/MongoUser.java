package ehu.isad.model;

public class MongoUser {
    private static MongoUser superUser =new MongoUser();
    private String user="";
    private String password="";
    private String collection="";

    private MongoUser(){ }

    public static MongoUser getInstance(){
        return superUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }
}
