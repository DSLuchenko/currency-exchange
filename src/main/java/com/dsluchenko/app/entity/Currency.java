package com.dsluchenko.app.entity;

import lombok.Data;

@Data
public class Currency {
    private final int id;
    private final String code;
    private final String fullName;
    private final String sign;
}
