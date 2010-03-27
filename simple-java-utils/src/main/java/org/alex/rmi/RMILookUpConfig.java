package org.alex.rmi;

import org.alex.config.ConfigItem;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class RMILookUpConfig {

    public static final ConfigItem<String> CONNECTIONFACTORY = new ConfigItem<String>(String.class, "rmi.connectionfactory");
    public static final ConfigItem<String> LOCATION = new ConfigItem<String>(String.class, "rmi.location");
    public static final ConfigItem<String> USER = new ConfigItem<String>(String.class, "rmi.username");
    public static final ConfigItem<String> PASSWORD = new ConfigItem<String>(String.class, "rmi.passwork");


}
