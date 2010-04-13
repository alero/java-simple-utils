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
    Object createInstance() throws InstantiationException;
}
