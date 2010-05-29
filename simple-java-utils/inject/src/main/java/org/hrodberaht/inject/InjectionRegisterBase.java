package org.hrodberaht.inject;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-28 21:12:27
 * @version 1.0
 * @since 1.0
 */
public abstract class InjectionRegisterBase {

    public static void activateJavaXInjectCompliance() {
        SimpleInjection.setContainerInjectAnnotationCompliantMode();
    }

    public static void resetContainerToDefault() {
        SimpleInjection.resetContainerToDefault();
    }

}
