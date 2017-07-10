package com.eduardomanrique.fxvalidation.service;

import com.eduardomanrique.fxvalidation.products.FXTransaction;
import com.eduardomanrique.fxvalidation.rulesengine.Validation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public interface FXValidationService {
    List<FXValidationResult> validateTransaction(List<FXTransaction> transactionList);

    Metrics getMetrics();

    class FXValidationResult extends Validation {

        @Getter
        private final String customerName;
        @Getter
        private final String currencyPair;
        @Getter
        private final String type;
        @Getter
        private final String direction;
        @Getter
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private final Date tradeDate;

        public FXValidationResult(String customerName, String currencyPair, String type, String direction, Date tradeDate) {
            this.customerName = customerName;
            this.currencyPair = currencyPair;
            this.type = type;
            this.direction = direction;
            this.tradeDate = tradeDate;
        }
    }

    class Metrics {
        private List<Long> timeList = new CopyOnWriteArrayList<>();
        private AtomicLong totalTime = new AtomicLong(0);
        @Getter
        private Long min = 0l;
        @Getter
        private Long max = 0l;

        public final int getTotalRequests() {
            return timeList.size();
        }

        protected final void addTime(long time) {
            if (min == 0) {
                min = time;
            } else {
                min = Math.min(time, min);
            }

            if (max == 0) {
                max = time;
            } else {
                max = Math.max(time, max);
            }
            totalTime.addAndGet(time);
            timeList.add(time);
        }

        public final Long getAverage() {
            return timeList.size() == 0 ? 0 : (long) ((double) totalTime.get()) / timeList.size();
        }

        public final int calculateQuantile95() {
            double val95 = totalTime.get() * 0.95d;
            long total = 0;
            int count = 0;
            for (Long time : timeList) {
                count++;
                total += time;
                if (total >= val95) {
                    break;
                }
            }
            return count;
        }
    }
}
