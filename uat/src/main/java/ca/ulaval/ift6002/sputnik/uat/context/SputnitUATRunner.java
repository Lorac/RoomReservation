package ca.ulaval.ift6002.sputnik.uat.context;

import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStories;

public class SputnitUATRunner {
    private static UatContext context;

    @BeforeStories
    public static void start() throws Exception {
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
