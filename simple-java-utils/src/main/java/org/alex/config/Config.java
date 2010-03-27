package org.alex.config;

import java.text.ParseException;

/**
 * Simple Java Utilts - Config
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public interface Config {

    void setPropertyPath(String path);
    void setCustomPropertyPath(String path);
    boolean requiresValidation();
    boolean validate();
    void initiate() throws IllegalAccessException;
    void loadProperties() throws ParseException;
}
