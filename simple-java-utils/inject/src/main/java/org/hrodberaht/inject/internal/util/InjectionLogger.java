package org.hrodberaht.inject.internal.util;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2013-09-13
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class InjectionLogger {

    private static boolean loggingEnabled = false;

    static{
        String value = System.getProperty("injection.logging.enabled");
        if(value != null){
            loggingEnabled = Boolean.parseBoolean(value);
        }
    }

    public static void info(String message, Object... vars){
        if(loggingEnabled){
            System.out.print("InjectionLogger - INFO - "+message);
        }
    }

    public static void error(String message, Object... vars){
        if(loggingEnabled){
            System.out.print("InjectionLogger - ERROR - "+message);
        }
    }

    public static void error(Exception e) {
        if(loggingEnabled){
            System.out.print("InjectionLogger - ERROR - ");
            e.printStackTrace(System.err);
        }
    }
}
