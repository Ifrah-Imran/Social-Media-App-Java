package dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.*;
import org.bson.Document;

public class DBConnection {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    // Your Atlas database name
    private static final String DB_NAME = "oop_proj";

    // Replace this with your MongoDB Atlas connection string
    private static final String MONGO_URI =
    "mongodb+srv://<username>:<password>@<cluster-name>.mongodb.net/<database-name>?retryWrites=true&w=majority";

    private static GridFSBucket gridFSBucket;

    public static MongoDatabase getInstance() {
        if (database == null) {
            connect();
        }
        return database;
    }

    private static void connect() {
        try {
            // Fix for DNS TXT record lookup failure
            System.setProperty("java.naming.provider.url", "dns://8.8.8.8");

            mongoClient = MongoClients.create(MONGO_URI);
            database = mongoClient.getDatabase(DB_NAME);
            gridFSBucket = GridFSBuckets.create(database);
            initializeCollections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GridFSBucket getGridFSBucket() {
        if (gridFSBucket == null) {
            getInstance(); // Ensure initialized
        }
        return gridFSBucket;
    }

    private static void initializeCollections() {
        try {
            if (!collectionExists("users"))
                database.createCollection("users");
            if (!collectionExists("posts"))
                database.createCollection("posts");
            if (!collectionExists("messages"))
                database.createCollection("messages");
            if (!collectionExists("notifications"))
                database.createCollection("notifications");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean collectionExists(String collectionName) {
        for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName))
                return true;
        }
        return false;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
