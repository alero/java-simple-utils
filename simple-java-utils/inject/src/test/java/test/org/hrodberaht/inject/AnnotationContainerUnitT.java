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
import org.hrodberaht.inject.InjectionRegisterJava;
import org.hrodberaht.inject.register.annotation.AnnotationRegistrationModule;
import org.junit.Test;
import test.org.hrodberaht.inject.testservices.annotated.Spare;
import test.org.hrodberaht.inject.testservices.annotated.SpareTire;
import test.org.hrodberaht.inject.testservices.annotated.Tire;

import static org.junit.Assert.assertTrue;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 17:02:46
 * @version 1.0
 * @since 1.0
 */
public class AnnotationContainerUnitT {


    @Test
    public void testFindAnnotatedWithSpecial() {

        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerJavaXInject();
        registerJava.register(new AnnotationRegistrationModule(){
            @Override
            public void registrations() {
                register(Tire.class).annotated(Spare.class).with(SpareTire.class);
            }
        });

        Container container = registerJava.getContainer();

        Tire spareTire = container.get(Tire.class, Spare.class);

        assertTrue(spareTire instanceof SpareTire);
    }


}