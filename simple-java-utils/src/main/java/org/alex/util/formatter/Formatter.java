

package org.alex.util.formatter;

import org.alex.util.locale.LocaleProvider;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class Formatter
{

    protected Locale locale = LocaleProvider.getProfile().getLocale();

    private static Map registry = Collections.synchronizedMap(new HashMap());
    protected Class propertyType;
    
    static {
        registerFormatter(String.class, Formatter.class);
        registerFormatter(BigDecimal.class, CurrencyFormatter.class);
        registerFormatter(Date.class, DateFormatter.class);
        registerFormatter(Integer.class, IntegerFormatter.class);
        registerFormatter(int.class, IntegerFormatter.class);
        registerFormatter(Boolean.class, BooleanFormatter.class);
        registerFormatter(Boolean.TYPE, BooleanFormatter.class);
    }
    
    public static Formatter getFormatter(Class aType) {
        return null;
    }
    

    
    /**
     * Binds the provided value type to a Formatter type. Note that a single
     * Formatter class can be associated with more than one type.
     * @param type a value type
     * @param formatterType a Formatter type
     */
    public static void registerFormatter(Class type, Class formatterType) {
        registry.put(type, formatterType);
    }
    
    /**
     * Returns <code>true</code> if the provided class is an array type,
     * implements either the {@link List} or {@link Set} interfaces, or is
     * one of the Formatter classes currently registered.
     * @see #registerFormatter(Class, Class)
     */
    public static boolean isSupportedType(Class type)
    {
        if (List.class.isAssignableFrom(type)) return true;
        if (Set.class.isAssignableFrom(type))  return true;
        
        return findFormatter(type) != null;
    }
    

    
    public static Class findFormatter(Class type)
    {
        if (type == null) return null;
        
        Iterator typeIter = registry.keySet().iterator();
        while (typeIter.hasNext())
        {
            Class currType = (Class) typeIter.next();
            if (currType.isAssignableFrom(type))
            {
                return (Class) registry.get(currType);
            }
        }
        
        return null;
    }
    

    public String convertToString(Object value) {
        return value.toString();
    }

    public Object convertToObject(String string)
    {
        return string == null ? null : string.trim();
    }

    public static boolean isEmptyValue(String target) {
        return false;
    }
}
