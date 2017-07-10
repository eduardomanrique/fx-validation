package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.util.DateUtil;
import com.eduardomanrique.fxvalidation.util.DateUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class FixerIOApiGatewayServiceImpl implements FixerIOApiGatewayService {

    private RestTemplate restTemplate = new RestTemplate();

    private static final String URL_FIXER_IO = "http://api.fixer.io/";

    @Cacheable("businessDays")
    @Override
    public boolean isBusinessDay(Date date) {
        String formatedDate = DateUtil.format(date);
        String url = URL_FIXER_IO + formatedDate;
        ResponseEntity<Map> resource = restTemplate.getForEntity(url, Map.class);
        return formatedDate.equals(resource.getBody().get("date"));
    }

    @Cacheable("businessDaysBetween")
    @Override
    public int businessDaysBetween(Date date1, Date date2) {
        int result = 0;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (cal1.before(cal2)) {
            cal1.add(Calendar.DATE, 1);
            if (isBusinessDay(cal1.getTime())) {
                result++;
            }
        }
        return result;
    }
}
