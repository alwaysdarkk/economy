package com.github.alwaysdarkk.economy.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class NumberFormatter {

    private final List<String> suffixes = Arrays.asList(
            "", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N", "D", "UN", "DD", "TR", "QT", "QN", "SD", "SPD",
            "OD", "ND", "VG", "UVG", "DVG", "TVG", "QTV", "QNV", "SEV", "SPV", "OVG", "NVG", "TG");

    public String format(double value) {
        int index = 0;
        double tmp;

        while ((tmp = value / 1000) >= 1) {
            value = tmp;
            ++index;
        }

        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value) + suffixes.get(index);
    }
}