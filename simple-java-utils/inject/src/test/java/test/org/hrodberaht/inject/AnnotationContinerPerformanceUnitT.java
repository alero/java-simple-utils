package test.org.hrodberaht.inject;

import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.hrodberaht.inject.SimpleInjection;
import org.junit.Test;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 15:39:23
 * @version 1.0
 * @since 1.0
 */
public class AnnotationContinerPerformanceUnitT {

    @Test(timeout = 10000)
    public void testPerformance(){
        AnnotationContinerUtil.prepareRegister();
        for(int i=0;i<10000;i++){
            final Car car = SimpleInjection.get(Car.class);
            Tck.testsFor(car, false, true);
        }

    }
}
