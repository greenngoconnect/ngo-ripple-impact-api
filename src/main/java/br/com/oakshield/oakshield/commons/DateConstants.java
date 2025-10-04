package br.com.oakshield.oakshield.commons;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * The type Constants.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DateConstants {
    public static final String BRAZILIAN_DATE_TIME = "dd/MM/yyyy HH:mm:ss";
    public static final String BRAZILIAN_DATE_TIME_WITHOUT_SECONDS = "dd/MM/yyyy HH:mm";
    public static final String BRAZILIAN_DATE = "dd/MM/yyyy";
    public static final String ISO8601_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSSz"; //2023-06-14T14:33:55.275Z
    public static final String RAW_PASSWORD = "123456";

}