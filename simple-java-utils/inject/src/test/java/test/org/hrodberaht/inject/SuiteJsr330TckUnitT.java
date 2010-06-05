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

import junit.framework.TestCase;
import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.atinject.tck.auto.Convertible;
import org.atinject.tck.auto.Drivers;
import org.atinject.tck.auto.DriversSeat;
import org.atinject.tck.auto.Engine;
import org.atinject.tck.auto.Seat;
import org.atinject.tck.auto.Tire;
import org.atinject.tck.auto.V8Engine;
import org.atinject.tck.auto.accessories.SpareTire;
import org.hrodberaht.inject.InjectionRegisterJava;
import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.creators.annotation.AnnotationRegistrationModule;


/**
 * Simple Java Utils - Injection
 *
 * @author Robert Alexandersson
 *         2010-maj-28 19:27:43
 * @version 1.0
 * @since 1.0
 */

public class SuiteJsr330TckUnitT extends TestCase {

    public static junit.framework.Test suite() {

        InjectionRegisterJava.activateInternalJavaXInjectAnnotations();

        // InjectionRegisterJava.register(Car.class, Convertible.class);
        // InjectionRegisterJava.register(Engine.class, V8Engine.class);
        // InjectionRegisterJava.register("spare", SpareTire.class);
        // InjectionRegisterJava.register(Drivers.class, DriversSeat.class);

        AnnotationRegistrationModule module = new AnnotationRegistrationModule(){
            @Override
            public void registrations() {
                register(Car.class).with(Convertible.class);
                register(Engine.class).with(V8Engine.class);
                register(Tire.class).namned("spare").with(SpareTire.class);
                register(Seat.class).annotated(Drivers.class).with(DriversSeat.class);
            }
        };

        InjectionRegisterJava.register(module);

        final Car car = SimpleInjection.get(Car.class);
        final boolean supportsStatic = false;
        final boolean supportsPrivate = true;

        return Tck.testsFor(car, supportsStatic, supportsPrivate);

    }
}
