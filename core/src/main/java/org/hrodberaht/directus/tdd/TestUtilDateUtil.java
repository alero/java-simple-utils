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

package org.hrodberaht.directus.tdd;

import org.hrodberaht.directus.exception.MessageRuntimeException;
import org.hrodberaht.directus.util.DateUtil;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Simple Java Utils
 *
 * When using the DateUtil from this package it is possible to add a very useful feature where a written test can be
 * forced to happen on a specific date. 
 *
 * @author Robert Alexandersson
 *         2010-apr-11 21:22:22
 * @version 1.0
 * @since 1.0
 */
public class TestUtilDateUtil {

    private TestUtilDateUtil() {}

    public static void setNowDate(Date date){

        try {
            Field field = DateUtil.class.getDeclaredField("systemOverriddenNow");
            field.setAccessible(true);
            field.set(null, date);
        } catch (IllegalAccessException e) {
            throw new MessageRuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new MessageRuntimeException(e);
        }
    }

    public static void clearNowDate() {
        try {
            Field field = DateUtil.class.getDeclaredField("systemOverriddenNow");
            field.setAccessible(true);
            field.set(null, null);
        } catch (IllegalAccessException e) {
            throw new MessageRuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new MessageRuntimeException(e);
        }
    }
}
