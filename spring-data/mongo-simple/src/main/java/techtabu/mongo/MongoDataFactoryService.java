package techtabu.mongo;

import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Service;


/**
 * @author TechTabu
 */

@Service
@Slf4j
public class MongoDataFactoryService {

    private final MongoDatabaseFactory mongo;

    public MongoDataFactoryService(MongoDatabaseFactory mongo) {
        this.mongo = mongo;
    }

    public void findAllDocuments() {

        log.info("finding all by using MongoDatabaseFactory");
        var iterator = mongo.getMongoDatabase()
                .getCollection("store_invoices")
                .find().iterator();

        while(iterator.hasNext()) {
            BsonDocument doc = iterator.next().toBsonDocument();

            log.info("Document: {}", doc);
        }
    }
}
