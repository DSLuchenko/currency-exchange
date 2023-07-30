package com.dsluchenko.app.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Currency {
    private final int id;
    private final String code;
    private final String fullName;
    private final String sign;
}
