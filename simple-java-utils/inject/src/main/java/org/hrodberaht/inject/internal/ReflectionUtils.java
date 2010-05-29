package org.hrodberaht.inject.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 00:58:46
 * @version 1.0
 * @since 1.0
 */
public class ReflectionUtils {

    public static List<Method> findMethods(Class clazz) {
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        final List<Method> methods = new ArrayList<Method>();

        for (final Method method : declaredMethods) {
            methods.add(method);
        }

        if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
            methods.addAll(findMethods(clazz.getSuperclass()));
        }

        return methods;
    }

    


    public static List<Member> findMembers(Class clazz) {
        final List<Member> members = new ArrayList<Member>();

        final Field[] declaredFields = clazz.getDeclaredFields();

        for (final Field field : declaredFields) {
            members.add(field);
        }

        final Method[] declaredMethods = clazz.getDeclaredMethods();

        for (final Method method : declaredMethods) {
            members.add(method);
        }


        if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
            members.addAll(0, findMembers(clazz.getSuperclass()));
        }

        return members;
    }

    public static boolean isOverridden(Method method, List<Method> candidates) {
        for (final Method candidate : candidates) {
            if (isOverridden(method, candidate)) {
                return true;
            }
        }

        return false;
    }
    
    public static boolean isOverridden(Method method, Method candidate) {
        if (!hasOverridableAccessModifiers(method, candidate)) {
            return false;
        }

        if (!isSubClassOf(candidate.getDeclaringClass(), method.getDeclaringClass())) {
            return false;
        }

        if (!hasTheSameName(method, candidate)) {
            return false;
        }

        if (!hasTheSameParameters(method, candidate)) {
            return false;
        }

        return true;
    }

    public static boolean isInTheSamePackage(Method method, Method candidate) {
        final Package methodPackage = method.getDeclaringClass().getPackage();
        final Package candidatePackage = candidate.getDeclaringClass().getPackage();

        return methodPackage.equals(candidatePackage);
    }



    public static boolean hasOverridableAccessModifiers(Method method, Method candidate) {
        if (isFinal(method) || isPrivate(method) || isStatic(method)
                || isPrivate(candidate) || isStatic(candidate)) {
            return false;
        }

        if (isDefault(method)) {
            return isInTheSamePackage(method, candidate);
        }

        return true;
    }

    public static boolean isPrivate(Member member) {
        return Modifier.isPrivate(member.getModifiers());
    }
    public static boolean isProtected(Member member) {
        return Modifier.isProtected(member.getModifiers());
    }

    public static boolean isPublic(Member member) {
        return Modifier.isPublic(member.getModifiers());
    }

    public static boolean isDefault(Member member) {
        return !isPublic(member) && !isProtected(member) && !isPrivate(member);
    }

    public static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    public static boolean isFinal(Member member) {
        return Modifier.isFinal(member.getModifiers());
    }

    public static boolean isSubClassOf(Class<?> subclass, Class<?> superclass) {
        if (subclass.getSuperclass() != null) {
            if (subclass.getSuperclass().equals(superclass)) {
                return true;
            }

            return isSubClassOf(subclass.getSuperclass(), superclass);
        }

        return false;
    }

    public static boolean hasTheSameName(Method method, Method candidate) {
        return method.getName().equals(candidate.getName());
    }

    public static boolean hasTheSameParameters(Method method, Method candidate) {
        Class<?>[] methodParameters = method.getParameterTypes();
        Class<?>[] candidateParameters = candidate.getParameterTypes();

        if (methodParameters.length != candidateParameters.length) {
            return false;
        }

        for (int i = 0; i < methodParameters.length; i++) {
            Class<?> methodParameter = methodParameters[i];
            Class<?> candidateParameter = candidateParameters[i];

            if (!methodParameter.equals(candidateParameter)) {
                return false;
            }
        }

        return true;
    }

}
