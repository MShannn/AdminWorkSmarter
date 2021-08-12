package com.jvidal.worksmarter.PDFReport.utils;

public class Utilities {
    public static int generateRandomNumber(int lower, int upper) {
        return (int) (Math.random() * (upper + 1 - lower)) + lower;
    }
}
