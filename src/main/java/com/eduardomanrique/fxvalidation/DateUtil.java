package com.eduardomanrique.fxvalidation;

import lombok.SneakyThrows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class DateUtil {

    private static Date currentDate;

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2016, 19, 9, 0, 0, 0);
        currentDate = cal.getTime();
    }

    private static final ThreadLocal<DateFormat> dateFormatThreadLocal =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    public static final Optional<Date> parse(Optional<String> date) {
        return date.map(s -> parse(s));
    }

    @SneakyThrows
    public static final Date parse(String date) {
        return dateFormatThreadLocal.get().parse(date);
    }

    public static final Optional<String> format(Optional<Date> date) {
        return date.map(d -> format(d));
    }

    @SneakyThrows
    public static final String format(Date date) {
        return dateFormatThreadLocal.get().format(date);
    }

    public static final Date currentDate() {
        return currentDate;
    }
}
