package test.org.alex.config;

import org.alex.config.ConfigBase;
import org.alex.config.ConfigItem;
import org.alex.config.MasterConfig;
import org.alex.util.StringUtil;

import java.text.ParseException;
import java.util.Date;


/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class AnyApplicationConfig extends ConfigBase {
    private static final String DEFAULT_CONFIG = "classpath:/test/org/alex/config/basicConfig.properties";

    public static void initConfig() throws ParseException {
        initConfig(DEFAULT_CONFIG);
    }

    public static void initConfig(String resource) throws ParseException {
        String externalConfig = System.getProperty("config.externalfile");
        if(!StringUtil.isBlank(externalConfig)){
            new AnyApplicationConfig(resource, externalConfig);
        }else{
            new AnyApplicationConfig(resource, null);
        }

    }


    public interface ApplicationState {
        ConfigItem<Boolean> A_BOOLEAN = new ConfigItem<Boolean>(Boolean.class, "anyapp.aboolean");
        ConfigItem<String> A_STRING = new ConfigItem<String>(String.class, "anyapp.astring");
        ConfigItem<Date> A_DATE = new ConfigItem<Date>(Date.class, "anyapp.adate");
        ConfigItem<Integer> A_INTEGER = new ConfigItem<Integer>(Integer.class, "anyapp.ainteger");
        ConfigItem<Long> A_LONG = new ConfigItem<Long>(Long.class, "anyapp.along");
    }


    private AnyApplicationConfig(String propertyPath, String customPropertyPath) throws ParseException {
        MasterConfig.registerConfig(this, propertyPath, customPropertyPath);
    }


}