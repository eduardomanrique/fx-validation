package com.eduardomanrique.fxvalidation.products;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Spot extends FXTransaction {
    private Date valueDate;
}
