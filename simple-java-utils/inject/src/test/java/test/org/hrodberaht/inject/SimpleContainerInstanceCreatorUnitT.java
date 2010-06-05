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


import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-23 15:59:57
 * @version 1.0
 * @since 1.0
 */
public class SimpleContainerInstanceCreatorUnitT {



    @Before
    public void init(){
        // InjectionRegisterJava.activateContainerDefault();
        // InjectionRegisterJava.registerInstanceCreator(new SimpleContainerInstanceCreatorImpl());
    }

    @Test
    public void dummy(){
        assertEquals("",""); 
    }

    /*@Test
    public void testInstanceCreatorRegister(){
        AnyService anyService = SimpleInjection.get(AnyService.class);
        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());
    }*/

    /*@Test
    public void testInstanceCreatorRegisterInstanceScope(){
        try{
            AnyService anyService = SimpleInjection.getNew(AnyService.class);

            assertEquals("Not allowed to reach this statement", null);
        }catch (InjectRuntimeException e){
            assertEquals("Can not use forced scope for service interface " +
                    AnyService.class.getName(),
                    e.getMessage());
        }

    } */

}
