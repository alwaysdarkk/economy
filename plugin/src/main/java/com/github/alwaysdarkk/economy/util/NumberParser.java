package com.github.alwaysdarkk.economy.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberParser {

    public Double tryParseDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public boolean isInvalid(Double value) {
        return value == null || value.isNaN() || value.isInfinite() || value < 1.0;
    }
}
