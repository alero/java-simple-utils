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

package test.org.hrodberaht.inject.annotation;


import org.hrodberaht.inject.Container;
import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.InjectionRegisterModule;
import org.hrodberaht.inject.InjectionRegisterScan;
import org.hrodberaht.inject.internal.annotation.InjectionFinder;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.inject.register.InjectionRegister;
import org.hrodberaht.inject.register.RegistrationModuleAnnotation;
import org.hrodberaht.inject.spi.InjectionInstanceCreator;
import org.hrodberaht.inject.spi.InjectionPointFinder;
import org.junit.Test;
import test.org.hrodberaht.inject.testservices.annotated.Car;
import test.org.hrodberaht.inject.testservices.annotated.Spare;
import test.org.hrodberaht.inject.testservices.annotated.SpareTire;
import test.org.hrodberaht.inject.testservices.annotated.SpareVindShield;
import test.org.hrodberaht.inject.testservices.annotated.SpecialSpareTire;
import test.org.hrodberaht.inject.testservices.annotated.TestDriverManager;
import test.org.hrodberaht.inject.testservices.annotated.Tire;
import test.org.hrodberaht.inject.testservices.annotated.VindShield;
import test.org.hrodberaht.inject.testservices.annotated.Volvo;
import test.org.hrodberaht.inject.testservices.annotated.VolvoFactory;
import test.org.hrodberaht.inject.testservices.regmodules.CustomInjectionPointFinder;
import test.org.hrodberaht.inject.testservices.regmodules.OverridesRegisterModuleAnnotated;
import test.org.hrodberaht.inject.testservices.regmodules.RegisterModuleAnnotated;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 17:02:46
 * @version 1.0
 * @since 1.0
 */
public class AnnotationContainerUnitTForExternalManagedBeans {


    @Test
    public void testFindAnnotatedWithForTwoDifferentServices() {

        InjectionRegister registerJava = AnnotationContainerUtil.prepareVolvoRegister();
        Container container = registerJava.getContainer();
        Tire spareTire = container.get(Tire.class, Spare.class);
        VindShield vindShield = container.get(VindShield.class, Spare.class);

        assertTrue(spareTire instanceof SpareTire);

        assertTrue(vindShield instanceof SpareVindShield);

        // Prepare empty register
        InjectionFinder finder = new CustomInjectionPointFinder();
        InjectionPointFinder.setInjectionFinder(finder);


        Volvo aCar = (Volvo) container.get(Car.class);
        assertNotNull("getSpecialInjectField is null", aCar.getSpecialInjectField());
        assertNotNull("getSpecialInjectMethod is null", aCar.getSpecialInjectMethod());
        assertEquals("Initialized Text", aCar.getInitText());
        assertEquals("Initialized special", aCar.getDriverManager().getInitTextSpecial());

    }


}