package com.doctogo;

public interface Constants {
    public static final String SERVER_URL="http://10.0.2.2:8080";
    public static final String USER_SERVICE_URL= SERVER_URL+"/user/";
    public static final String NOTIFICATION_SERVICE_URL= SERVER_URL+"/notification/";
    public static final String DISPATCHER_SERVICE_URL= SERVER_URL+"/dispatcher/";
    public static final String HELPER_SERVICE_URL= SERVER_URL+"/helper/";
    public static final String GET_ALL_NOTIFICATIONS_SERVICE_URL = DISPATCHER_SERVICE_URL
            +"getNotifications";
    public static final String ACCEPT_AND_FIND_HELPERS_SERVICE_URL = DISPATCHER_SERVICE_URL
            +"acceptAndFindHelpers";

    public static final String ACCEPT_NOTIF_SERVICE_URL = DISPATCHER_SERVICE_URL
            +"acceptNotification";

    public static final String USER_INFO ="USER_INFO";
    public static final String NOTIF_INFO="NOTIF_INFO";
    public static final String USER_LIST="USER_LIST";
    public static final String EMAIL_PARAM="email";
    public static final String PASS_PARAM="password";
    public static final String N_ID_PARAM="notificationId";
    public static final String HELPER_IDS_PARAM="helperIds";
    public static final String HELPER_ID_PARAM="helperId";
    public static final String DISPATCHER_ID_PARAM="dispatcherId";

    public static final String NOTIF_ID_TAG="NID";

}
