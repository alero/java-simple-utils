/*
 * ~ Copyright (c) 2010.
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~        http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and limitations under the License.
 */

package org.hrodberaht.inject;

import org.hrodberaht.inject.internal.exception.InjectRuntimeException;
import org.hrodberaht.inject.register.InjectionRegister;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterScan extends InjectionRegisterBase<InjectionRegisterScan> {


    private boolean detailedScanLogging = false;
    private Collection<CustomClassLoader> customClassLoaders = new ArrayList<CustomClassLoader>();

    public InjectionRegisterScan() {        
    }

    public InjectionRegisterScan(InjectionRegister register) {
        super(register);
    }

    public InjectionRegisterScan registerBasePackageScan(String packagename) {
        Class[] clazzs = getClasses(packagename);
        for (Class aClazz : clazzs) {
            createRegistration(aClazz);
        }
        return this;
    }

    public InjectionRegisterScan registerBasePackageScan(String packagename, Class... manuallyexcluded) {
        Class[] clazzs = getClasses(packagename);
        List<Class> listOfClasses = new ArrayList<Class>(clazzs.length);

        // remove the manual excludes
        for (Class aClazz : clazzs) {
            if (!manuallyExcluded(aClazz, manuallyexcluded)) {
                listOfClasses.add(aClazz);
            }
        }
        for (Class aClazz : listOfClasses) {
            createRegistration(aClazz);
        }
        return this;
    }


    public void registerThirdPartyJar(String... jars) {
        for (String jar : jars) {

            try {
                URL u = new URL("jar", "", jar);
                URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{ u });
                customClassLoaders.add(new CustomClassLoader(jarClassLoader, CustomClassLoader.ClassLoaderType.JAR));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void setDetailedScanLogging(boolean detailedScanLogging) {
        this.detailedScanLogging = detailedScanLogging;
    }

    private void createRegistration(Class aClazz) {
        if (
                !aClazz.isInterface()
                && !aClazz.isAnnotation()
                && !Modifier.isAbstract(aClazz.getModifiers())
                ) {
            try{
                container.register(aClazz, aClazz, null, SimpleInjection.RegisterType.NORMAL);
            }catch(InjectRuntimeException e){
                System.out.println("Hrodberaht Injection: Silently failed to register class = "+aClazz);
                if(detailedScanLogging){
                    e.printStackTrace(System.err);       
                }
            }
        }
    }

    private boolean manuallyExcluded(Class aClazz, Class[] manuallyexluded) {
        for (Class excluded : manuallyexluded) {
            if (excluded == aClazz) {
                return true;
            }
        }
        return false;
    }


    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private Class[] getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<Class> classes = findClassesToLoad(
                packageName, classLoader, CustomClassLoader.ClassLoaderType.THREAD
        );
        for (CustomClassLoader customclassLoader : customClassLoaders) {
            ClassLoader parentClassLoader = ClassLoader.getSystemClassLoader();
            classes.addAll(findClassesToLoad(packageName, parentClassLoader, customclassLoader.loaderType));
        }
        return classes.toArray(new Class[classes.size()]);

    }

    private ArrayList<Class> findClassesToLoad(
            String packageName, ClassLoader classLoader, CustomClassLoader.ClassLoaderType loaderType)
    {
        if(loaderType == CustomClassLoader.ClassLoaderType.THREAD){
            return findFiles(packageName, classLoader);
        }else if(loaderType == CustomClassLoader.ClassLoaderType.JAR){
            return findFiles(packageName, classLoader);
        }
        return null;
    }



    private ArrayList<Class> findFiles(String packageName, ClassLoader classLoader) {
        ArrayList<Class> classes = new ArrayList<Class>();
        try {

            assert classLoader != null;
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<File>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile().replaceAll("%20"," ")));
            }

            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }
        } catch (IOException e) {
            throw new InjectRuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new InjectRuntimeException(e);
        }
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6))
                );
            }
        }
        return classes;
    }

    private ArrayList<Class> findJarFiles(String packageName, ClassLoader classLoader) {
        ArrayList<Class> classes = new ArrayList<Class>();
        try {

            assert classLoader != null;
            URLClassLoader urlClassLoader = (URLClassLoader)classLoader;
            String path = packageName.replace('.', '/');
            URL[] resources = urlClassLoader.getURLs();
            List<JarFile> dirs = new ArrayList<JarFile>();
            for (URL resource:resources) {
                dirs.add(new JarFile(resource.getFile()));
            }

            for (JarFile directory : dirs) {
                classes.addAll(findJarClasses(directory, packageName));
            }
        } catch (IOException e) {
            throw new InjectRuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new InjectRuntimeException(e);
        }
        return classes;
    }

    private static List<Class> findJarClasses(JarFile directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        Enumeration<JarEntry> files = directory.entries();
        while (files.hasMoreElements()) {
            JarEntry file = files.nextElement();
            classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6))
            );
        }
        return classes;
    }




    private static class CustomClassLoader{

        private enum ClassLoaderType { JAR, THREAD };

        public CustomClassLoader(URLClassLoader classLoader, ClassLoaderType loaderType) {
            this.classLoader = classLoader;
            this.loaderType = loaderType;
        }

        private ClassLoader classLoader;
        private ClassLoaderType loaderType;
    }  

}