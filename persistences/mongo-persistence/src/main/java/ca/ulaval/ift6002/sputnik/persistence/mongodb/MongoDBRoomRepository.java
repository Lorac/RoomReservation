package ca.ulaval.ift6002.sputnik.persistence.mongodb;


import ca.ulaval.ift6002.sputnik.domain.core.mongo.room.MongoRoom;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomRepository;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;

public class MongoDBRoomRepository extends BasicDAO<MongoRoom, RoomNumber> implements RoomRepository<MongoRoom> {


    protected MongoDBRoomRepository(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, dbName);
    }

    @Override
    public MongoRoom findRoomByNumber(RoomNumber roomNumber) {
        return findOne("roomNumber", roomNumber);
    }

    @Override
    public List<MongoRoom> findAll() {
        return find().asList();
    }

    @Override
    public void persist(MongoRoom mongoRoom) {
        save(mongoRoom);
    }
}
