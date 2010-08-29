/*
 * <!--
 *   ~ Copyright (c) 2010.
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
 *   -->
 */

package org.hrodberaht.i18n.formatter.types;

import java.math.BigDecimal;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-apr-01 17:50:27
 * @version 1.0
 * @since 1.0
 */
public class PercentData extends BigDecimal {

    public PercentData(String val) {
        super(val);
    }

    public PercentData(double val) {
        super(val);
    }

    public PercentData(int val) {
        super(val);
    }

    public PercentData(long val) {
        super(val);
    }
}
