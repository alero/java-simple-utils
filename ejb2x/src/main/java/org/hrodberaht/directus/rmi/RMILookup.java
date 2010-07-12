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

package org.hrodberaht.directus.rmi;

import org.hrodberaht.directus.logging.SimpleLogger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.util.Hashtable;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class RMILookup {

    private static final SimpleLogger LOGGER = SimpleLogger.getInstance(RMILookup.class);
    private static Hashtable<String, String> env = null;

    private static void init() {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, RMILookUpConfig.CONNECTIONFACTORY.getValue());
        env.put(Context.PROVIDER_URL, RMILookUpConfig.LOCATION.getValue());
        env.put(Context.SECURITY_PRINCIPAL, RMILookUpConfig.USER.getValue());
        env.put(Context.SECURITY_CREDENTIALS, RMILookUpConfig.PASSWORD.getValue());
        RMILookup.env = env;
        
    }

    private RMILookup() {

    }

    public static Object findHome(Class theClass, String jndiName) throws NamingException {

        if (env == null ) {
            init();
        }
        LOGGER.debug("looking up PROVIDER_URL {0}", RMILookUpConfig.LOCATION.getValue());
        LOGGER.debug("looking up CONNECTIONFACTORY {0}", RMILookUpConfig.CONNECTIONFACTORY.getValue());
        InitialContext ctx = new InitialContext(env);
        LOGGER.debug("looking up {0} using jndiName {1}", theClass, jndiName);
        
        Object anEJBHome = PortableRemoteObject.narrow(ctx.lookup(jndiName), theClass);
        return anEJBHome;
    }

}
