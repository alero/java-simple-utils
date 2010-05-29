package test.org.hrodberaht.inject;

import org.hrodberaht.inject.creators.SimpleContainerInstanceCreator;
import test.org.hrodberaht.inject.testservices.AnyService;
import test.org.hrodberaht.inject.testservices.AnyServiceDoSomethingImpl;


/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-23 16:00:58
 * @version 1.0
 * @since 1.0
 */
public class SimpleContainerInstanceCreatorImpl implements SimpleContainerInstanceCreator {



    @Override
    public <T> T getService(Class<T> service) {
        return (T) new AnyServiceDoSomethingImpl();
    }

    @Override
    public boolean supportServiceCreation(Class service) {
        return service.isAssignableFrom(AnyService.class);
    }

    @Override
    public boolean supportForcedInstanceScope() {
        return false;
    }
}
