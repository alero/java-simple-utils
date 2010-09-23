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

import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.hrodberaht.inject.Container;
import org.hrodberaht.inject.internal.stats.Statistics;
import org.hrodberaht.inject.register.InjectionRegister;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import test.org.hrodberaht.inject.testservices.annotated.Volvo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:23
 * @version 1.0
 * @since 1.0
 */
@Category({PerformanceTests.class})
public class AnnotationContinerPerformanceUnitT {

    private InjectionRegister registerVolvo;

    @Before
    public void init() {
        registerVolvo = AnnotationContainerUtil.prepareLargeVolvoRegister();
        Statistics.setEnabled(true);
    }

    @After
    public void destroy(){
        Statistics.setEnabled(false);
    }

    @Test(timeout = 10000)
    public void testPerformance() {
        Container container = TckUtil.prepareRegister().getContainer();
        for (int i = 0; i < 10000; i++) {
            Car car = container.get(Car.class);
            // This does loads of fetching from the container, will stress test it a lot.
            // Form what i could see on the Cobertura report each rotation give about 100 calls.
            // meaning these 1 000 iterations will test about 100 000 calls to the SimpleInjection.getInnerContainer method.

            // On my machine an 10 000 Intel i7 820 this takes about 2 seconds using 1 of 4 CPU's at 75%.
            // This is not strange as this test is not threaded in any way. 
            Tck.testsFor(car, false, true);
        }
    }

    @Test(timeout = 30000)
    public void testMultiThreadPerformance() {


        final Container container = registerVolvo.getContainer();

        Collection<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 500; i++) {
            threads.add(
                    new Thread() {
                        @Override
                        public void run() {
                            runThreadContainerGet(container);
                        }
                    }
            );
        }
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            waitForIt(thread);
        }

        System.out.println(
                MessageFormat.format("Created objects: {0}",Statistics.getNewInstanceCount())
        );
        System.out.println(
                MessageFormat.format("Injected field count: {0}",Statistics.getInjectFieldCount())
        );
        System.out.println(
                MessageFormat.format("Injected method count: {0}",Statistics.getInjectMethodCount())
        );
        System.out.println(
                MessageFormat.format("Injected construct count: {0}",Statistics.getInjectConstructorCount())
        );        
    }

    private void waitForIt(Thread thread) {
        try {
            while (thread.isAlive())
                Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void runThreadContainerGet(Container container) {
        for (int i = 0; i < 25000; i++) {
            Volvo car = container.get(Volvo.class);
            AnnotationContainerUtil.assertVolvo(car);
        }
    }

}
