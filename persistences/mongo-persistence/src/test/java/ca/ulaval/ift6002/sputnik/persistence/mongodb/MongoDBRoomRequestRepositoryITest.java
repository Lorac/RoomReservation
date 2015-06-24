package ca.ulaval.ift6002.sputnik.persistence.mongodb;

import ca.ulaval.ift6002.sputnik.domain.core.mongo.request.MongoRoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongodb.morphia.Morphia;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBRoomRequestRepositoryITest {

    private static final String DATABASE_NAME = "MongoDBRoomRequestRepositoryITest";
    private MongoServer mongoServer;
    private MongoClient mongoClient;
    private Morphia morphia;
    private RoomRequestRepository roomRequestRepository;

    @Before
    public void setUp() {
        mongoServer = new MongoServer(new MemoryBackend());
        InetSocketAddress serverAddress = mongoServer.bind();
        mongoClient = new MongoClient(new ServerAddress(serverAddress));
        mongoClient.dropDatabase(DATABASE_NAME);

        morphia = new Morphia();
        morphia.createDatastore(mongoClient, DATABASE_NAME);
        roomRequestRepository = new MongoDBRoomRequestRepository(mongoClient, morphia, DATABASE_NAME);
    }

    @After
    public void tearDown() {
        mongoClient.close();
        mongoServer.shutdownNow();
    }

    @Test
    public void persistsTheRoomWithTheRoomNumber() {
        RequestIdentifier identifier = RequestIdentifier.create();
        RoomRequest request = new MongoRoomRequest(identifier, Priority.HIGH, new User("organizer@email.com"), new LinkedList<>());
        roomRequestRepository.persist(request);

        RoomRequest requestFound = (RoomRequest) roomRequestRepository.findReservationByIdentifier(identifier);

        assertTrue("Should contain the request", requestFound.hasIdentifier(identifier));
    }

    @Test
    public void whenFindAllWithNoRoomShouldReturnNothing() {
        List<RoomRequest> all = roomRequestRepository.findAll();

        assertTrue("Should not contain a request", all.isEmpty());
    }

    @Test(expected = RoomRequestNotFoundException.class)
    public void whenTryingToFindARoomWithWrongNumberItShouldThrow() {
        roomRequestRepository.findReservationByIdentifier(RequestIdentifier.create());
    }
}
