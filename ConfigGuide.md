# Introduction #

Java Utilities is called core and is synced to Maven Central (no third party repository is needed)
```
<dependency>
    <groupId>org.hrodberaht.directus</groupId>
    <artifactId>core</artifactId>
    <version>1.2.0</version>
</dependency>

```

## Config (extension of java properties file) ##
The Configuration is intended as a extension of the Java Properties support.
JSE version lacks current development and usability so the following has been added.

  1. typed properties via Java Generics
  1. 3 way Inheritable properties via default/custom/system loaders.
  1. Fully automated discovery of Config classes via Reflection, all that is needed is single line registration of config class.


## Utilities (Util classes) ##

DateUtil the Date utility. if you are unable to use yodatime or some other useful date management framework, alot of these ease of use methods are really helpful.

String util, for the ever repeated isBlank method.

# Details #

## Config (extension of java properties file) ##

**Typed properties**

The properties are typed via a Class that can hold name, value and type. See ConfigItem.

```
public class AnyApplicationConfig extends ConfigBase {

    public static ConfigItem<Boolean> A_BOOLEAN = new ConfigItem<Boolean>(Boolean.class, "anyapp.aboolean");
    public static ConfigItem<Boolean> IS_ENABLED = new ConfigItem<Boolean>(Boolean.class, "anyapp.theboolean", true);

    public interface ApplicationState {

        ConfigItem<String> A_STRING = new ConfigItem<String>(String.class, "anyapp.astring");
        ConfigItem<Date> A_DATE = new ConfigItem<Date>(Date.class, "anyapp.adate");
        ConfigItem<Integer> A_INTEGER = new ConfigItem<Integer>(Integer.class, "anyapp.ainteger");
        ConfigItem<Long> A_LONG = new ConfigItem<Long>(Long.class, "anyapp.along");
    }

```

**Inheritable configuration**

It is possible to set the value of a configuration in your application with three different population strategies.
  * A default one, see propertyPath (currently only properties files are supported).
  * After that a custom property file can be added, this will be appended to the default (i.e it will inherit/overwrite the value). See customPropertyPath
  * The last and "strongest" way of setting a configuration value is via System properties. System.setProperty("key","value");

```
ConfigRegister.registerConfig(this, propertyPath, customPropertyPath);
```
**Easy registration**

The registration is done using using ConfigRegister.registerConfig, the first parameter must be an object containing the ConfigItems that will be evaluated and filled with data according to the Inheritance/Population rules described above.

```
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

    private AnyApplicationConfig(String propertyPath, String customPropertyPath) throws ParseException {
        ConfigRegister.registerConfig(this, propertyPath, customPropertyPath);
    }
```