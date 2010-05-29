package test.org.hrodberaht.inject;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 13:38:13
 * @version 1.0
 * @since 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    SuiteJsr330TckUnitT.class,
    SimpleContainerUnitT.class,
    SimpleContainerInstanceCreatorUnitT.class,
    AnnotationContinerPerformanceUnitT.class
})
public class TestSuite {
}
