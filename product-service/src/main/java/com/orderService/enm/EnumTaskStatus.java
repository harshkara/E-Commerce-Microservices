package com.orderService.enm;

public enum EnumTaskStatus {

    OPEN("00"),CLOSED("01");

    private final String code;
    EnumTaskStatus(String number) {
        this.code = number;
    }

    public String getCode(){
        return code;
    }

    public static EnumTaskStatus fromCode(String code) {
        for (EnumTaskStatus status : EnumTaskStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
