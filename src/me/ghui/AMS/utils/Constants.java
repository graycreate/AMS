package me.ghui.AMS.utils;

/**
 * Created by ghui on 3/25/14.
 */
public interface Constants {
    String ID = "id";
    String PSW = "password";
    String LOGIN_DATA = "login_data_sharedpreference";
    String UNLOGIN = "";
    String PREFS_USER_INFO = "pref_user_info";
    String PREF_USER_NAME = "pref_user_name";
    String PREF_USER_PSW = "pref_user_psw";
    String PREF_USER_ID = "pref_user_id";


    //below is url

    String BASE_URL = "http://202.196.192.25";
    String VALIDATE_CODE_URL = BASE_URL + "/lyit/sys/ValidateCode.aspx";
    String LOGIN_URL = BASE_URL + "/lyit/_data/index_LOGIN.aspx";
    String TEACHER_INFO_URL = BASE_URL + "/lyit/jxzy/Tea_MyInfo_rpt.aspx";
    String TERM_INFO_URL = BASE_URL + "/lyit/znpk/Pri_TeacKCJXRW.aspx";
    String COURSE_INFO_URL = BASE_URL + "/lyit/znpk/Pri_TeacKCJXRW_rpt.aspx";
    String TERM_INFO_TEA_PLAN = BASE_URL + "/lyit/znpk/Pri_TeacSel.aspx";
    String PLAN_INFO_URL = BASE_URL + "/lyit/znpk/Pri_TeacSel_rpt.aspx";
    String PSW_MOD_URL = BASE_URL + "/lyit/MyWeb/User_ModPWD.aspx";
    String COURSE_GRADE_INFO_URL = BASE_URL + "/lyit/xscj/Tea_skbjcj.aspx";
    String COURSE_GRADE_INFO_DETAIL_URL = BASE_URL + "/lyit/xscj/Tea_skbjcj_rpt.aspx";
    String SOURCE_GRADE_INFO_URL = BASE_URL + "/lyit/XSCJ/Tea_skbjcj_print.aspx";
    String FINAL_GRADE_INFO_URL = BASE_URL + "/lyit/XSCJ/Tea_skbjcj_printyx.aspx";
    String INPUT_GRADE_INFO_URL = BASE_URL + "/lyit/XSCJ/Tea_KCCJLR.aspx";
    String INPUT_GRADE_INFO_INDEX_URL = BASE_URL + "/lyit/XSCJ/Tea_KCCJLR_rpt.aspx";
    String INPUT_SCORE_URL = BASE_URL + "/lyit/XSCJ/KCCJ_ADD_rpt_T.aspx";
    String USER_INFO_FOOT_URL = BASE_URL + "/lyit/PUB/foot.aspx";
}
