package com.mb.lab.banks.user.persistence.domain.entity;

public enum SmsType {
    CREATE_PARTNER_ADMIN,
    RESET_PASSWORD_PARTNER_ADMIN,
    CREATE_STORE_ADMIN,
    RESET_PASSWORD_STORE_ADMIN,
    CUSTOMER_GET_VOUCHER,
    CUSTOMER_GET_VOUCHER_NO_CODE,
    CUSTOMER_GET_VOUCHER_CODE_EXPIRE_WARNING,
    CUSTOMER_GET_VOUCHER_CODE_EXPIRE_WARNING_NO_CODE,
    ALMOST_OUT_OF_VOUCHER_CODE_WARNING,
    OUT_OF_VOUCHER_CODE_WARNING,
    ADMIN_RESEND_VOUCHER_CODE,
    GIVE_VOUCHER_CODE,
    VOUCHER_LIMIT_PERIOD_EXCEEDED,
    VOUCHER_LIMIT_GLOBAL_EXCEEDED,
    VOUCHER_LIMIT_DAY_EXCEEDED,
    VOUCHER_LIMIT_HOUR_EXCEEDED,
    MYVIETTEL_API_KPI_SLOW,
    MYVIETTEL_API_KPI_ERROR,
    MYVIETTEL_API_GET_CODE_KPI_SLOW,
    MYVIETTEL_API_GET_CODE_KPI_ERROR,
    CUSTOMER_GET_TOO_MANY_CODE_OF_VOUCHER,
    CUSTOMER_GET_VOUCHER_FREE,
    CUSTOMER_GET_VOUCHER_BY_POINT,
    CREATE_SUB_ADMIN,
    RESET_PASSWORD_SUB_ADMIN,
    CREATE_ECOM_SUB_ADMIN,
    RESET_PASSWORD_ECOM_SUB_ADMIN,
    APPROVE_BOOKING,
    REJECT_BOOKING,
    CONTRACT_VALUE_THRESHOLD,
    DAILY_NUMBER_GET_CODE_BY_POINT,
    NUMBER_GET_CODE_INCREASED_TO_PREVIOUS_DAY,
    PARTNER_GET_CODE_UNEXPECTED_ERROR,
    NEW_BOOKING,
    NEW_REGISTER_MEMBER_CARD,
    CUSTOMER_GET_MANY_CODES,
    NEW_COOPERATION_REQUEST,
    WARNING_CDR_FILES_DIFFERENCE,
    WARNING_EXPORT_CDR_ERROR,
    NEW_REGISTER_GOLDEN_GATE,
    NEW_BOOKING_TO_CUSTOMER,
    VOUCHER_INTRODUCE_MESSAGE,
    CONTRACT_VALUE_THRESHOLD_BY_FIELD;
}