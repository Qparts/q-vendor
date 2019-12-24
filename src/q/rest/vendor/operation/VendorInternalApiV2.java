package q.rest.vendor.operation;

import q.rest.vendor.dao.DAO;
import q.rest.vendor.filter.SecuredUser;
import q.rest.vendor.filter.SecuredUserVendor;
import q.rest.vendor.filter.SecuredVendor;
import q.rest.vendor.filter.ValidApp;
import q.rest.vendor.helper.AppConstants;
import q.rest.vendor.helper.Helper;
import q.rest.vendor.model.contract.QvmSearchRequest;
import q.rest.vendor.model.contract.QvmVendorCredentials;
import q.rest.vendor.model.contract.VendorHealthCheck;
import q.rest.vendor.model.contract.VendorUserHolder;
import q.rest.vendor.model.entity.*;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Path("/internal/api/v2/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VendorInternalApiV2 {

    @EJB
    private DAO dao;

    @SecuredUserVendor
    @GET
    @Path("vendors")
    public Response getAllVendors() {
        try {
            List<Vendor> vendors = dao.getOrderBy(Vendor.class, "id");
            for(Vendor vendor : vendors){
                addCategories(vendor);
                addContacts(vendor);
            }
            return Response.status(200).entity(vendors).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @GET
    @Path("couriers")
    public Response getAllCouriers() {
        try {
            List<Courier> couriers= dao.getOrderBy(Courier.class, "id");
            return Response.status(200).entity(couriers).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @POST
    @Path("vendor")
    public Response createVendor(Vendor vendor){
        try{
            if(vendorExists(vendor)){
                return Response.status(409).build();
            }
            vendor.setCreated(new Date());
            dao.persist(vendor);
            for(VendorCategory vc : vendor.getVendorCategories()){
                vc.setCreated(new Date());
                vc.setVendorId(vendor.getId());
                dao.persist(vc);
            }

            for(VendorContact vc : vendor.getVendorContacts()){
                vc.setVendorId(vendor.getId());
                vc.setCreated(new Date());
                dao.persist(vc);
            }
            return Response.status(201).build();

        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @POST
    @Path("courier")
    public Response createCourier(Courier courier) {
        try {
            Courier c = dao.findCondition(Courier.class, "name", courier.getName());
            if (c != null) {
                return Response.status(409).build();
            }
            dao.persist(courier);
            return Response.status(201).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }



    @SecuredUser
    @GET
    @Path("courier/{param}")
    public Response getCourier(@PathParam(value = "param") int cId) {
        try {
            Courier courier = dao.find(Courier.class, cId);
            return Response.status(200).entity(courier).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }




    @ValidApp
    @POST
    @Path("login")
    public Response login(@HeaderParam("Authorization") String header, Map<String,Object> map) {
        try {
            WebApp webApp = getWebAppFromAuthHeader(header);
            String password = Helper.cypher((String) map.get("password"));
            String email = (String) map.get("email");
            String sql = "select b from VendorUser b where b.status = :value0 and b.email = :value1 and b.password = :value2";
            VendorUser vendorUser = dao.findJPQLParams(VendorUser.class, sql, 'A', email, password);
            if (vendorUser != null) {
                String token = issueToken(vendorUser, webApp, 500);
                VendorUserHolder holder = new VendorUserHolder();
                holder.setVendorUser(vendorUser);
                holder.setToken(token);
                return Response.status(200).entity(holder).build();
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            return Response.status(401).build();
        }
    }




    @SecuredUser
    @PUT
    @Path("approve-vendor")
    public Response approveVendor(@HeaderParam("Authorization") String header, SignupRequest sr){
        try{
            String sql = "select b from SignupRequest b where b.id = :value0 and b.status = :value1";
            SignupRequest check = dao.findJPQLParams(SignupRequest.class, sql, sr.getId(), 'N');//new
            if(check == null){
                return Response.status(429).build();
            }
            sr.setStatus('A');//approved
            dao.update(sr);

            //create vendor
            Vendor vendor = new Vendor();
            vendor.setCreated(new Date());
            vendor.setCreatedBy(0);
            vendor.setIntegrationType('V');
            vendor.setName(sr.getVendorName());
            vendor.setNameAr(sr.getVendorNameAr());
            vendor.setNotes(sr.getNotes());
            vendor.setStatus('A');
            dao.persist(vendor);

            //create vendor user
            VendorUser u = new VendorUser();
            u.setFirstName(sr.getFirstName());
            u.setLastName(sr.getLastName());
            u.setPassword(sr.getPassword());
            u.setStatus('A');
            u.setVendorId(vendor.getId());
            u.setEmail(sr.getEmail());
            u.setMobile(sr.getMobile());
            u.setCreated(new Date());
            u.setCreatedBy(0);
            dao.persist(u);

            //create branch
            Branch b = new Branch();
            b.setCityId(sr.getCityId());
            b.setCountryId(sr.getCountryId());
            b.setCreated(new Date());
            b.setCreatedBy(0);
            b.setStatus('A');
            dao.persist(b);
            return Response.status(200).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }


    @ValidApp
    @POST
    @Path("signup-request")
    public Response signupRequest(@HeaderParam("Authorization") String header, SignupRequest signupRequest) {
        try {
//            WebApp webApp = getWebAppFromAuthHeader(header);
            String cypher = Helper.cypher(signupRequest.getPassword());
            String sql = "select b from VendorUser b where b.email = :value0";
            VendorUser vendorUser = dao.findJPQLParams(VendorUser.class, sql, signupRequest.getEmail());
            if (vendorUser != null) {
                return Response.status(409).entity("Already Registered").build();
            }
            sql = "select b from SignupRequest b where b.email = :value0";
            SignupRequest sr = dao.findJPQLParams(SignupRequest.class, sql, signupRequest.getEmail());
            if(sr != null){
                return Response.status(409).entity("Request Already Sent").build();
            }
            signupRequest.setPassword(cypher);
            signupRequest.setCreated(new Date());
            dao.persist(signupRequest);
            return Response.status(201).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }



    @SecuredUser
    @GET
    @Path("health-check")
    public Response integratedHealthCheck() {
        try {
            List<Vendor> vendors = dao.getTwoConditions(Vendor.class, "integrationType", "status", 'I', 'A');
            List<VendorHealthCheck> checklist = new ArrayList<>();
            if(!vendors.isEmpty()){
                ExecutorService es = Executors.newFixedThreadPool(vendors.size());
                for (int i = 0; i < vendors.size(); i++) {
                    final int ii = i;
                    Runnable runnable = () -> {
                        Vendor vendor = vendors.get(ii);
                        int code = 200;
                        try {
                            String endpoint = vendor.getEndpointAddress() + "check/";
                            String header = "Bearer " + vendor.getIntegrationSecretCode();
                            Response r = getSecuredRequest(endpoint, header);
                            code = r.getStatus();
                            VendorHealthCheck health = new VendorHealthCheck();
                            health.setVendor(vendor);
                            health.setStatus(code);
                            checklist.add(health);
                        } catch (Exception ignore) {
                        }
                    };
                    es.execute(runnable);
                }
                es.shutdown();
                while (!es.isTerminated()) ;
                //all threads ended
            }
            return Response.status(200).entity(checklist).build();
        }

        catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(500).build();
        }
    }

    private void saveSearchKeyword(String query, Integer vendorUserId, Integer vendorId, WebApp webApp){
        QvmSearchKeyword sk = new QvmSearchKeyword();
        sk.setCreated(new Date());
        sk.setQuery(query);
        sk.setVendorUserId(vendorUserId);
        sk.setVendorId(vendorId);
        sk.setAppCode(webApp.getAppCode());
        dao.persist(sk);
    }



    @SecuredVendor
    @Path("search")
    @POST
    public Response search(@HeaderParam("Authorization") String header, Map<String, Object> map){
        try{
            WebApp webApp = getWebAppFromAuthHeader(header);
            String query = Helper.undecorate((String) map.get("query"));
            Integer userId = ((Number) map.get("userId")).intValue();
            Integer vendorId = ((Number) map.get("vendorId")).intValue();
            saveSearchKeyword(query, userId, vendorId, webApp);

            QvmSearchRequest searchRequest = new QvmSearchRequest();
            searchRequest.setVendorCreds(new ArrayList<>());
            searchRequest.setQuery(query);
            List<Vendor> vendors = dao.getTwoConditions(Vendor.class, "integrationType", "status", 'I', 'A');
            for(Vendor vendor: vendors){
                QvmVendorCredentials creds = new QvmVendorCredentials();
                creds.setEndpointAddress(vendor.getEndpointAddress());
                creds.setSecret(vendor.getIntegrationSecretCode());
                creds.setVendorId(vendor.getId());
                searchRequest.getVendorCreds().add(creds);
            }
            Response r = postSecuredRequest(AppConstants.POST_QVM_SEARCH, searchRequest, header);
            System.out.println(r.getStatus());
            if(r.getStatus() != 200) {
                throw new Exception();
            }
            Object o = r.readEntity(Object.class);
            return Response.status(200).entity(o).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }



    @SecuredUser
    @GET
    @Path("signup-requests/pending")
    public Response getSingupRequests(@HeaderParam("Authorization") String header){
        try{
            List<SignupRequest> srs = dao.getCondition(SignupRequest.class, "status", 'N');
            return Response.status(200).entity(srs).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }





    @SecuredUser
    @POST
    @Path("/match-token")
    public Response matchToken(Map<String,String> map) {
        try {
            String appSecret = map.get("appSecret");
            String token = map.get("token");
            Integer userId = Integer.parseInt(map.get("username"));
            WebApp webApp = getWebAppFromSecret(appSecret);

            String sql = "select b from AccessToken b where b.vendorUserId = :value0 and b.webApp = :value1 " +
                    "and b.status =:value2 and b.token =:value3 and b.expire > :value4";
            List<AccessToken> accessTokenList = dao.getJPQLParams(AccessToken.class, sql, userId, webApp, 'A', token, new Date());
            if(accessTokenList.isEmpty()){
                throw new NotAuthorizedException("Request authorization failed");
            }
            return Response.status(200).build();
        } catch (Exception e) {
            return Response.status(403).build();// unauthorized
        }
    }



    private boolean vendorExists(Vendor vendor){
        String sql = "select b from Vendor b where lower(b.name) =:value0 or lower(b.nameAr) =:value1";
        List<Vendor> vendors = dao.getJPQLParams(Vendor.class, sql, vendor.getName().trim().toLowerCase(), vendor.getNameAr().trim().toLowerCase());
        return !vendors.isEmpty();
    }

    private void addCategories(Vendor vendor){
        List<VendorCategory> vendorCategories = dao.getCondition(VendorCategory.class, "vendorId", vendor.getId());
        vendor.setVendorCategories(vendorCategories);
    }
    private void addContacts(Vendor vendor){
        List<VendorContact> vendorContacts = dao.getCondition(VendorContact.class, "vendorId", vendor.getId());
        vendor.setVendorContacts(vendorContacts);
    }


    private WebApp getWebAppFromAuthHeader(String authHeader) throws Exception {
        String[] values = authHeader.split("&&");
        String appSecret = values[2].trim();
        return getWebAppFromSecret(appSecret);
    }

    private String issueToken(VendorUser vendorUser, WebApp webapp, int expireMinutes) {
        deactivateOldTokens(vendorUser);
        Date tokenTime = new Date();
        AccessToken accessToken = new AccessToken(vendorUser.getId(), tokenTime);
        accessToken.setWebApp(webapp);
        accessToken.setExpire(Helper.addMinutes(tokenTime, expireMinutes));
        accessToken.setStatus('A');
        accessToken.setToken(Helper.getSecuredRandom());
        dao.persist(accessToken);
        return accessToken.getToken();
    }

    private void deactivateOldTokens(VendorUser vendorUser) {
        List<AccessToken> tokens = dao.getTwoConditions(AccessToken.class, "vendorUserId", "status", vendorUser.getId(), 'A');
        for (AccessToken t : tokens) {
            t.setStatus('K');// kill old token
            dao.update(t);
        }
    }



    // retrieves app object from app secret
    private WebApp getWebAppFromSecret(String secret) throws Exception {
        // verify web app secret
        WebApp webApp = dao.findTwoConditions(WebApp.class, "appSecret", "active", secret, true);
        if (webApp == null) {
            throw new Exception();
        }
        return webApp;
    }


    public Response getSecuredRequest(String link, String header) {
        Invocation.Builder b = ClientBuilder.newClient().target(link).request();
        b.header(HttpHeaders.AUTHORIZATION, header);
        Response r = b.get();
        return r;
    }


    public <T> Response postSecuredRequest(String link, T t, String authHeader) {
        Invocation.Builder b = ClientBuilder.newClient().target(link).request();
        b.header(HttpHeaders.AUTHORIZATION, authHeader);
        Response r = b.post(Entity.entity(t, "application/json"));
        return r;
    }



}
