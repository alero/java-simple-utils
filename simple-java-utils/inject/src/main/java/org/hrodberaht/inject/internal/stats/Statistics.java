package org.hrodberaht.inject.internal.stats;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-aug-04 22:36:08
 * @version 1.0
 * @since 1.0
 */
public class Statistics {

    private static AtomicLong newInstanceCount = new AtomicLong(0L);
    private static AtomicLong registerServicesCount = new AtomicLong(0L);

    private static boolean enabled = false;

    public static Long getNewInstanceCount() {
        return newInstanceCount.get();
    }

    public static Long getRegisterServicesCount() {
        return registerServicesCount.get();
    }

    public static void addRegisterServicesCount() {
        if (enabled) {
            registerServicesCount.incrementAndGet();
        }
    }

    public static void addNewInstanceCount() {
        if (enabled) {
            newInstanceCount.incrementAndGet();
        }
    }

    public static void setEnabled(boolean enabled) {
        Statistics.enabled = enabled;
        // Reset
        if(!Statistics.enabled){
            newInstanceCount = new AtomicLong(0L);
            registerServicesCount = new AtomicLong(0L); 
        }
    }
}
