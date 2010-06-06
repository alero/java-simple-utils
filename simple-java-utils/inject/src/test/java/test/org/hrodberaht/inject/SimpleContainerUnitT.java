/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

package test.org.hrodberaht.inject;

import org.hrodberaht.inject.Container;
import org.hrodberaht.inject.InjectRuntimeException;
import org.hrodberaht.inject.InjectionRegisterJava;
import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.SimpleInjection;
import org.junit.Before;
import org.junit.Test;
import test.org.hrodberaht.inject.testservices.AnyService;
import test.org.hrodberaht.inject.testservices.AnyServiceDoNothingImpl;
import test.org.hrodberaht.inject.testservices.AnyServiceDoSomethingImpl;

import static org.junit.Assert.assertEquals;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class SimpleContainerUnitT {

    @Before
    public void init() {

    }

    @Test
    public void testNothingRegistered() {        
        Container container = new InjectionRegisterJava().getContainer();
        try {
            AnyService anyService = container.get(AnyService.class);
            assertEquals(null, "Should not be called");
        } catch (InjectRuntimeException e) {
            assertEquals(
                    "Service interface " + AnyService.class.getName() +
                            " not registered in SimpleInjection"
                    , e.getMessage());
        }
    }

    @Test
    public void testNothingServiceWrapping() {
        Container container = registerSingle(AnyService.class, AnyServiceDoNothingImpl.class);

        AnyService anyService = container.get(AnyService.class);

        assertEquals(null, anyService.getStuff());
    }



    @Test
    public void testNamedNothingServiceWrapping() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.register("myAnyService", AnyService.class, AnyServiceDoNothingImpl.class);
        Container container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class, "myAnyService");

        assertEquals(null, anyService.getStuff());
    }

    @Test
    public void testSomethingServiceWrapping() {
        Container injection = registerSingle(AnyService.class, AnyServiceDoSomethingImpl.class);
        AnyService anyService = injection.get(AnyService.class);

        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());
    }

    @Test
    public void testSomethingServiceNewObjectSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class, SimpleInjection.Scope.SINGLETON);
        ScopeContainer container = registerJava.getScopedContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

        AnyService anyServiceSingleton = container.get(AnyService.class);
        assertEquals(1, anyServiceSingleton.getStuff().size());

        AnyService anyServiceNew = container.getNew(AnyService.class);
        assertEquals(0, anyServiceNew.getStuff().size());
    }

    @Test
    public void testSomethingServiceSingletonObjectSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
        ScopeContainer container = registerJava.getScopedContainer();

        AnyService anyService = container.getSingleton(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

        AnyService anyServiceSingleton = container.getSingleton(AnyService.class);
        assertEquals(1, anyServiceSingleton.getStuff().size());

        AnyService anyServiceNew = container.get(AnyService.class);
        assertEquals(0, anyServiceNew.getStuff().size());
    }

    @Test
    public void testReRegisterSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.register(AnyService.class, AnyServiceDoNothingImpl.class);
        registerJava.reRegister(AnyService.class, AnyServiceDoSomethingImpl.class);

        Container container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test
    public void testDefaultRegisterSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.registerDefault(AnyService.class, AnyServiceDoNothingImpl.class);
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
        Container container = registerJava.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test(expected = InjectRuntimeException.class)
    public void testFinalRegisterSupport() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.finalRegister(AnyService.class, AnyServiceDoNothingImpl.class);
        registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);

    }

    @Test
    public void testNormalRegisterFail() {

        try {
            InjectionRegisterJava registerJava = new InjectionRegisterJava();
            registerJava.register(AnyService.class, AnyServiceDoNothingImpl.class);
            registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
            assertEquals("Not suppose to execute this", "So fail");
        } catch (InjectRuntimeException e) {
            assertEquals(
                    "Service interface " + AnyService.class.getName() +
                            " is already registered, to override register please use the override method"
                    , e.getMessage());
        }


    }


    private Container registerSingle(Class anInterface, Class aService) {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.register(anInterface, aService);
        Container injection = registerJava.getContainer();
        return injection;
    }

}