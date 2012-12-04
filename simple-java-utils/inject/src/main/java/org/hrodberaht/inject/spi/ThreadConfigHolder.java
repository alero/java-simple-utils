package org.hrodberaht.inject.spi;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 *         2011-05-22 17:43
 * @created 1.0
 * @since 1.0
 */
public class ThreadConfigHolder {

    private static final ThreadLocal<ContainerConfig> threadLocal = new ThreadLocal<ContainerConfig>();


    public static ContainerConfig get(){
        return threadLocal.get();
    }

    public static void set(ContainerConfig configBase){
        threadLocal.set(configBase);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
