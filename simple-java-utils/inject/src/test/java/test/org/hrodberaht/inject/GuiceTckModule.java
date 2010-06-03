package test.org.hrodberaht.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.atinject.tck.auto.Car;
import org.atinject.tck.auto.Convertible;
import org.atinject.tck.auto.Drivers;
import org.atinject.tck.auto.DriversSeat;
import org.atinject.tck.auto.Engine;
import org.atinject.tck.auto.FuelTank;
import org.atinject.tck.auto.Seat;
import org.atinject.tck.auto.Tire;
import org.atinject.tck.auto.V8Engine;
import org.atinject.tck.auto.accessories.Cupholder;
import org.atinject.tck.auto.accessories.SpareTire;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-03 17:15:09
 * @version 1.0
 * @since 1.0
 */
public class GuiceTckModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(Car.class).to(Convertible.class);
        bind(Seat.class).annotatedWith(Drivers.class).to(DriversSeat.class);
        bind(Tire.class).annotatedWith(Names.named("spare")).to(SpareTire.class);
        bind(Engine.class).to(V8Engine.class);
        bind(Cupholder.class);
        bind(Tire.class);
        bind(FuelTank.class);
        requestStaticInjection(Convertible.class, SpareTire.class);
    }

}
