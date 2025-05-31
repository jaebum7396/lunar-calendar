package com.codism.config.error.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorEntityBody {
    private String code;
    private Object body;
}
