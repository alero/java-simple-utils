package org.hrodberaht.inject.internal.annotation;

import java.lang.reflect.Method;
import java.util.List;

/**
 * ¤Projectname¤
 *
 * @author Robert Alexandersson
 *         2010-sep-23 20:33:47
 * @version 1.0
 * @since 1.0
 */
public interface InjectionFinder {
    List<InjectionPoint> findInjectionPoints(Class service, AnnotationInjection annotationInjection);

    Method findPostConstruct(Class serviceClass);
}
