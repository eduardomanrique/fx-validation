package com.eduardomanrique.fxvalidation.products;

import lombok.Data;

import java.util.Date;

@Data
public class Spot extends FXTransaction {
    private Date valueDate;
}
