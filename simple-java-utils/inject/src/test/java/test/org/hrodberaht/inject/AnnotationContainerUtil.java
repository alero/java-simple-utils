package test.org.hrodberaht.inject;

import org.atinject.tck.auto.Car;
import org.atinject.tck.auto.Convertible;
import org.atinject.tck.auto.DriversSeat;
import org.atinject.tck.auto.Engine;
import org.atinject.tck.auto.FuelTank;
import org.atinject.tck.auto.Seat;
import org.atinject.tck.auto.V8Engine;
import org.atinject.tck.auto.accessories.Cupholder;
import org.atinject.tck.auto.accessories.SpareTire;
import org.hrodberaht.inject.InjectionRegisterJava;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:51
 * @version 1.0
 * @since 1.0
 */
public class AnnotationContainerUtil {

    public static void prepareRegister() {
        InjectionRegisterJava.activateJavaXInjectCompliance();

        InjectionRegisterJava.register(Car.class, Convertible.class);
        InjectionRegisterJava.register(Engine.class, V8Engine.class);
        InjectionRegisterJava.register(Cupholder.class);
        InjectionRegisterJava.register(SpareTire.class, "spare");
        InjectionRegisterJava.register(FuelTank.class);
        InjectionRegisterJava.register(Seat.class);
        // hmm should be support for Qualifier annotation instead
        InjectionRegisterJava.register(DriversSeat.class, "Drivers");
    }
}
