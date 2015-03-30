# java-simple-utils
Automatically exported from code.google.com/p/java-simple-utils

A small project that is a collection of the following mini frameworks.

- i18n - Formatting(extends java.text.Format) and Locale Use to transform Objects to Strings and Strings to Objects. Uses a well configured and more usable version of the format package than standard Formatting classes provide. See wiki

- Daily Developer Utilities (Date, String, I/O) DateUtil?, StringUtil?, SocketCloser?, NumberUtil? All classes have null return instead of nullpointer when parsing.

- Logging A wrapper around a logging implementation, not really that useful anymore as slf4j is really good, was more useful when it wrapped Commons or Log4j.

- Config (a generics based version of the way to simple java properties) This configuration adds some small things to java properties, see Wiki

- IoC, a very minimalistic Container. Has been created as its own project called Injection and now supports Injection with compatability with the JSR-330 Specification (http://jcp.org/en/jsr/detail?id=330) and Implementation (http://code.google.com/p/atinject/). See wiki

Injection Extensions can be found at this project place https://github.com/alero/injection-extensions

Maven Report for the latest release can be found here. ADD LINK!

All projects are synced to Maven2 Central and can be used directly.

The google group can be found at http://groups.google.com/group/injectcontainer

Injection
````
<dependency>
    <groupId>org.hrodberaht</groupId>
    <artifactId>inject</artifactId>
    <version>1.2.0</version>
</dependency>
````
Java Utilities
````
<dependency>
    <groupId>org.hrodberaht.directus</groupId>
    <artifactId>core</artifactId>
    <version>1.2.0</version>
</dependency>
````
I18n (Formatters and LocalProfile?)
````
<dependency>
    <groupId>org.hrodberaht</groupId>
    <artifactId>i18n</artifactId>
    <version>1.2.0</version>
</dependency>
````
