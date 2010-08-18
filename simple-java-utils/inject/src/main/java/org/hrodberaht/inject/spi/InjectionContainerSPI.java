package org.hrodberaht.inject.spi;

import org.hrodberaht.inject.internal.annotation.InstanceCreatorFactory;
import org.hrodberaht.inject.internal.annotation.creator.InstanceCreator;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-aug-10 18:10:19
 * @version 1.0
 * @since 1.0
 */
public class InjectionContainerSPI {
    public void changeInstanceCreator(InstanceCreator instanceCreator){
        InstanceCreatorFactory.setCreator(instanceCreator);        
    }

    public void resetInstanceCreator(InstanceCreator instanceCreator){
        InstanceCreatorFactory.resetCreator();        
    }
}