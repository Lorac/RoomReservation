package ca.ulaval.ift6002.sputnik.uat.steps.state;

import org.jbehave.core.annotations.BeforeScenario;

import java.util.HashMap;
import java.util.Map;

public class StatefulStep<T extends StepState> {

    private static ThreadLocal<Map<Class<?>, Object>> perThreadState = new ThreadLocal<>();

    static {
        perThreadState.set(new HashMap<>());
    }

    // Unfortunatly this cannot be abstract, JBehave needs to create
    // the class to run the @BeforeScenario
    protected T getInitialState() {
        return null;
    }

    @BeforeScenario
    public void createState() {
        T initialState = getInitialState();
        if (initialState != null) {
            perThreadState.get().put(getClass(), initialState);
        }
    }

    @SuppressWarnings("unchecked")
    protected T state() {
        return (T) perThreadState.get().get(getClass());
    }
}
