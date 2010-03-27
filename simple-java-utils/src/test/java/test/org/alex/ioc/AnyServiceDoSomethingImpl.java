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

package test.org.alex.ioc;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class AnyServiceDoSomethingImpl implements AnyService{

    private Collection<String> collection = new ArrayList<String>();  

    @Override
    public void doStuff() {
        collection.add("Added something");
    }

    @Override
    public Collection getStuff() {
        return collection;
    }
}