package test.org.hrodberaht.inject;


import org.hrodberaht.inject.InjectionRegisterJava;
import org.hrodberaht.inject.SPIRuntimeException;
import org.hrodberaht.inject.SimpleInjection;
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
        InjectionRegisterJava.resetContainerToDefault();
        InjectionRegisterJava.registerInstanceCreator(new SimpleContainerInstanceCreatorImpl());
    }

    @Test
    public void testInstanceCreatorRegister(){
        AnyService anyService = SimpleInjection.get(AnyService.class);
        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());
    }

    @Test
    public void testInstanceCreatorRegisterInstanceScope(){
        try{
            AnyService anyService = SimpleInjection.getNew(AnyService.class);

            assertEquals("Not allowed to reach this statement", null);
        }catch (SPIRuntimeException e){
            assertEquals("Can not use forced scope for service interface " +
                    AnyService.class.getName(), 
                    e.getMessage());
        }

    }

}
