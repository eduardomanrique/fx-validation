package com.eduardomanrique.fxvalidation.products;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Forward extends FXTransaction {
    private Date valueDate;
}
