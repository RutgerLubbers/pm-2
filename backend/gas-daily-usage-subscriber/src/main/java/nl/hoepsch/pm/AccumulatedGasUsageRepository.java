package nl.hoepsch.pm;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import nl.hoepsch.pm.model.AccumulatedGasUsage;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Repository
public class AccumulatedGasUsageRepository implements InitializingBean {

    private MongoClient mongoClient;
    private CodecRegistry pojoCodecRegistry;

    public AccumulatedGasUsage store(AccumulatedGasUsage value) {
        final MongoDatabase database = mongoClient.getDatabase("pm");
        final MongoCollection<AccumulatedGasUsage> collection = database.getCollection("accumulated_daily_usage", AccumulatedGasUsage.class);

        if (value.getId() == null) {
            System.out.println("Store");
            value.setId(new ObjectId());
            collection.insertOne(value);
        } else {
            System.out.println("replace");
            Bson filter = Filters.eq("_id", value.getId());

            collection.replaceOne(filter, value);
        }

        return value;
    }

    @Override
    public void afterPropertiesSet() {
        pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));


        MongoCredential credential = MongoCredential.createCredential("pm",
                "pm",
                "pm".toCharArray());

        String host = "localhost";
        int port = 27017;
        ServerAddress address = new ServerAddress(host, port);
        final MongoClientOptions clientOptions = MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build();
        mongoClient = new MongoClient(address, credential, clientOptions);
    }

}
