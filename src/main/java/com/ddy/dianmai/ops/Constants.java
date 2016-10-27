package com.ddy.dianmai.ops;

public interface Constants {
    String CURRENT_USER = "user";
    String SESSION_FORCE_LOGOUT_KEY = "session.force.logout";
    
    /**
     * 上个页面地址
     */
    String BACK_URL = "BackURL";

    String IGNORE_BACK_URL = "ignoreBackURL";
    
    String CONTEXT_PATH = "ctx";
    
    /**
     * 当前请求的地址 带参数
     */
    String CURRENT_URL = "currentURL";
    
    /**
     * 当前请求的地址 不带参数
     */
    String NO_QUERYSTRING_CURRENT_URL = "noQueryStringCurrentURL";
    
    /**
     * 当前在线会话
     */
    String ONLINE_SESSION = "online_session";

    /**
     * 仅清空本地缓存 不情况数据库的
     */
    String ONLY_CLEAR_CACHE = "online_session_only_clear_cache";
    
    public static final String DEFAULT_CHAR_SET = "UTF-8";

    public static final String REQUEST_HEADER_TOKEN = "token";
    public static final String REQUEST_HEADER_TIMESTAMP = "timestamp";
    public static final String REQUEST_HEADER_UID = "uid";

    public static final String REQUEST_HEADER_CLIENT_AGENT = "Client-Agent";

    public static final String REQUEST_HEADER_APPID = "appId";
    public static final String REQUEST_HEADER_APPUUID = "appUUID";


    public static final String REQUEST_BODY_USERNAME = "username";
    public static final String REQUEST_BODY_PASSWORD = "password";
}
