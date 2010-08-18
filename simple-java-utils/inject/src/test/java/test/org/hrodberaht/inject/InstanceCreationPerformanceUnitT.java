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

import org.hrodberaht.inject.internal.annotation.creator.InstanceCreator;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreatorCGLIB;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreatorDefault;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import test.org.hrodberaht.inject.testservices.annotated.Tire;
import test.org.hrodberaht.inject.testservices.annotated.Volvo;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:23
 * @version 1.0
 * @since 1.0
 */
@Category({PerformanceTests.class})
public class InstanceCreationPerformanceUnitT {

    @Test(timeout = 10000)
    public void testPerformance() throws InterruptedException {
        Constructor constructor = Volvo.class.getConstructors()[0];


        // warmup
        runPerformanceTest(constructor, new InstanceCreatorCGLIB(), 10);

        int iterations = 3000000;

        System.gc();Thread.sleep(100L);
        System.out.println("TestRun with CGLIB - " +
                runPerformanceTest(constructor, new InstanceCreatorCGLIB(), iterations)+"ms");

        System.gc();Thread.sleep(100L);
        System.out.println("TestRun with java Reflection - " +
                runPerformanceTest(constructor, new InstanceCreatorDefault(), iterations)+"ms");
        
        System.gc();Thread.sleep(100L);
        System.out.println("TestRun with pure java - " +
                runPerformanceTest(null, null, iterations)+"ms");

    }

    private long runPerformanceTest(Constructor constructor, InstanceCreator instanceCreator, int iterations) {
        long startTime = new Date().getTime();
        Volvo[] volvos = new Volvo[iterations];
        for (int i=0;i<iterations;i++) {
            Tire spareTire = new Tire();
            if(constructor != null){
                Volvo aVolvo = (Volvo) instanceCreator.createInstance(constructor, spareTire);
                volvos[i] = aVolvo;
            }
            else {
                Volvo aVolvo = new Volvo(spareTire);
                volvos[i] = aVolvo;
            }
        }
        long endTime = new Date().getTime();
        return endTime-startTime;
    }
}