package com.versatilemobitech.VeeZee.utils;

import com.versatilemobitech.VeeZee.Model.PushForCarHandOverDetails;

import java.util.ArrayList;

/**
 * Created by USER on 23-08-2018.
 */

public class Constants {
    public static final String APP_PREF = "Veezee_Driver_Pref";
    public static boolean logMessageOnOrOff = true;
    public static final int NO_INTERNET_CONNECTION = 1;


    public static final String IS_LOGGEDIN = "IS_LOGGEDIN";
    public static final String IS_VERIFIED = "IS_VERIFIED";
    public static final String USER_ID = "USER_ID";
    public static final String NAME = "NAME";
    public static final String MOBILE = "MOBILE";
    public static final String EMAIL = "EMAIL";
    public static final String IMAGE = "IMAGE";
    public static final String DOB = "DOB";
    public static final String GENDER = "GENDER";
    public static final String WALLET_AMOUNT = "WALLET_AMOUNT";
    public static final String QR = "QR";
    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";
    public static final String STATE_ID = "STATE_ID";
    public static final String CITY_ID = "CITY_ID";
    public static final String CITY = "CITY";
    public static final String STATE = "STATE";
    public static final String REFERRER_ID = "REFERRER_ID";
    public static final String IS_EMAIL_VERIFIED_FROM ="";

    public static long Timer = 180000;

    public static final String KEY_HASH = "KEY_HASH";

    public static  boolean isTimer = false;
    public static String ShareQr = "VeeZee";

    public static boolean isTipPush = false;
    public static PushForCarHandOverDetails pushDetailsList = null;



    public static final String BASE_URL = "https://veezee.app/admin/";

    public static final String OTP_URL = BASE_URL + "send-otp";
    public static final int OTP_CODE = 101;

    public static final String STATE_URL = BASE_URL + "state";
    public static final int STATE_CODE = 102;

    public static final String CITY_URL = BASE_URL + "city";
    public static final int CITY_CODE = 103;


    public static final String SIGN_UP_URL = BASE_URL + "user-signup";
    public static final int SIGN_UP_CODE = 104;

    public static final String BRANDS_URL = BASE_URL + "get-brands";
    public static final int BRAND_CODE = 105;


    public static final String CARS_URL = BASE_URL + "get-cars-by-brand";
    public static final int CARS_CODE = 106;

    public static final String CARS_COLOR_URL = BASE_URL + "get-color-by-car";
    public static final int CARS_COLOR_CODE = 117;

    public static final String ADD_CAR_URL = BASE_URL + "add-car";
    public static final int ADD_CAR_CODE = 107;

    public static final String USER_CARS = BASE_URL + "user-cars";
    public static final int USER_CARS_CODE = 108;

    public static final String ADS_URL = BASE_URL + "ads";
    public static final int ADS_CODE = 109;

    public static final String PARTENR_CATEGORY_URL = BASE_URL + "partner-category";
    public static final int PARTENR_CATEGORY_CODE = 110;

    public static final String PARTNER_LIST = BASE_URL + "partner-list";
    public static final int PARTNER_LIST_CODE = 111;

    public static final String REWARDS_LIST = BASE_URL + "rewards-list";
    public static final int REWARDS_CODE = 112;

    public static final String DELETE_USER_CAR = BASE_URL + "delete-user-car";
    public static final int DELETE_USER_CAR_CODE = 113;

    public static final String CLAIM_REWARD_URL = BASE_URL + "claim-reward";
    public static final int CLAIM_REWARD_CODE = 114;

    public static final String REWARDS_BRANDS = BASE_URL + "reward-category";
    public static final int REWARDS_BRAND_CODE = 115;

    public static final String USER_REWARDS = BASE_URL + "user-rewards";
    public static final int USER_REWARDS_CODE = 116;

    public static final String CREDITS_HISTORY = BASE_URL + "credits-history";
    public static final int CREDITS_HISTORY_CODE = 117;

    public static final String SOCIAL_LOGINS = BASE_URL + "social-login";
    public static final int SOCIAL_LOGINS_CODE = 118;

    public static final String REPORT_ISSUE_VALET = BASE_URL + "report-issue-valet";
    public static final int REPORT_ISSUE_VALET_CODE = 119;

    public static final String HISTORY_VALET = BASE_URL + "valet-history";
    public static final int HISTORY_VALET_CODE = 120;

    public static final String HISTORY_VALUT = BASE_URL + "vault-history";
    public static final int HISTORY_VALUT_CODE = 121;

    public static final String PROFILE_EDIT = BASE_URL + "edit-profile";
    public static final int PROFILE_EDIT_CODE = 121;

    public static final String REPORT_ISSUE_VAULT = BASE_URL + "report-issue-vault";
    public static final int REPORT_ISSUE_VAULT_CODE = 122;

    public static final String REQUEST_FOR_CAR = BASE_URL + "request-car-text";
    public static final int REQUEST_FOR_CAR_CODE = 123;

    public static final String REQUEST_FOR_CHECKSUM = BASE_URL + "generate-checksum";
    public static final int REQUEST_FOR_CHECKSUM_CODE = 124;

    public static final String REQUEST_FOR_VIP_CAR = BASE_URL + "request-car";
    public static final int REQUEST_FOR_VIP_CAR_CODE = 125;

    public static final String SEND_TIP = BASE_URL + "send-tip";
    public static final int SEND_TIP_CODE = 126;

    public static final String DELETE_USER_ACCOUNT = BASE_URL + "delete-account";
    public static final int DELETE_USER_ACCOUNT_CODE = 127;

    public static final String REQUEST_CAR_STATUS = BASE_URL + "car-status";
    public static final int REQUEST_CAR_STATUS_CODE = 127;

    public static final String REQUEST_CONTACTS = BASE_URL + "veezee-contacts";
    public static final int REQUEST_CONTACTS_CODE = 128;

    public static final String SHARE_CAR = BASE_URL + "share-car";
    public static final int SHARE_CAR_CODE = 129;

    public static final String LOG_OUT = BASE_URL + "user-logout";
    public static final int LOG_OUT_CODE = 129;

    public static final String EDIT_CAR = BASE_URL + "edit-car";
    public static final int EDIT_CAR_CODE = 130;

    public static final String VERIFY_EMAIL_ID = BASE_URL + "is-email-verified";
    public static final int VERIFY_EMAIL_ID_CODE = 131;

}
