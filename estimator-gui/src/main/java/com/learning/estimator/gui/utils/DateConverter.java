package com.learning.estimator.gui.utils;

import com.vaadin.data.util.converter.Converter;

import java.time.ZonedDateTime;
import java.util.Locale;

/**
 * Zoned date time converter
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public class DateConverter implements Converter<String, ZonedDateTime> {

    @Override
    public ZonedDateTime convertToModel(String value, Class<? extends ZonedDateTime> targetType, Locale locale) throws ConversionException {
        throw new ConversionException("This is a one-way converter");
    }

    @Override
    public String convertToPresentation(ZonedDateTime value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (value == null)
            return "Not finished yet";
        return value.getDayOfMonth() + "-" + value.getMonth() + "-" + value.getYear();
    }

    @Override
    public Class<ZonedDateTime> getModelType() {
        return ZonedDateTime.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
