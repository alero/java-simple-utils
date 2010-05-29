package org.hrodberaht.inject.internal;

import org.hrodberaht.inject.InjectRuntimeException;
import org.hrodberaht.inject.SimpleInjection;
import org.hrodberaht.inject.creators.SimpleContainerInstanceCreator;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 12:38:22
 * @version 1.0
 * @since 1.0
 */
public class SimpleInjectionContainer extends InjectionContainerBase implements InjectionContainer {


    private SimpleContainerInstanceCreator simpleContainerInstanceCreator = null;

    public void setSimpleContainerInstanceCreator(SimpleContainerInstanceCreator simpleContainerInstanceCreator) {
        this.simpleContainerInstanceCreator = simpleContainerInstanceCreator;
    }

    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope, String qualifier) {
        if (simpleContainerInstanceCreator != null) {
            if (forcedScope != null && !simpleContainerInstanceCreator.supportForcedInstanceScope()) {
                throw new InjectRuntimeException("Can not use forced scope for service {0}", service);
            }
            if (simpleContainerInstanceCreator.supportServiceCreation(service)) {
                return simpleContainerInstanceCreator.getService(service);
            }
        }
        if (!registeredNamedServices.containsKey(qualifier)) {
            throw new InjectRuntimeException("Service {0} not registered in SimpleInjection", service);
        }
        ServiceRegister serviceRegister = registeredNamedServices.get(qualifier);
        return instantiateService(service, forcedScope, serviceRegister);
    }

    private void reRegisterSupport(Class anInterface, SimpleInjection.RegisterType type) {
        ServiceRegister serviceRegister = registeredServices.get(anInterface);
        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.WEAK) {
            registeredServices.remove(anInterface);
            return;
        }

        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.NORMAL) {
            if (type == SimpleInjection.RegisterType.OVERRIDE_NORMAL) {
                registeredServices.remove(anInterface);
                return;
            }
            throw new InjectRuntimeException(
                    "Service {0} is already registered, to override register please use the override method"
                    , anInterface);
        }
        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.FINAL) {
            throw new InjectRuntimeException(
                    "A FINAL Service for {0} is already registered, can not reRegister", anInterface);
        }

    }

    private void reRegisterSupport(String namedInstance, SimpleInjection.RegisterType type) {
        ServiceRegister serviceRegister = registeredNamedServices.get(namedInstance);
        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.WEAK) {
            registeredNamedServices.remove(namedInstance);
            return;
        }

        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.NORMAL) {
            if (type == SimpleInjection.RegisterType.OVERRIDE_NORMAL) {
                registeredNamedServices.remove(namedInstance);
                return;
            }
            throw new InjectRuntimeException(
                    "Service {0} is already registered, to override register please use the override method"
                    , namedInstance);
        }
        if (serviceRegister.getRegisterType() == SimpleInjection.RegisterType.FINAL) {
            throw new InjectRuntimeException(
                    "A FINAL Service for {0} is already registered, can not reRegister", namedInstance);
        }

    }

    @SuppressWarnings(value = "unchecked")
    public <T> T getService(Class<T> service, SimpleInjection.Scope forcedScope) {

        if (simpleContainerInstanceCreator != null) {
            if (forcedScope != null && !simpleContainerInstanceCreator.supportForcedInstanceScope()) {
                throw new InjectRuntimeException("Can not use forced scope for service {0}", service);
            }
            if (simpleContainerInstanceCreator.supportServiceCreation(service)) {
                return simpleContainerInstanceCreator.getService(service);
            }
        }
        ServiceRegister serviceRegister = findServiceImplementation(service);
        return instantiateService(service, forcedScope, serviceRegister);
    }

    private <T> ServiceRegister findServiceImplementation(Class<T> service) {
        if (!registeredServices.containsKey(service)) {
            if(service.isInterface()){ // TODO support this for classes as well? = inheritance support
                for(ServiceRegister serviceRegister:registeredServices.values()){
                    if(service.isAssignableFrom(serviceRegister.getService())){
                        return serviceRegister;                         
                    }
                }
            }
            throw new InjectRuntimeException("Service {0} not registered in SimpleInjection", service);
        }
        ServiceRegister serviceRegister = registeredServices.get(service);
        return serviceRegister;
    }

    @Override
    public void register(Class anInterface, Class<Object> service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        if (registeredServices.containsKey(anInterface)) {
            reRegisterSupport(anInterface, type);
        }
        registeredServices.put(anInterface,
                new ServiceRegister(service, createInstance(new ServiceRegister(service)), scope, normalizeType(type))
        );
    }

    @Override
    public void register(String namedInstance, Class<Object> service, SimpleInjection.Scope scope, SimpleInjection.RegisterType type) {
        if (registeredNamedServices.containsKey(namedInstance)) {
            reRegisterSupport(namedInstance, type);
        }
        registeredNamedServices.put(namedInstance,
                new ServiceRegister(service, createInstance(new ServiceRegister(service)), scope, normalizeType(type))
        );
    }


    public Object createInstance(ServiceRegister serviceRegister) {
        try {
            return serviceRegister.getService().newInstance(); // uses empty constructor
        } catch (InstantiationException e) {
            throw new InjectRuntimeException("Could not create an instance of {0}", e, serviceRegister.getService());
        } catch (IllegalAccessException e) {
            throw new InjectRuntimeException("Could not create an instance of {0}", e, serviceRegister.getService());
        }
    }


}
