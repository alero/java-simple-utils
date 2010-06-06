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
public class DefaultScopeHandler implements ScopeHandler{

    @Override
    public Object getInstance() {
        return null;
    }

    @Override
    public void addScope(Object instance) {

    }

    @Override
    public SimpleInjection.Scope getScope() {
        return SimpleInjection.Scope.NEW;
    }
}