package org.hrodberaht.i18n.formatter.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-apr-01 17:39:35
 * @version 1.0
 * @since 1.0
 */
public class CurrencyData extends BigDecimal {

    private Currency currency = null;


    public CurrencyData(String val) {
        super(val);
    }

    public CurrencyData(String val, Currency currency) {
        super(val);
        this.currency = currency;
    }

    public CurrencyData(double val) {
        super(val);
    }

    public CurrencyData(double val, Currency currency) {
        super(val);
        this.currency = currency;
    }

    public CurrencyData(BigInteger val) {
        super(val);
    }

    public CurrencyData(BigInteger unscaledVal, int scale) {
        super(unscaledVal, scale);
    }

    public CurrencyData(BigInteger unscaledVal, Currency currency) {
        super(unscaledVal);
        this.currency = currency;
    }

    public CurrencyData(BigInteger unscaledVal, int scale, Currency currency) {
        super(unscaledVal, scale);
        this.currency = currency;
    }

    public CurrencyData(int val) {
        super(val);
    }

    public CurrencyData(int val, Currency currency) {
        super(val);
        this.currency = currency;
    }

    public CurrencyData(long val) {
        super(val);
    }

    public CurrencyData(long val, Currency currency) {
        super(val);
        this.currency = currency;
    }

}
