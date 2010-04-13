package org.hrodberaht.directus.util.ioc;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-apr-13 20:37:58
 * @version 1.0
 * @since 1.0
 */
public interface SimpleContainerInstanceCreator {
    /**
     * /**
     * This will return the service that implements the intended interface
     * @param service a service interface
     * @return a service that is created using a instance creator          
     */
    Object getService(Class service);

    boolean supportServiceCreation(Class service);

    boolean supportForcedInstanceScope();
}
