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
import org.hrodberaht.inject.InjectionRegisterScan;
import org.junit.Test;
import test.org.hrodberaht.inject.testservices.AnyService;
import test.org.hrodberaht.inject.testservices.AnyServiceDoNothingImpl;
import test.org.hrodberaht.inject.testservices.annotated.Car;
import test.org.hrodberaht.inject.testservices.annotated.Volvo;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 17:02:46
 * @version 1.0
 * @since 1.0
 */
public class ContainerScanUnitT {


    @Test
    public void testScanningOfImplementations() {

        InjectionRegisterScan register = new InjectionRegisterScan();
        // Tests scanning and exclusion of single class
        register.registerBasePackageScan("test.org.hrodberaht.inject.testservices", AnyServiceDoNothingImpl.class);
        Container container = register.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());

    }


    @Test
    public void testAnnotatedScanningOfImplementations() {
        InjectionRegisterScan register = new InjectionRegisterScan();
        register.activateContainerJavaXInject();
        // Tests scanning and exclusion of single class
        register.registerBasePackageScan("test.org.hrodberaht.inject.testservices.annotated");
        Container container = register.getContainer();
        Car aCar = container.get(Car.class);

        assertEquals("volvo", aCar.brand());
        Volvo aVolvo = (Volvo)aCar;
        assertNotNull(aVolvo.getBackLeft());
        assertNotNull(aVolvo.getBackRight());
        assertNotNull(aVolvo.getFrontLeft());
        assertNotNull(aVolvo.getFrontRight());
        assertNotNull(aVolvo.getSpareTire());
        assertNotNull(aVolvo.getVindShield());

    }

    @Test
    public void testScanningOfSinglePackageImplementations() {

        InjectionRegisterScan register = new InjectionRegisterScan();
        // Tests scanning and exclusion of single class
        register.registerBasePackageScan("test.org.hrodberaht.inject.testservices", AnyServiceDoNothingImpl.class);
        Container container = register.getContainer();

        AnyService anyService = container.get(AnyService.class);
        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());

    }
}
