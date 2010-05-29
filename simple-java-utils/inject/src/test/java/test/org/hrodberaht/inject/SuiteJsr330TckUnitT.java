package test.org.hrodberaht.inject;

import junit.framework.TestCase;
import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.hrodberaht.inject.SimpleInjection;


/**
 * Simple Java Utils - Injection
 *
 * @author Robert Alexandersson
 *         2010-maj-28 19:27:43
 * @version 1.0
 * @since 1.0
 */

public class SuiteJsr330TckUnitT extends TestCase {

    public static junit.framework.Test suite() {

        AnnotationContainerUtil.prepareRegister();

        final Car car = SimpleInjection.get(Car.class);
        final boolean supportsStatic = false;
        final boolean supportsPrivate = true;

        return Tck.testsFor(car, supportsStatic, supportsPrivate);

    }





}
