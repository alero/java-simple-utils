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
import org.hrodberaht.inject.SimpleInjection;
import org.junit.Test;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:23
 * @version 1.0
 * @since 1.0
 */
public class AnnotationContinerPerformanceUnitT {

    @Test(timeout = 10000)
    public void testPerformance(){
        AnnotationContainerUtil.prepareRegister();
        for(int i=0;i<1000;i++){
            Car car = SimpleInjection.get(Car.class);
            // This does loads of fetching from the container, will stress test it a lot.
            // Form what i could see on the Cobertura report each rotation give about 100 calls.
            // meaning this will test about 100 000 calls to the SimpleInjection.get method.

            // On my machine an Intel i7 820 this takes about 0.4 seconds using 1 of 4 CPU's at 75%.
            // This is not strange as this test is not threaded in any way. 
            Tck.testsFor(car, false, true);
        }
    }
}
