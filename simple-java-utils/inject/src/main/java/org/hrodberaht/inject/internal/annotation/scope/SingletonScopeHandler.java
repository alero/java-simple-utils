package org.hrodberaht.inject.internal.annotation.scope;

import org.hrodberaht.inject.SimpleInjection;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-06 02:45:49
 * @version 1.0
 * @since 1.0
 */
public class SingletonScopeHandler implements ScopeHandler{

    private Object singleton;

    @Override
    public Object getInstance() {
        return singleton;
    }

    @Override
    public void addScope(Object instance) {
        singleton = instance;
    }

    @Override
    public SimpleInjection.Scope getScope() {
        return SimpleInjection.Scope.SINGLETON;
    }
}
