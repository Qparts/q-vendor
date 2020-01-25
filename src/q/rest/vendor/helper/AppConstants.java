package q.rest.vendor.helper;

public class AppConstants {

    public final static String EMAIL_ADDRESS = "no-reply@qetaa.com";
    public final static String PASSWORD = "qetaa3!Cs@";
    public final static String SMTP_SERVER = "smtp.zoho.com";

    private final static String WEBSITE_BASE_URL = "http://qtest.fareed9.com/";
    private static final String USER_SERVICE = "http://localhost:8081/service-q-user/rest/internal/api/v2/";
    private static final String IMAGE_SERVICE = "http://localhost:8081/q-images/rest/internal/api/v2/";
    private static final String CUSTOMER_SERVICE = "http://localhost:8081/service-q-customer/rest/internal/api/v2/";
    private static final String PRODUCT_QVM_SERVICE = "http://localhost:8081/service-q-product/rest/qvm/api/v2/";
    private final static String PUBLIC_VEHICLE_SERVICE = "http://localhost:8081/service-qetaa-vehicle/rest/api/v1/";

    public static final String CUSTOMER_MATCH_TOKEN = CUSTOMER_SERVICE + "match-token";
    public static final String USER_MATCH_TOKEN = USER_SERVICE + "match-token";
    public static final String POST_QVM_SEARCH_AVAILABILITY = PRODUCT_QVM_SERVICE + "search-availability";
    public static final String POST_QVM_SEARCH_PARTS = PRODUCT_QVM_SERVICE + "search-parts";
    public final static String REGISTRATION_COMPLETE_EMAIL_TEMPLATE = "email/registration-complete.vm";
    public final static String VENDOR_APPROVED_EMAIL_TEMPLATE = "email/vendor-approved.vm";

}
