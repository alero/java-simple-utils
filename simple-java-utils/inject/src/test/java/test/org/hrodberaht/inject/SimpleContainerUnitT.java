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
 * @version 1.0
 * @since 1.0
 */
public class SimpleContainerUnitT {

    @Before
    public void init() {
        InjectionRegisterJava.resetContainerToDefault();
    }

    @Test
    public void testNothingRegistered() {

        try {
            AnyService anyService = SimpleInjection.get(AnyService.class);
            assertEquals(null, "Should not be called");
        } catch (SPIRuntimeException e) {
            assertEquals(
                    "Service interface " + AnyService.class.getName() +
                            " not registered in SimpleInjection"
                    , e.getMessage());
        }
    }

    @Test
    public void testNothingServiceWrapping() {
        InjectionRegisterJava.register(AnyService.class, AnyServiceDoNothingImpl.class);

        AnyService anyService = SimpleInjection.get(AnyService.class);

        assertEquals(null, anyService.getStuff());
    }

    @Test
    public void testSomethingServiceWrapping() {
        InjectionRegisterJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);

        AnyService anyService = SimpleInjection.get(AnyService.class);

        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());
    }

    @Test
    public void testSomethingServiceNewObjectSupport() {
        InjectionRegisterJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);

        AnyService anyService = SimpleInjection.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

        AnyService anyServiceSingleton = SimpleInjection.get(AnyService.class);
        assertEquals(1, anyServiceSingleton.getStuff().size());

        AnyService anyServiceNew = SimpleInjection.getNew(AnyService.class);
        assertEquals(0, anyServiceNew.getStuff().size());
    }

    @Test
    public void testSomethingServiceSingletonObjectSupport() {
        InjectionRegisterJava.register(AnyService.class, AnyServiceDoSomethingImpl.class, SimpleInjection.Scope.NEW);

        AnyService anyService = SimpleInjection.getSingleton(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

        AnyService anyServiceSingleton = SimpleInjection.getSingleton(AnyService.class);
        assertEquals(1, anyServiceSingleton.getStuff().size());

        AnyService anyServiceNew = SimpleInjection.get(AnyService.class);
        assertEquals(0, anyServiceNew.getStuff().size());
    }

    @Test
    public void testReRegisterSupport() {
        InjectionRegisterJava.register(AnyService.class, AnyServiceDoNothingImpl.class);

        InjectionRegisterJava.reRegister(AnyService.class, AnyServiceDoSomethingImpl.class);

        AnyService anyService = SimpleInjection.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test
    public void testDefaultRegisterSupport() {
        InjectionRegisterJava.registerDefault(AnyService.class, AnyServiceDoNothingImpl.class);
        InjectionRegisterJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);

        AnyService anyService = SimpleInjection.get(AnyService.class);
        anyService.doStuff();
        assertEquals(1, anyService.getStuff().size());

    }

    @Test(expected = SPIRuntimeException.class)
    public void testFinalRegisterSupport() {

        InjectionRegisterJava.finalRegister(AnyService.class, AnyServiceDoNothingImpl.class);
        InjectionRegisterJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);

    }

    @Test
    public void testNormalRegisterFail() {

        try {
            InjectionRegisterJava.register(AnyService.class, AnyServiceDoNothingImpl.class);
            InjectionRegisterJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
            assertEquals("Not suppose to execute this", "So fail");
        } catch (SPIRuntimeException e) {
            assertEquals(
                    "Service interface " + AnyService.class.getName() +
                            " is already registered, to override register please use the override method"
                    , e.getMessage());
        }


    }

}