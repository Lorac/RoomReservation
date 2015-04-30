package ca.ulaval.ift6002.sputnik.interfaces;

import ca.ulaval.ift6002.sputnik.context.DemoContext;
import ca.ulaval.ift6002.sputnik.interfaces.rest.resources.filters.EntityManagerContextFilter;
import ca.ulaval.ift6002.sputnik.interfaces.rest.server.SputnikServer;

import java.util.Arrays;

public class RestMain {
    private final static int DEFAULT_PORT = 8081;

    public static void main(String[] args) throws Exception {

        new DemoContext().apply();
        SputnikServer server = new SputnikServer();
        server.start(DEFAULT_PORT, Arrays.asList(EntityManagerContextFilter.class));
        server.join();
    }

    private RestMain() {
    }
}
