package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.SPIRuntimeException;

import javax.inject.Named;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-28 21:50:56
 * @version 1.0
 * @since 1.0
 */
public class AnnotationQualifierUtil {

    private static final Class<Qualifier> QUALIFIER = Qualifier.class;

    public static String getQualifierName(final Object owner, final Annotation[] annotations) {
        final List<String> qualifierAnnotations = new ArrayList<String>();

        for (final Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(QUALIFIER)) {
                qualifierAnnotations.add(getQualifier(owner, annotation));
            }
        }
        if (qualifierAnnotations.size() == 0) {
            return null;
        } else if (qualifierAnnotations.size() > 1) {
            throw new SPIRuntimeException("Several qualifier annotations found on " + owner + " " + qualifierAnnotations);
        }

        return qualifierAnnotations.get(0);
    }

    private static String getQualifier(final Object owner, final Annotation annotation) {
        if (annotation instanceof Named) {
            final Named named = (Named) annotation;
            final String value = named.value();

            if (isEmpty(value)) {
                throw new SPIRuntimeException("Named qualifier annotation used without a value on " + owner);
            }

            return value;
        } else {
            return annotation.annotationType().getSimpleName();
        }
    }

    private static boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }

}
