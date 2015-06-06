package ca.ulaval.ift6002.sputnik.uat.context;

import org.jbehave.core.annotations.*;

class SputnitUATRunner {
    private static UatContext context;

    @BeforeStories
    public static void start() {
        context = new UatContext();
        context.apply();
    }

    @BeforeScenario
    public void reinitializeData() {
        context.reinitialize();
    }

    @AfterStories
    public void stopServer() {
        context.reinitialize();
        context.stopMailServer();
        context = null;
    }
}
