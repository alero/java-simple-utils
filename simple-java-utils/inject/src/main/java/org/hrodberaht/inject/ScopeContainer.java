package org.hrodberaht.inject;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-06 02:06:55
 * @version 1.0
 * @since 1.0
 */
public interface ScopeContainer extends Container {

    public enum Scope {
        SINGLETON, NEW, THREAD, INHERITABLE_THREAD
    }

    <T> T getNew(Class<T> service);
    <T> T getSingleton(Class<T> service);
}
