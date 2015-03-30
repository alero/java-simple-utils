# Introduction #

The Java Injection Container is Inversion of Control Container
- in the default setting a very simple IoC container without Dependency Injection support.
- in the Injection setting its a JSR330 Compliant IoC container with very simple Injection support.
- it can also wrap Spring/Guice Injection containers inside to provide their support, simply working as a Factory Wrapper around the thirdparty framework. (This is not recommend usage, its only meant as demo feature.)

The JSR-330 Specification (http://jcp.org/en/jsr/detail?id=330) and Implementation (http://code.google.com/p/atinject/) with the TCK (http://en.wikipedia.org/wiki/Technology_Compatibility_Kit).

The project is synced to Maven Central (no third party repository is needed)
```
<dependency>
    <groupId>org.hrodberaht</groupId>
    <artifactId>inject</artifactId>
    <version>1.2.0</version>
</dependency>

```

Next release 1.3, plans
- General bug fixes based on usage
- Performance improvements (remove duplicate internal hashmap, test was done with 70% improved performance)

Simplest possible usage

```
InjectionRegisterJava registerJava = new InjectionRegisterJava();
registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);

Container container = registerJava.getContainer();
AnyService anyService = container.get(AnyService.class);
```

# Details #

## The Simple Container ##

The default instance creator has a very extendible and register friendly Java API for Manual Registration and Scanning of packages.
The unique part of this version of the API is that it can create three different kinds of Registrations (same as Guice bindings).

**Default registration**
  * The registration can be overridden without calling for it using normal registration.
  * Is intended for a replaceble service in a Java Module (a part system)
Example, first part is done in a Constructor while second part is done in some registration override support method for this "Module".

```
InjectionRegisterJava registerJava = new InjectionRegisterJava();
registerJava.registerDefault(AnyService.class, AnyServiceDoNothingImpl.class);

// This code will work just fine and override the old registration
registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
```

See [LocaleProvider](http://code.google.com/p/java-simple-utils/source/browse/tags/1.0.0/i18n/src/main/java/org/hrodberaht/i18n/locale/LocaleProvider.java) in i18n project.


**Normal registration**

  * The registration is weakly protected against overwriting but can be forced using reRegister support. Will throw Runtime exceptions when registrations is done in this way. (At container registration boot up)
  * Is inteded as the normal way to bind the interfaces and implementations.

Scenario with InjectRuntimeException thrown as result
```
InjectionRegisterJava registerJava = new InjectionRegisterJava();
registerJava.register(AnyService.class, AnyServiceDoNothingImpl.class);

// This code will not work as intended
registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
```
Good scenario with overridden registration
```
InjectionRegisterJava registerJava = new InjectionRegisterJava();
registerJava.register(AnyService.class, AnyServiceDoNothingImpl.class);

// This code will work just fine and override the old registration
registerJava.reRegister(AnyService.class, AnyServiceDoSomethingImpl.class);
```

**Final registration**

As the name applies this can not be overridden in any way. And was never inteded to be so.

  * The registration is strongly protected against overwriting and can not be forced. Will throw Runtime exceptions when re-registration is done.
  * Is inteded as a protected final registration where the developer still want to use the container for instance creation.

```
InjectionRegisterJava registerJava = new InjectionRegisterJava();
registerJava.finalRegister(AnyService.class, AnyServiceDoNothingImpl.class);

// This code will not work as intended
registerJava.register(AnyService.class, AnyServiceDoSomethingImpl.class);
```

**Registration Module**

All these registration types can be performed with a collection of registrations in a Registration Module (RegistrationModuleSimple).
```
InjectionRegisterJava registerJava = new InjectionRegisterJava();
registerJava.register(new RegistrationModuleSimple() {
    public void registrations() {
        register(AnyService.class)
            .annotated(DoNothing.class)
            .scopeAs(ScopeContainer.Scope.SINGLETON)
            .with(AnyServiceDoNothingImpl.class);

        register(AnyService.class)
            .registeredAs(SimpleInjection.RegisterType.FINAL)
            .with(AnyServiceDoSomethingImpl.class);
    }
});

Container container = registerJava.getContainer();
AnyService anyService = container.get(AnyService.class);
AnyService anyNothingService = container.get(AnyService.class, DoNothing.class);

```
## The Injection Container ##

JSR 330 compliant version of the Container, has passed the TCK for JSR-330 (not confirmed by the owners of the JSR yet).
See http://code.google.com/p/atinject/

This does not have support for different types of Registrations. Recommended usage to be able to perform all needed registration is the RegistrationModuleAnnotation class. This can be extended (or created) and contains all registration support needed for annotation's scopes and so on.

The big difference is that this version of the Container has dependency injection support, this is provided via the JSR 330 Annotations and interface.

Example registration with Annotated by a implemented named annotation.
```
InjectionRegisterJava registerJava = new InjectionRegisterJava();
registerJava.register(new RegistrationModuleAnnotation(){
    @Override
    public void registrations() {
       register(Tire.class).annotated(Spare.class).with(SpareTire.class);
       register(VindShield.class).annotated(Spare.class).with(SpareVindShield.class);
    }
});
Container container = registerJava.getContainer();
```

Example registration to pass JSR330 TCK
```
InjectionRegisterJava registerJava = new InjectionRegisterJava();
RegistrationModuleAnnotation module = new RegistrationModuleAnnotation(){         
    public void registrations() {
        register(Car.class).with(Convertible.class);
        register(Engine.class).with(V8Engine.class);
        register(Tire.class).namned("spare").with(SpareTire.class);
        register(Seat.class).annotated(Drivers.class).with(DriversSeat.class);
    }
};
Container container = registerJava.register(module).getContainer();
```

## Container Register Scanning ##

This is support for a non manual registration of implementations that satisfy the needs for some applications interfaces. This is intended as a fast way of getting it to work for simple Interface Service one to one relations. The InjectionRegisterScan extends the InjectionRegisterJava so its provides all the same methods and two extras.

The methods _registerBasePackageScan(String packagename)_ and _registerBasePackageScan(String packagename, Class... manuallyexcluded)_.

Register a normal scanning area (will search this and all underlying classes and register them as long as they are Classes not of the type interface or annotation)

Works for both Simple Container and Injection Container.

```
InjectionRegisterScan register = new InjectionRegisterScan();
// Tests scanning and exclusion of single class
register.registerBasePackageScan("test.org.hrodberaht.inject.testservices", AnyServiceDoNothingImpl.class);

Container container = register.getContainer();
AnyService anyService = container.get(AnyService.class);
```

```
InjectionRegisterScan register = new InjectionRegisterScan();
// Tests scanning of a package base
      register.registerBasePackageScan("test.org.hrodberaht.inject.testservices.annotated");

Container container = register.getContainer();
Car aCar = container.get(Car.class);
```