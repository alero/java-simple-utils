package org.hrodberaht.inject.creators;

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
     * This will return the service that implements the intended interface has "wired" to it.
     * @param service a service interface
     * @return a service that is created using a instance creator          
     */
    <T> T getService(Class<T> service);

    /**
     * Indicates is the service is registered at the instance creator or not.
     * This way its possible to have a custom registry using SimpleContainerInstanceCreator
     * but still supporting the normal SimpleContainer registry, the SimpleContainerInstanceCreator
     * is evaluated before local registry is checked in service, so its possible to "override" existing services this way as well.
     * @param service the Interface intended for service "lookup"
     * @return true = is registered, false = is not registered
     */
    boolean supportServiceCreation(Class service);

    /**
     * Can the custom registry support forced scopes, will throw a RuntimeException if new/singleton methods are called when support is disabled
     * @return true = will be able to support forced scope, false = no support
     * @see org.hrodberaht.inject.SimpleInjection#getNew(Class) this method is affected by support
     * @see org.hrodberaht.inject.SimpleInjection#getSingleton(Class) this method is affected by support
     */
    boolean supportForcedInstanceScope();
}
