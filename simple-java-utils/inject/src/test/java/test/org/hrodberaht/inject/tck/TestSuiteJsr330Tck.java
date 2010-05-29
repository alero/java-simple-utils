package test.org.hrodberaht.inject.tck;

import junit.framework.TestCase;
import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.atinject.tck.auto.Convertible;
import org.atinject.tck.auto.DriversSeat;
import org.atinject.tck.auto.Engine;
import org.atinject.tck.auto.FuelTank;
import org.atinject.tck.auto.Seat;
import org.atinject.tck.auto.V8Engine;
import org.atinject.tck.auto.accessories.Cupholder;
import org.atinject.tck.auto.accessories.RoundThing;
import org.hrodberaht.inject.InjectionRegisterJava;
import org.hrodberaht.inject.SimpleInjection;


/**
 * Simple Java Utils - Injection
 *
 * @author Robert Alexandersson
 *         2010-maj-28 19:27:43
 * @version 1.0
 * @since 1.0
 */


public class TestSuiteJsr330Tck extends TestCase {


    public static junit.framework.Test suite() {        

        InjectionRegisterJava.activateJavaXInjectCompliance();

        InjectionRegisterJava.register(Car.class, Convertible.class);
        InjectionRegisterJava.register(Engine.class, V8Engine.class);
        InjectionRegisterJava.register(Cupholder.class);
        InjectionRegisterJava.register(RoundThing.class, "spare");
        InjectionRegisterJava.register(FuelTank.class);
        InjectionRegisterJava.register(Seat.class);
        InjectionRegisterJava.register(DriversSeat.class, "Drivers");


        final Car car = SimpleInjection.get(Car.class);
        final boolean supportsStatic = false;
        final boolean supportsPrivate = true;

        return Tck.testsFor(car, supportsStatic, supportsPrivate);

    }

}
