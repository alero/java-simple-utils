package test.org.hrodberaht.inject.testservices.annotated_extra;

import org.hrodberaht.inject.annotation.VariableProvider;
import test.org.hrodberaht.inject.testservices.annotated.Car;

import javax.inject.Inject;

/**
 * �Projectname�
 *
 * @author Robert Alexandersson
 *         2010-sep-26 22:41:04
 * @version 1.0
 * @since 1.0
 */
public class CarWrapper {

    @Inject
    VariableProvider<Car, Manufacturer> variableProvider;

    public Car getCar(Manufacturer manufacturer){
        return variableProvider.get(manufacturer);
    }
}
