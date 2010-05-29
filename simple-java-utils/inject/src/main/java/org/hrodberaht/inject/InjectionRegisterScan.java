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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class InjectionRegisterScan extends InjectionRegisterBase {

    public static void registerBasePackageScan(String packagename) {
        Class[] clazzs = getClasses(packagename);        
        for(Class aClazz:clazzs){
            if(
                    !aClazz.isInterface()
                    && !aClazz.isAnnotation()
            ){
                InjectionRegisterJava.register(aClazz);
            }
        }
    }

    public static void registerBasePackageScan(String packagename, Class... manuallyexluded) {
        Class[] clazzs = getClasses(packagename);
        List<Class> listOfClasses = new ArrayList<Class>(clazzs.length);

        // remove the manual excludes
        for(Class aClazz:clazzs){
            if(!manuallyExluded(aClazz, manuallyexluded)){
                listOfClasses.add(aClazz);
            }
        }
        for(Class aClazz:listOfClasses){
            if(
                    !aClazz.isInterface()
                    && !aClazz.isAnnotation()
            ){
                InjectionRegisterJava.register(aClazz);
            }
        }
    }

    private static boolean manuallyExluded(Class aClazz, Class[] manuallyexluded) {
        for(Class excluded:manuallyexluded){
            if(excluded == aClazz){
                return true;   
            }
        }
        return false;
    }

    public static void registerSingleLevelPackageScan(String packagename) {

    }

    


    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class[] getClasses(String packageName) {
        ArrayList<Class> classes = new ArrayList<Class>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            assert classLoader != null;
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = null;

            resources = classLoader.getResources(path);

            List<File> dirs = new ArrayList<File>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }

            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }
        } catch (IOException e) {
            throw new InjectRuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new InjectRuntimeException(e);
        }
        return classes.toArray(new Class[classes.size()]);

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
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}