/*
 * Copyright (c) 2010.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

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
 * @version 1.0
 * @since 1.0
 */
public class TestSimpleContainer {

    @Before
    public void init(){
        JavaContainerRegister.cleanRegister();
    }

    @Test
    public void testNothingServiceWrapping(){
        JavaContainerRegister.register(AnyService.class, AnyServiceDoNothingImpl.class);

        AnyService anyService = SimpleContainer.get(AnyService.class);

        assertEquals(null, anyService.getStuff());
    }

    @Test
    public void testSomethingServiceWrapping(){
        JavaContainerRegister.register(AnyService.class, AnyServiceDoSomethingImpl.class);

        AnyService anyService = SimpleContainer.get(AnyService.class);

        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());
    }

    @Test
    public void testSomethingServiceNewObjectSupport(){
        JavaContainerRegister.register(AnyService.class, AnyServiceDoSomethingImpl.class);

        AnyService anyService = SimpleContainer.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

        AnyService anyServiceSingleton = SimpleContainer.get(AnyService.class);
        assertEquals(1, anyServiceSingleton.getStuff().size());

        AnyService anyServiceNew = SimpleContainer.getNew(AnyService.class);        
        assertEquals(0, anyServiceNew.getStuff().size());
    }

    @Test
    public void testReRegisterSupport(){
        JavaContainerRegister.register(AnyService.class, AnyServiceDoNothingImpl.class);

        JavaContainerRegister.reRegister(AnyService.class, AnyServiceDoSomethingImpl.class);

        AnyService anyService = SimpleContainer.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test
    public void testDefaultRegisterSupport(){
        JavaContainerRegister.registerDefault(AnyService.class, AnyServiceDoNothingImpl.class);
        JavaContainerRegister.register(AnyService.class, AnyServiceDoSomethingImpl.class);

        AnyService anyService = SimpleContainer.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test (expected = MessageRuntimeException.class)
    public void testFinalRegisterSupport(){

        JavaContainerRegister.finalRegister(AnyService.class, AnyServiceDoNothingImpl.class);
        JavaContainerRegister.register(AnyService.class, AnyServiceDoSomethingImpl.class);

    }

    @Test
    public void testNormalRegisterFail(){

        try{
            JavaContainerRegister.register(AnyService.class, AnyServiceDoNothingImpl.class);
            JavaContainerRegister.register(AnyService.class, AnyServiceDoSomethingImpl.class);
            assertEquals("Not suppose to execute this", "So fail");
        }catch (MessageRuntimeException e){
            assertEquals(
                    "Service interface test.org.hrodberaht.directus.ioc.AnyService is already registered, to override register please use the override method"
                    , e.getMessage());
        }


    }

}
