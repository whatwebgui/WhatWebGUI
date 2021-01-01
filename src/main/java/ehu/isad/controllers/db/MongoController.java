package ehu.isad.controllers.db;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ehu.isad.utils.CmsMongo;
import ehu.isad.model.MongoUser;
import ehu.isad.utils.Utils;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MongoController {
    //singleton
    private static MongoController mongoKud = new MongoController();

    private MongoController() { }

    public static MongoController getInstance() {
        return mongoKud;
    }


    public List<CmsMongo> getMongoCMS() {
        //returns all of the elements in mongos db with the specified columns
        //domain name(target), CMS(plugins.MetaGenerator.string) and the countrys name and abbreviation
        List<CmsMongo> list = new ArrayList<>();

        try (MongoClient client = new MongoClient("localhost", 27017)) {
            MongoDatabase database = client.getDatabase(Utils.getProperties().getProperty("dbMongo"));
            MongoCollection<Document> collection = database.getCollection(MongoUser.getInstance().getCollection());
            // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

            Document query = new Document();
            query.append("http_status", 200.0);

            Document projection = new Document();
            projection.append("_id", 0.0);
            projection.append("target", 1.0);
            projection.append("plugins.MetaGenerator.string", 1.0);
            projection.append("plugins.Country.module",1.0);
            projection.append("plugins.Country.string",1.0);

            Consumer<Document> processBlock = new Consumer<Document>() {
                @Override
                public void accept(Document document) {
                    //System.out.println(document.toJson());
                    CmsMongo lag = new Gson().fromJson(document.toJson(), CmsMongo.class);
                    list.add(lag);
                }
            };
            collection.find(query).projection(projection).forEach(processBlock);

        } catch (MongoException e) {
            // handle MongoDB exception
            e.printStackTrace();
        }
        if (list.isEmpty()){
            System.out.println("Empty list...");
        }
        return list;
    }


    public Boolean mongoSearch(String url){
        //it will search in mongos db for the specified domain
        //out: true if it is, else false
        List<CmsMongo> list=new ArrayList<>();
        try (MongoClient client = new MongoClient("localhost", 27017)) {

            MongoDatabase database = client.getDatabase(Utils.getProperties().getProperty("dbMongo"));
            MongoCollection<Document> collection = database.getCollection(MongoUser.getInstance().getCollection());

            // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

            Document query = new Document();
            query.append("target", url);

            Consumer<Document> processBlock = new Consumer<Document>() {
                @Override
                public void accept(Document document) {
                    var unekoa=new Gson().fromJson(document.toJson(), CmsMongo.class);
                    list.add(unekoa);
                }
            };
            collection.find(query).forEach(processBlock);

        } catch (MongoException e) {
            // handle MongoDB exception
        }
        return !list.isEmpty();
    }
}
