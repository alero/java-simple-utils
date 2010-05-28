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
