package org.alex.rmi;

import org.alex.logging.SimpleLogger;

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
    private static String initialContextFactory = null;

    private static void init() {
        Hashtable<String, String> env = new Hashtable<String, String>();
        if (initialContextFactory != null) {
            env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        } else {
            env.put(Context.INITIAL_CONTEXT_FACTORY, RMILookUpConfig.CONNECTIONFACTORY.getValue());
        }
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
        LOGGER.debug("looking up {0}", theClass);
        
        Object anEJBHome = PortableRemoteObject.narrow(ctx.lookup(jndiName), theClass);
        return anEJBHome;
    }

}
