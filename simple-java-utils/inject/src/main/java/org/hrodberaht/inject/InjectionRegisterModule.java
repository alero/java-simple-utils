package org.hrodberaht.inject;

import org.hrodberaht.inject.register.InjectionRegister;
import org.hrodberaht.inject.register.RegistrationModule;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-aug-01 16:35:23
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterModule extends InjectionRegisterBase<InjectionRegister> {

    private Collection<RegistrationModule> registeredModules = new ArrayList<RegistrationModule>();

    public InjectionRegisterModule() {
    }

    public InjectionRegisterModule(InjectionRegister register) {
        super(register);
    }

    public InjectionRegisterModule register(RegistrationModule... modules) {
        for(RegistrationModule module:modules){
            registeredModules.add(module);
        }
        container.register(modules);
        return this;
    }

    public void printRegistration() {

        System.out.println("--------- InjectionRegisterModule Information Printer --------------");
        System.out.println("The following modules has been appended in order");
        for(RegistrationModule module:registeredModules){
            System.out.println("Module: "+module.getClass().getName());       
        }

        super.printRegistration();

    }

    
}
