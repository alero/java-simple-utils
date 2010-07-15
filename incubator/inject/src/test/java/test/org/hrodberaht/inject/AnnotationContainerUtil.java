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

import org.atinject.tck.auto.Car;
import org.atinject.tck.auto.Convertible;
import org.atinject.tck.auto.Drivers;
import org.atinject.tck.auto.DriversSeat;
import org.atinject.tck.auto.Engine;
import org.atinject.tck.auto.Seat;
import org.atinject.tck.auto.Tire;
import org.atinject.tck.auto.V8Engine;
import org.atinject.tck.auto.accessories.SpareTire;
import org.hrodberaht.inject.Container;
import org.hrodberaht.inject.InjectionRegisterJava;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:51
 * @version 1.0
 * @since 1.0
 */
public class AnnotationContainerUtil {

    public static Container prepareRegister() {
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerJavaXInject();

        registerJava.register(Car.class, Convertible.class);
        registerJava.register(Engine.class, V8Engine.class);
        // InjectionRegisterJava.register(Cupholder.class);
        registerJava.register("spare", Tire.class, SpareTire.class);
        // InjectionRegisterJava.register(FuelTank.class);
        // InjectionRegisterJava.register(Seat.class);
        registerJava.register(Drivers.class, Seat.class, DriversSeat.class);

        return registerJava.getContainer();
    }
}
