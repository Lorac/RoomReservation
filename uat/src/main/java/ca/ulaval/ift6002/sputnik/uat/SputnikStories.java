package ca.ulaval.ift6002.sputnik.uat;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.PendingStepStrategy;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.ScanningStepsFactory;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;

public class SputnikStories extends JUnitStories {

    private PendingStepStrategy pendingStepStrategy = new FailingUponPendingStep();

    private Format[] formats = new Format[]{CONSOLE};
    private StoryReporterBuilder reporterBuilder =
            new StoryReporterBuilder()
                    .withCodeLocation(codeLocationFromClass(SputnikStories.class))
                    .withFailureTrace(true)
                    .withFailureTraceCompression(true)
                    .withDefaultFormats()
                    .withFormats(formats);

    private Embedder embedder = new Embedder();

    public SputnikStories() {
        useEmbedder(embedder);
    }

    public static void main(String[] args) {
        new SputnikStories().embedder.runAsEmbeddables(Arrays.asList(SputnikStories.class.getCanonicalName()));
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .usePendingStepStrategy(pendingStepStrategy)
                .useStoryLoader(new LoadFromClasspath(getClass().getClassLoader()))
                .useStoryReporterBuilder(reporterBuilder);
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new ScanningStepsFactory(configuration(), getClass());
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder()
                .findPaths(codeLocationFromClass(this.getClass()).getFile(), asList("**/*.story", "*.story"), null);
    }
}
