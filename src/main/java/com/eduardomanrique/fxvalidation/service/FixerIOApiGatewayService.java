package com.eduardomanrique.fxvalidation.service;

import java.util.Date;

public interface FixerIOApiGatewayService {
    boolean isBusinessDay(Date date);

    int businessDaysBetween(Date date1, Date date2);
}
