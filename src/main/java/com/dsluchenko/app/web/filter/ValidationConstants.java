package com.dsluchenko.app.web.filter;

interface ValidationConstants {
    int CURRENCY_CODE_LENGTH = 3;
    int SIGN_LENGTH = 1;

    int START_INDEX_FIRST_CURRENCY_CODE_IN_URL = 1;
    int END_INDEX_FIRST_CURRENCY_CODE_IN_URL = 4;
}
