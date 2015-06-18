package ca.ulaval.ift6002.sputnik.persistence.mongodb;

import ca.ulaval.ift6002.sputnik.domain.core.mongo.room.MongoRoom;
import ca.ulaval.ift6002.sputnik.domain.core.room.*;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongodb.morphia.Morphia;

import java.net.InetSocketAddress;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBRoomRepositoryITest {

    private static final RoomNumber ROOM_NUMBER = new RoomNumber("PLT-1234");
    private static final RoomNumber BAD_ROOM_NUMBER = new RoomNumber("BAD_ROOM_NUMBER");
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

    @Test
    public void whenFindAllWithNoRoomShouldReturnNothing() {
        List<MongoRoom> all = roomRepository.findAll();

        assertTrue("Should not contain a room", all.isEmpty());
    }

    @Test(expected = RoomNotFoundException.class)
    public void whenTryingToFindARoomWithWrongNumberItShouldThrow() {
        roomRepository.findRoomByNumber(BAD_ROOM_NUMBER);
    }
}
