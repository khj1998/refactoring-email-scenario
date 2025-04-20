package com.email.util;

import com.email.common.StatusCodeEnum;
import com.email.exception.BaseException;
import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class TokenUtils {
    private static final int TRANSACTION_ID_INDEX = 0;
    private static final int SYSTEM_ID_INDEX = 1;
    private static final int REQUEST_TIME_MILLIS_INDEX = 2;

    public String decodeToken(String token) {
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        return new String(decodedBytes);
    }

    public String[] parseToken(String decodedToken) {
        return decodedToken.split(":");
    }

    public String getRequestSystemId(String[] decodedTokenArray) {
        return decodedTokenArray[SYSTEM_ID_INDEX];
    }

    public String getTransactionId(String[] decodedTokenArray) {
        return decodedTokenArray[TRANSACTION_ID_INDEX];
    }

    public void validateTokenArrayLength(String[] decodedTokenArray) {
        if (decodedTokenArray.length != 3) {
            throw new BaseException(StatusCodeEnum.INVALID_TOKEN_ARRAY_LENGTH);
        }
    }

    public void validateRequestMillisTime(String[] decodedTokenArray) {
        Long requestTimeMillis = getRequestTimeMillis(decodedTokenArray);
        Long currentTimeMillis = System.currentTimeMillis();

        Long twoMinutesMillis = 180000L;
        Long timeDifference = currentTimeMillis - requestTimeMillis;

        if (timeDifference > twoMinutesMillis || requestTimeMillis > currentTimeMillis) {
            throw new BaseException(StatusCodeEnum.INVALID_REQUEST_TIME_MILLIS);
        }
    }

    private Long getRequestTimeMillis(String[] decodedTokenArray) {
        return Long.parseLong(decodedTokenArray[REQUEST_TIME_MILLIS_INDEX]);
    }
}
