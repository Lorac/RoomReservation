package ca.ulaval.ift6002.sputnik.persistence.mongodb;

import ca.ulaval.ift6002.sputnik.domain.core.mongo.room.MongoRoom;
import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongodb.morphia.Morphia;

import java.net.InetSocketAddress;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBRoomRepositoryITest {

    private static final RoomNumber ROOM_NUMBER = new RoomNumber("PLT-1234");
    private static final int ROOM_CAPACITY = 4;
    private final String DATABASE_NAME = "MongoDBRoomRepositoryITest";
    private MongoClient mongoClient = null;
    private MongoServer mongoServer = null;
    private Morphia morphia = null;
    private RoomRepository roomRepository;

    @Before
    public void setUp() {
        mongoServer = new MongoServer(new MemoryBackend());
        InetSocketAddress serverAddress = mongoServer.bind();
        mongoClient = new MongoClient(new ServerAddress(serverAddress));
        mongoClient.dropDatabase(DATABASE_NAME);

        morphia = new Morphia();
        morphia.createDatastore(mongoClient, DATABASE_NAME);
        roomRepository = new MongoDBRoomRepository(mongoClient, morphia, DATABASE_NAME);
    }

    @After
    public void tearDown() {
        mongoClient.close();
        mongoServer.shutdownNow();
    }

    @Test
    public void persistsTheRoomWithTheRoomNumber() {
        Room room = new MongoRoom(ROOM_NUMBER, ROOM_CAPACITY);
        roomRepository.persist(room);

        Room roomFound = (Room) roomRepository.findRoomByNumber(ROOM_NUMBER);

        assertTrue("Should contain the room", roomFound.hasSameNumber(room));
    }
}
