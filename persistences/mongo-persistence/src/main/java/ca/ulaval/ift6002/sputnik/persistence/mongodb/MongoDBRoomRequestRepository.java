package ca.ulaval.ift6002.sputnik.persistence.mongodb;

import ca.ulaval.ift6002.sputnik.domain.core.mongo.request.MongoRoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;

public class MongoDBRoomRequestRepository extends BasicDAO<MongoRoomRequest, RequestIdentifier> implements RoomRequestRepository<MongoRoomRequest> {

    public MongoDBRoomRequestRepository(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, dbName);
    }

    @Override
    public MongoRoomRequest findReservationByIdentifier(RequestIdentifier identifier) {
        MongoRoomRequest request = findOne("identifier", identifier);
        if (request == null) {
            throw new RoomRequestNotFoundException(String.format("Couldn't find the request with identifier '%s'.", identifier.describe()));
        }
        return request;
    }

    @Override
    public List<MongoRoomRequest> findAll() {
        return find().asList();
    }

    @Override
    public void persist(MongoRoomRequest mongoRoomRequest) {
        save(mongoRoomRequest);
    }
}
