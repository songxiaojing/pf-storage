package com.topsec.bdc.platform.storage.services;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindFluent;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.topsec.bdc.platform.core.exception.PlatformException;
import com.topsec.bdc.platform.core.metrics.AbstractMetricMBean;
import com.topsec.bdc.platform.core.services.IService;
import com.topsec.bdc.platform.core.services.ServiceInfo;
import com.topsec.bdc.platform.core.utils.Assert;
import com.topsec.bdc.platform.log.PlatformLogger;


@ServiceInfo(description = "MongoDBStorageService", configurationPath = "application/services/MongoDBStorageService/")
public class MongoDBStorageService extends AbstractMetricMBean implements IService, IStorage {

    /**
     * logger
     */
    final private static PlatformLogger theLogger = PlatformLogger.getLogger(MongoDBStorageService.class);

    @XmlElementWrapper(name = "mongoDBConnections")
    @XmlElement(name = "mongoConnection", type = String.class)
    public ArrayList<String> _mongoDBConnections = new ArrayList<String>();

    @XmlElement(name = "initialPoolSize", type = Integer.class, defaultValue = "20")
    public Integer _initialPoolSize = new Integer(20);

    @XmlElement(name = "databaseInstace", type = String.class, defaultValue = "mcu")
    public String _databaseInstaceName = "mcu";

    private MongoClient _mongoClient = null;

    private MongoDatabase _databaseInstance = null;

    @Override
    public void start() throws PlatformException {

        initStorage();
    }

    @Override
    public void stop() throws PlatformException {

        destoryStorage();
    }

    @Override
    public void initStorage() throws PlatformException {

        try {
            ArrayList<ServerAddress> connectionList = new ArrayList<ServerAddress>();
            for (int i = 0; i < this._mongoDBConnections.size(); i++) {
                String[] info = this._mongoDBConnections.get(i).split(":");
                if (info.length != 2) {
                    continue;
                }
                connectionList.add(new ServerAddress(info[0], Integer.parseInt(info[1])));
            }

            this._mongoClient = new MongoClient(connectionList);
            this._databaseInstance = this._mongoClient.getDatabase(this._databaseInstaceName);
            theLogger.info("craeteMongoDBClient", connectionList.toString());
        } catch (Exception e) {
            theLogger.exception(e);
        }

    }

    @Override
    public void destoryStorage() throws PlatformException {

        this._databaseInstance = null;
        this._mongoClient.close();
        this._mongoClient = null;

    }

    /**
     * Your Methods description is in here.
     * 
     * @param args
     */
    public void write(String collectionName, List<Document> saveList) {

        if (Assert.isEmptyString(collectionName) == true) {
            return;
        }

        this._databaseInstance.getCollection(collectionName).insertMany(saveList);

    }

    /**
     * Your Methods description is in here.
     * 
     * @param args
     */
    public List<Document> read(String collectionName, Document queryDocument, Document sortDocument, int pageNo, int pageSize) {

        FindFluent<Document> findFluent = this._databaseInstance.getCollection(collectionName).find(queryDocument);
        findFluent.sort(sortDocument);
        findFluent.batchSize(pageNo);
        findFluent.limit(pageSize);
        //findFluent.;
        MongoCursor<Document> cursor = findFluent.iterator();
        ArrayList<Document> dataList = new ArrayList<Document>();
        try {
            while (cursor.hasNext()) {
                dataList.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        dataList.trimToSize();
        return dataList;
    }
}
