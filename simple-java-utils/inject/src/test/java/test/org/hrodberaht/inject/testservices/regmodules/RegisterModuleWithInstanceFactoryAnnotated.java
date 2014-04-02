package test.org.hrodberaht.inject.testservices.regmodules;

import org.hrodberaht.inject.ScopeContainer;
import org.hrodberaht.inject.internal.annotation.InjectionFinder;
import org.hrodberaht.inject.register.InjectionFactory;
import org.hrodberaht.inject.register.RegistrationModuleAnnotation;
import test.org.hrodberaht.inject.testservices.annotated.Car;
import test.org.hrodberaht.inject.testservices.annotated.Spare;
import test.org.hrodberaht.inject.testservices.annotated.SpareTire;
import test.org.hrodberaht.inject.testservices.annotated.SpareVindShield;
import test.org.hrodberaht.inject.testservices.annotated.TestDriver;
import test.org.hrodberaht.inject.testservices.annotated.Tire;
import test.org.hrodberaht.inject.testservices.annotated.VindShield;
import test.org.hrodberaht.inject.testservices.annotated.Volvo;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-aug-01 16:32:48
 * @version 1.0
 * @since 1.0
 */
public class RegisterModuleWithInstanceFactoryAnnotated extends RegistrationModuleAnnotation {

    @Override
    public void registrations() {
        InjectionFinder injectionFinder = new CustomInjectionPointFinder();
        registerInjectionFinder(injectionFinder);

        register(Car.class).with(Volvo.class);
        register(Tire.class).annotated(Spare.class).with(SpareTire.class);
        register(VindShield.class).annotated(Spare.class).with(SpareVindShield.class);
        register(TestDriver.class).scopeAs(ScopeContainer.Scope.SINGLETON).with(TestDriver.class);
    }


}
