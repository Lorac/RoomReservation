package ca.ulaval.ift6002.sputnik.applicationservice.shared.locator;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ServiceLocatorTest {

    @Before
    public void clearServiceLocator() {
        ServiceLocator.reset();
    }

    @Test(expected = UnregisteredServiceException.class)
    public void cannotResolveAServiceThatIsNotRegistered() {
        ServiceLocator.getInstance().resolve(SampleService.class);
    }

    @Test(expected = DoubleServiceRegistrationException.class)
    public void cannotRegisterTheSameServiceTwice() {
        ServiceLocator.getInstance().register(SampleService.class, new SampleImplementation());
        ServiceLocator.getInstance().register(SampleService.class, new SampleImplementation());
    }

    @Test
    public void canResolveAServiceWhenRegisteredAsSingleton() {
        ServiceLocator.getInstance().register(SampleService.class, new SampleImplementation());

        SampleService implementation = ServiceLocator.getInstance().resolve(SampleService.class);

        assertThat(implementation, instanceOf(SampleImplementation.class));
    }

    @Test(expected = UnregisteredServiceException.class)
    public void canUnregisterAServiceThatWasRegistered() {
        ServiceLocator.getInstance().register(SampleService.class, new SampleImplementation());

        ServiceLocator.getInstance().unregister(SampleService.class);

        ServiceLocator.getInstance().resolve(SampleService.class);
    }

    private interface SampleService {

    }

    private class SampleImplementation implements SampleService {

    }
}
