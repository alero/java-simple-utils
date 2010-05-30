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
import java.math.BigInteger;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-apr-01 17:44:28
 * @version 1.0
 * @since 1.0
 */
public class MeasureData  extends BigDecimal {
    private Measurement measure = null;


    public MeasureData(String val) {
        super(val);
    }

    public MeasureData(String val, Measurement measure) {
        super(val);
        this.measure = measure;
    }

    public MeasureData(double val) {
        super(val);
    }

    public MeasureData(double val, Measurement measure) {
        super(val);
        this.measure = measure;
    }

    public MeasureData(BigInteger val) {
        super(val);
    }

    public MeasureData(BigInteger unscaledVal, int scale) {
        super(unscaledVal, scale);
    }

    public MeasureData(BigInteger unscaledVal, Measurement measure) {
        super(unscaledVal);
        this.measure = measure;
    }

    public MeasureData(BigInteger unscaledVal, int scale, Measurement measure) {
        super(unscaledVal, scale);
        this.measure = measure;
    }

    public MeasureData(int val) {
        super(val);
    }

    public MeasureData(int val, Measurement measure) {
        super(val);
        this.measure = measure;
    }

    public MeasureData(long val) {
        super(val);
    }

    public MeasureData(long val, Measurement measure) {
        super(val);
        this.measure = measure;
    }
}
