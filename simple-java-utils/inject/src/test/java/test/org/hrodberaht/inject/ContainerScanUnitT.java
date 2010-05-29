package test.org.hrodberaht.inject;

import org.hrodberaht.inject.InjectionRegisterScan;
import org.hrodberaht.inject.SimpleInjection;
import org.junit.Test;
import test.org.hrodberaht.inject.testservices.AnyService;
import test.org.hrodberaht.inject.testservices.AnyServiceDoNothingImpl;
import test.org.hrodberaht.inject.testservices.annotated.Car;
import test.org.hrodberaht.inject.testservices.annotated.Volvo;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 17:02:46
 * @version 1.0
 * @since 1.0
 */
public class ContainerScanUnitT {


    @Test
    public void testScanningOfImplementations() {
        InjectionRegisterScan.resetContainerToDefault();
        // Tests scanning and exclusion of single class
        InjectionRegisterScan.registerBasePackageScan("test.org.hrodberaht.inject.testservices", AnyServiceDoNothingImpl.class);


        AnyService anyService = SimpleInjection.get(AnyService.class);
        anyService.doStuff();

        assertEquals(1, anyService.getStuff().size());

    }


    @Test
    public void testAnnotatedScanningOfImplementations() {
        InjectionRegisterScan.activateJavaXInjectCompliance();
        // Tests scanning and exclusion of single class
        InjectionRegisterScan.registerBasePackageScan("test.org.hrodberaht.inject.testservices.annotated");
        Car aCar = SimpleInjection.get(Car.class);

        assertEquals("volvo", aCar.brand());
        Volvo aVolvo = (Volvo)aCar;
        assertNotNull(aVolvo.getBackLeft());
        assertNotNull(aVolvo.getBackRight());
        assertNotNull(aVolvo.getFrontLeft());
        assertNotNull(aVolvo.getFrontRight());
        assertNotNull(aVolvo.getSpareTire());
        assertNotNull(aVolvo.getVindShield());

    }
}
