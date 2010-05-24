package test.org.hrodberaht.directus.ioc;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.directus.util.ioc.JavaContainerRegister;
import org.hrodberaht.directus.util.ioc.SimpleContainer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-23 15:59:57
 * @version 1.0
 * @since 1.0
 */
public class TestSimpleContainerInstanceCreator {

    @Before
    public void init(){
        JavaContainerRegister.cleanRegister();
        JavaContainerRegister.registerInstanceCreator(new SimpleContainerInstanceCreatorImpl());
    }

    @Test
    public void testInstanceCreatorRegister(){
        AnyService anyService = SimpleContainer.get(AnyService.class);
        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());
    }

    @Test
    public void testInstanceCreatorRegisterInstanceScope(){
        try{
            AnyService anyService = SimpleContainer.getNew(AnyService.class);

            assertEquals("Not allowed to reach this statement", null);
        }catch (MessageRuntimeException e){
            assertEquals("Can not use forced scope for service interface test.org.hrodberaht.directus.ioc.AnyService", e.getMessage());  
        }

    }

}
