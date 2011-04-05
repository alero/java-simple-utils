package test.org.hrodberaht.inject.annotation;

import org.hrodberaht.inject.InjectionRegisterJava;
import org.hrodberaht.inject.InjectionRegisterModule;
import org.hrodberaht.inject.InjectionRegisterScan;
import org.hrodberaht.inject.register.RegistrationModuleAnnotation;
import org.junit.BeforeClass;
import org.junit.Test;
import test.org.hrodberaht.inject.PerformanceTests;
import test.org.hrodberaht.inject.testservices.annotated.Car;
import test.org.hrodberaht.inject.testservices.annotated.CarCreatorSingleton;
import test.org.hrodberaht.inject.testservices.annotated_extra.Saab;

import java.util.Date;

import static org.junit.Assert.assertFalse;

/**
 * �Projectname�
 *
 * @author Robert Alexandersson
 *         2010-okt-18 20:45:45
 * @version 1.0
 * @since 1.0
 */
public class CloneContainerRegistrationUnitT implements PerformanceTests {

    @BeforeClass
    public static void initClass()
    {
        // Prepares and optimizes the clone method
        InjectionRegisterModule registerJava = AnnotationContainerUtil.prepareLargeVolvoRegister();
        for(int i=0;i<500;i++){
            InjectionRegisterModule registerJavaClone = registerJava.clone();
            registerJavaClone.activateContainerJavaXInject();
        }
    }

    @Test
    public void testCloneJava() throws CloneNotSupportedException {
        InjectionRegisterJava injectionRegisterJava = new InjectionRegisterJava();
        InjectionRegisterJava testCloneJava = injectionRegisterJava.clone();
        assertFalse(testCloneJava == injectionRegisterJava);
    }

     @Test
    public void testCloneJavaScan() throws CloneNotSupportedException {
        InjectionRegisterScan injectionRegisterJava = new InjectionRegisterScan();
        InjectionRegisterScan testCloneJava = injectionRegisterJava.clone();
        assertFalse(testCloneJava == injectionRegisterJava);
    }

    @Test
    public void testCloneJavaModule() throws CloneNotSupportedException {
        InjectionRegisterModule injectionRegisterJava = new InjectionRegisterModule();
        InjectionRegisterModule testCloneJava = injectionRegisterJava.clone();
        assertFalse(testCloneJava == injectionRegisterJava);
    }

    @Test
    public void testCloneSingletonService() throws Exception {
        InjectionRegisterScan registerJava = new InjectionRegisterScan();
        registerJava.registerBasePackageScan("test.org.hrodberaht.inject.testservices.annotated");

        CarCreatorSingleton carCreatorSingleton = registerJava.getInjectContainer().get(CarCreatorSingleton.class);

        InjectionRegisterScan registerJavaClone = registerJava.clone();
        CarCreatorSingleton carCreatorSingletonClone = registerJavaClone.getInjectContainer().get(CarCreatorSingleton.class);

        assertFalse(carCreatorSingleton == carCreatorSingletonClone);

    }

    @Test(timeout = 500)
    public void testClonePerformance() throws CloneNotSupportedException {

        TimerUtil timer = new TimerUtil().start();
        InjectionRegisterModule registerJava = AnnotationContainerUtil.prepareLargeVolvoRegister();
        for(int i=0;i<1000;i++){
            InjectionRegisterModule registerJavaClone = registerJava.clone();
            if(registerJavaClone.equals(null)){
                System.out.println("Just doing something so the compiler wont skip the code");
            }
        }
        timer.endAndPrint("testClonePerformance");
    }

    @Test(timeout = 500)
    public void testCloneAndRegisterPerformance() throws CloneNotSupportedException {
        TimerUtil timer = new TimerUtil().start();
        InjectionRegisterModule registerJava = AnnotationContainerUtil.prepareLargeVolvoRegister();
        for(int i=0;i<1000;i++){
            InjectionRegisterModule registerJavaClone = registerJava.clone();
            registerJavaClone.overrideRegister(Car.class, Saab.class);
        }
        timer.endAndPrint("testCloneAndRegisterPerformance");
    }

    @Test(timeout = 500)
    public void testCloneAndRegisterModulePerformance() throws CloneNotSupportedException {
        TimerUtil timer = new TimerUtil().start();
        InjectionRegisterModule registerJava = AnnotationContainerUtil.prepareLargeVolvoRegister();
        for(int i=0;i<1000;i++){
            InjectionRegisterModule registerJavaClone = registerJava.clone();
            registerJavaClone.register(new RegistrationModuleAnnotation(){
                @Override
                public void registrations() {
                    register(Car.class).withInstance(new Saab());
                }
            });
        }
        timer.endAndPrint("testCloneAndRegisterModulePerformance");
    }

    private class TimerUtil {
        private Date startDate = null;
        private Date endDate = null;

        public TimerUtil start() {
            startDate = new Date();
            return this;
        }

        public void endAndPrint(String message) {
            endDate = new Date();
            System.out.println(message+" : "+(endDate.getTime()-startDate.getTime())+"ms");
        }
    }
}
