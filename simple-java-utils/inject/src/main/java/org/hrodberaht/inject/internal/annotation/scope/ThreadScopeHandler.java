package org.hrodberaht.inject.internal.annotation.scope;

import org.hrodberaht.inject.SimpleInjection;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-06 02:45:49
 * @version 1.0
 * @since 1.0
 */
public class ThreadScopeHandler implements ScopeHandler{

    private ThreadLocal<Object> placeHolder = new ThreadLocal<Object>();

    @Override
    public Object getInstance() {
        return placeHolder.get();
    }

    @Override
    public void addScope(Object instance) {
        placeHolder.set(instance);
    }

    @Override
    public SimpleInjection.Scope getScope() {
        return SimpleInjection.Scope.THREAD;
    }
}