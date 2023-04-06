package com.ventas.crud;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToBigDecimalConverter;

public class DolarConverter extends StringToBigDecimalConverter {

	public DolarConverter() {
        super("No se puede convertir el valor a número");
    }

    @Override
    public Result<BigDecimal> convertToModel(String value,
            ValueContext context) {
        value = value.replaceAll("[$\\s]", "").trim();
        if ("".equals(value)) {
            value = "0";
        }
        return super.convertToModel(value, context);
    }

    @Override
    protected NumberFormat getFormat(Locale locale) {
        // Always display currency with two decimals
        NumberFormat format = super.getFormat(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setMaximumFractionDigits(2);
            ((DecimalFormat) format).setMinimumFractionDigits(2);
        }
        return format;
    }

    @Override
    public String convertToPresentation(BigDecimal value,
            ValueContext context) {
        return super.convertToPresentation(value, context) + " $";
    }
}
