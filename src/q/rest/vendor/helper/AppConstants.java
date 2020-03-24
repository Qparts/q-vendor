package q.rest.vendor.helper;

public class AppConstants {

    public final static String EMAIL_ADDRESS = "no-reply@qetaa.com";
    public final static String PASSWORD = "qetaa3!Cs@";
    public final static String SMTP_SERVER = "smtp.zoho.com";
    public final static String PRODUCT_WS_ENDPOINT = "ws://localhost:8081/service-q-product/ws/";

    private final static String WEBSITE_BASE_URL = "http://qtest.fareed9.com/";
    private static final String USER_SERVICE = "http://localhost:8081/service-q-user/rest/internal/api/v2/";
    private static final String IMAGE_SERVICE = "http://localhost:8081/q-images/rest/internal/api/v2/";
    private static final String CUSTOMER_SERVICE = "http://localhost:8081/service-q-customer/rest/internal/api/v2/";
    private static final String INVOICE_SERVICE = "http://localhost:8081/service-q-invoice/rest/internal/api/v2/";
    private static final String PRODUCT_QVM_SERVICE = "http://localhost:8081/service-q-product/rest/qvm/api/v2/";
    private final static String PUBLIC_VEHICLE_SERVICE = "http://localhost:8081/service-qetaa-vehicle/rest/api/v1/";

    public static final String CUSTOMER_MATCH_TOKEN = CUSTOMER_SERVICE + "match-token";
    public static final String USER_MATCH_TOKEN = USER_SERVICE + "match-token";
    public static final String POST_QVM_SEARCH_AVAILABILITY = PRODUCT_QVM_SERVICE + "search-availability";
    public static final String POST_QVM_SEARCH_PARTS = PRODUCT_QVM_SERVICE + "search-parts";
    public final static String REGISTRATION_COMPLETE_EMAIL_TEMPLATE = "email/registration-complete.vm";
    public final static String VENDOR_APPROVED_EMAIL_TEMPLATE = "email/vendor-approved.vm";
    public final static String SUBSCRIPTION_INVOICE_EMAIL_TEMPLATE = "email/subscription-invoice.vm";
    public final static String SUBSCRIPTION_INVOICE_EMAIL_TEMPLATE2 = "email/subscription-invoice2.vm";
    public final static String EMAIL_VERIFICATION_EMAIL_TEMPLATE = "email/email-verification.vm";
    public final static String PASSWORD_RESET_TEMPLATE = "email/password-reset.vm";

    public final static String getActivationLink(String email, String code){
        return "https://www.qvm.parts/activation?email=" + email + "&code="+code;
    }

    public final static String getPasswordResetLink(String code){
        return "https://www.qvm.parts/password-reset?code="+code;
    }

    public static String getQvmSales(long qvmSalesId){
        return INVOICE_SERVICE + "qvm-sales/" + qvmSalesId;
    }

    public final static String getProductQVMSearchEndpoint(int vendorUserId, String token){
        return PRODUCT_WS_ENDPOINT + "search/vendor-user/"+vendorUserId+"/token/" + token;
    }

}
