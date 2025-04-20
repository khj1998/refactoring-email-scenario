package com.email.common;

import lombok.Getter;

@Getter
public enum EmailTypeEnum {
    INFORMATION(0),
    NEWSLETTER(1);

    private final int value;

    EmailTypeEnum(int value) {
        this.value = value;
    }
}
