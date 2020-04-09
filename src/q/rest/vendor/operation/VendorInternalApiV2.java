package q.rest.vendor.operation;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import q.rest.vendor.dao.DAO;
import q.rest.vendor.filter.*;
import q.rest.vendor.helper.AppConstants;
import q.rest.vendor.helper.Helper;
import q.rest.vendor.model.contract.*;
import q.rest.vendor.model.contract.qvm.*;
import q.rest.vendor.model.entity.*;
import q.rest.vendor.model.entity.branch.Branch;
import q.rest.vendor.model.entity.branch.BranchContact;
import q.rest.vendor.model.entity.plan.*;
import q.rest.vendor.model.entity.registration.ContactUs;
import q.rest.vendor.model.entity.registration.EmailVerification;
import q.rest.vendor.model.entity.registration.PasswordReset;
import q.rest.vendor.model.entity.registration.SignupRequest;
import q.rest.vendor.model.entity.user.*;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.*;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Path("/internal/api/v2/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VendorInternalApiV2 {

    @EJB
    private DAO dao;


    @Context
    private ServletContext context;

    @EJB
    private AsyncService async;



    @GET
    @Path("test")
    @Produces(MediaType.TEXT_HTML)
    public Response testQuotationReadyHtml(){
        Map<String,Object> vmap = new HashMap<>();
        vmap.put("firstName", "Fareed");
        vmap.put("quotationLink", "http://somelink.com");
        vmap.put("quotationId", 50001);
        String body = this.getHtmlTemplate(AppConstants.PASSWORD_RESET_TEMPLATE, vmap);
        return Response.status(200).entity(body).build();
    }


    @SecuredUser
    @GET
    @Path("plan-promotions")
    public Response getPlanPromotions(){
        try{
            String sql = "select b from PlanPromotion b order by b.startDate desc";
            List<PlanPromotion> pps = dao.getJPQLParams(PlanPromotion.class, sql);
            for(PlanPromotion planPromotion : pps){
                if(planPromotion.getStatus() == 'A'){
                    if(planPromotion.getEndDate().before(new Date())){
                        planPromotion.setStatus('E');
                        dao.update(planPromotion);
                    }
                }
            }
            return Response.status(200).entity(pps).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }


    @SecuredVendor
    @GET
    @Path("plan-promotion/code/{code}/vendor/{vendorId}/plan/{planId}/option/{optionId}")
    public Response getPlanPromotion(@PathParam(value = "vendorId") int vendorId,
                                     @PathParam(value = "code") String code,
                                     @PathParam(value = "optionId") int optionId,
                                     @PathParam(value = "planId") int planId){
        try{
            String sql = "select b from PlanPromotion b where b.promoCode = :value0 and b.optionId = :value1 and b.planId = :value2 and b.status = :value3";
            PlanPromotion planPromotion = dao.findJPQLParams(PlanPromotion.class, sql, code, optionId, planId, 'A');
            if(planPromotion == null){
                return Response.status(404).build();
            }

            if(planPromotion.getEndDate().before(new Date())){
                planPromotion.setStatus('E');
                dao.update(planPromotion);
                return Response.status(410).build();
            }

            if(planPromotion.isVendorUnique()){
                if(planPromotion.getVendorId() != vendorId){
                    return Response.status(404).build();
                }
            }
            return Response.status(200).entity(planPromotion).build();
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.status(500).build();
        }
    }

    @SecuredUserVendor
    @GET
    @Path("qvm-invoice/{invoiceId}")
    @Produces(MediaType.TEXT_HTML)
    public Response getHtmlInvoice(@HeaderParam("Authorization") String header, @PathParam(value = "invoiceId") long invoiceId){
        try{
            int vendorId = getVendorUserFromHeader(header).getVendorId();
            PlanSubscription ps = dao.findCondition(PlanSubscription.class, "salesId", invoiceId);

            if(ps == null || ps.getVendorId() != vendorId){
                return Response.status(401).build();
            }
            Response r = this.getSecuredRequest(AppConstants.getQvmSales(invoiceId), header);
            if(r.getStatus() == 200){
                QvmSales qvmSales = r.readEntity(QvmSales.class);
                if(qvmSales.getPaymentOrder().getVendorId() != vendorId){
                    return Response.status(401).build();
                }
                Vendor vendor = dao.find(Vendor.class, vendorId);
                Plan plan = dao.find(Plan.class, qvmSales.getPaymentOrder().getPlanId());
                double subtotal = qvmSales.getPaymentOrder().getBaseAmount() - qvmSales.getPaymentOrder().getPlanDiscount() - qvmSales.getPaymentOrder().getPromoDiscount();
                Helper h = new Helper();
                String paymentMethodAr = "";
                String paymentMethodEn = "";
                if(qvmSales.getPaymentOrder().getPaymentMethod() == 'C'){
                    paymentMethodAr = "بطاقة ائتمانية";
                    paymentMethodEn = "Credit/Debit Card";
                }
                else if (qvmSales.getPaymentOrder().getPaymentMethod() == 'W'){
                    paymentMethodAr = "تحويل بنكي";
                    paymentMethodEn = "Bank Transfer";
                }

                Map<String,Object> vmap = new HashMap<>();
                vmap.put("invoiceNumber", qvmSales.getId());
                vmap.put("orderNumber", qvmSales.getPaymentOrder().getId());

                vmap.put("customerId", vendorId);
                vmap.put("companyEn", vendor.getName());
                vmap.put("paymentMethodEn", paymentMethodEn);
                vmap.put("companyAr", vendor.getNameAr());
                vmap.put("paymentMethodAr", paymentMethodAr);
                vmap.put("duration", qvmSales.getPaymentOrder().getOptionDuration());
                vmap.put("planNameEn", plan.getName());
                vmap.put("planNameAr", plan.getNameAr());
                vmap.put("invoiceDate", h.getDateFormat(qvmSales.getCreated(), "dd-MMM-yyyy"));
                vmap.put("startDate", h.getDateFormat(qvmSales.getPaymentOrder().getPlanStartDate(), "dd-MMM-yyyy"));
                vmap.put("endDate", h.getDateFormat(qvmSales.getPaymentOrder().getPlanEndDate(), "dd-MMM-yyyy"));
                vmap.put("amount", "SR "+ new DecimalFormat("#.##").format(qvmSales.getPaymentOrder().getBaseAmount()));
                vmap.put("discount", "SR " + new DecimalFormat("#.##").format(qvmSales.getPaymentOrder().getPlanDiscount() + qvmSales.getPaymentOrder().getPromoDiscount()));
                vmap.put("subtotal", "SR "+ new DecimalFormat("#.##").format(subtotal));
                vmap.put("qty", "1");
                vmap.put("vatPercentage", (qvmSales.getPaymentOrder().getVatPercentage() *100) + "%");
                vmap.put("vatNumber", AppConstants.VAT_NUMBER);
                vmap.put("vatAmount", "SR "+ new DecimalFormat("#.##").format(subtotal * qvmSales.getPaymentOrder().getVatPercentage()));
                vmap.put("netTotal", "SR "+ new DecimalFormat("#.##").format(subtotal * qvmSales.getPaymentOrder().getVatPercentage() + subtotal));
                String body = this.getHtmlTemplate(AppConstants.SUBSCRIPTION_INVOICE_EMAIL_TEMPLATE2, vmap);
                return Response.status(200).entity(body).build();
            }
            return Response.status(404).build();
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.status(500).build();
        }
    }



    private String createVerificationObject(VendorUser vendorUser) {
        String code = "";
        boolean available = false;
        do {
            code = Helper.getRandomString(20);
            String sql = "select b from EmailVerification b where b.token = :value0 and b.expire >= :value1 and status =:value2";
            List<EmailVerification> l = dao.getJPQLParams(EmailVerification.class, sql, code, new Date(), 'R');
            if (l.isEmpty()) {
                available = true;
            }
        } while (!available);

        EmailVerification ev = new EmailVerification();
        ev.setToken(code);
        ev.setCreated(new Date());
        ev.setVendorId(vendorUser.getVendorId());
        ev.setVendorUserId(vendorUser.getId());
        ev.setExpire(Helper.addMinutes(ev.getCreated(), 60*24*14));
        ev.setEmail(vendorUser.getEmail());
        ev.setStatus('R');
        dao.persist(ev);
        return code;
    }

    private String createPasswordResetObject(VendorUser vendorUser) {
        String code = "";
        boolean available = false;
        do {
            code = Helper.getRandomString(20);
            String sql = "select b from PasswordReset b where b.token = :value0 and b.expire >= :value1 and status =:value2";
            List<PasswordReset> l = dao.getJPQLParams(PasswordReset.class, sql, code, new Date(), 'R');
            if (l.isEmpty()) {
                available = true;
            }
        } while (!available);

        PasswordReset ev = new PasswordReset();
        ev.setToken(code);
        ev.setCreated(new Date());
        ev.setVendorUserId(vendorUser.getId());
        ev.setExpire(Helper.addMinutes(ev.getCreated(), 60*24*14));
        ev.setStatus('R');
        dao.persist(ev);
        return code;
    }


    public String getHtmlTemplate(String templateName, Map<String,Object> map){
        Properties p = new Properties();
        p.setProperty("resource.loader", "webapp");
        p.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.WebappResourceLoader");
        p.setProperty("webapp.resource.loader.path", "/WEB-INF/velocity/");
        VelocityEngine engine = new VelocityEngine(p);
        engine.setApplicationAttribute("javax.servlet.ServletContext", context);
        engine.init();
        Template template = engine.getTemplate(templateName);
        VelocityContext velocityContext = new VelocityContext();
        map.forEach((k,v) -> velocityContext.put(k,v));
        StringWriter writer = new StringWriter();
        template.merge(velocityContext, writer);
        return writer.toString();
    }


    @SecuredUserVendor
    @GET
    @Path("vendors")
    public Response getAllVendors() {
        try {
            List<Vendor> vendors = dao.getOrderBy(Vendor.class, "id");
            for(Vendor vendor : vendors){
                vendor.setVendorCategories(getCategories(vendor.getId()));
                vendor.setVendorContacts(getContacts(vendor.getId()));
                vendor.setBranches(getVendorBranches(vendor.getId()));
                vendor.setSubscriptions(getSubscription(vendor.getId()));
                vendor.setReferrals(getReferrals(vendor.getId()));
            }
            return Response.status(200).entity(vendors).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }

    @SecuredUserVendor
    @GET
    @Path("vendors/detailed")
    public Response getAllVendorsDetailed(){
        try {
            List<Vendor> vendors = dao.getOrderBy(Vendor.class, "id");
            List<VendorHolder> holders = new ArrayList<>();
            for(Vendor vendor : vendors){
                VendorHolder holder = getVendorHolder(vendor);
                holders.add(holder);
            }
            return Response.status(200).entity(holders).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }

    private VendorHolder getVendorHolder(Vendor vendor){
        VendorHolder holder = new VendorHolder();
        holder.setVendor(vendor);
        holder.setAccessTokens(getAccessTokens(vendor.getId()));
        holder.setBranches(getVendorBranches(vendor.getId()));
        holder.setKeywords(getVendorSearchKeywords(vendor.getId()));
        holder.setPlanSubscriptions(getSubscription(vendor.getId()));
        holder.setVendorPolicies(getVendorPolicies(vendor.getId()));
        holder.setReferrals(getReferrals(vendor.getId()));
        holder.setVendorUsers(getVendorUserHolders(vendor.getId()));
        return holder;
    }

    @SecuredUser
    @PUT
    @Path("vendor")
    public Response updateVendor(Vendor vendor){
        try{
            dao.update(vendor);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUserVendor
    @GET
    @Path("vendor/{vendorId}/detailed")
    public Response getVendorDetailed(@PathParam(value = "vendorId") int vendorId) {
        try {
            Vendor vendor = dao.find(Vendor.class, vendorId);
            VendorHolder vendorHolder = getVendorHolder(vendor);
            return Response.status(200).entity(vendorHolder).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }


    @SecuredUser
    @GET
    @Path("vendor/{vendorId}")
    public Response getVendor(@PathParam(value = "vendorId") int vendorId) {
        try {
            Vendor vendor = dao.find(Vendor.class, vendorId);
            vendor.setVendorCategories(getCategories(vendor.getId()));
            vendor.setVendorContacts(getContacts(vendor.getId()));
            vendor.setBranches(getVendorBranches(vendor.getId()));
            vendor.setSubscriptions(getSubscription(vendor.getId()));
            vendor.setReferrals(getReferrals(vendor.getId()));
            return Response.status(200).entity(vendor).build();
        } catch (Exception ex) {
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
    @DELETE
    @Path("vendor-user-role/role/{roleId}/user/{vendorUserId}")
    public Response deleteUserRole(@PathParam(value = "roleId") int roleId, @PathParam(value = "vendorUserId") int vendorUserId){
        try{
            String sql = "delete from vnd_user_role where role_id = " + roleId + " and vendor_user_id = " + vendorUserId;
            dao.updateNative(sql);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @POST
    @Path("vendor-user-role")
    public Response addUserRole(Map<String,Object> map){
        try{
            int roleId = (int) map.get("roleId");
            int vendorUserId = (int) map.get("vendorUserId");
            String sql = "insert into vnd_user_role (role_id, vendor_user_id) values(" + roleId + "," + vendorUserId + ")";
            dao.insertNative(sql);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @POST
    @Path("plan-role")
    public Response addPlanRole(Map<String,Object> map){
        try{
            int roleId = (int) map.get("roleId");
            int planId = (int) map.get("planId");
            String sql = "insert into vnd_plan_role (role_id, plan_id) values(" + roleId + " , " + planId + ")";
            dao.insertNative(sql);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUserVendor
    @GET
    @Path("plans")
    public Response getPlans(){
        try{
            List<Plan> plans = dao.get(Plan.class);
            for(Plan plan : plans){
                initPlan(plan);
            }
            return Response.status(200).entity(plans).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }


    @SecuredUserVendor
    @GET
    @Path("plan/{planId}")
    public Response getPlan(@PathParam(value = "planId") int planId){
        try{
            Plan plan = dao.find(Plan.class, planId);
            initPlan(plan);
            return Response.status(200).entity(plan).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }



    @SecuredUser
    @POST
    @Path("branch-contact")
    public Response createBranchContact(BranchContact bc){
        try{
            String sql = "select b from BranchContact b where " +
                    "b.vendorId =:value0 and " +
                    "b.branchId = :value1 and " +
                    "b.phone =:value2 and " +
                    "b.firstName =:value3 and " +
                    "b.lastName =:value4 and " +
                    "b.email = :value5";
            List<BranchContact> contacts = dao.getJPQLParams(BranchContact.class, sql , bc.getVendorId(), bc.getBranchId(), bc.getPhone(), bc.getFirstName(), bc.getLastName(), bc.getEmail());
            if(!contacts.isEmpty()){
                return Response.status(409).build();
            }
            bc.setCreated(new Date());
            dao.persist(bc);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @POST
    @Path("branch")
    public Response createBranch(Branch branch){
        try{
            String sql = "select b from Branch b where b.vendorId =:value0 and b.name = :value1";
            List<Branch> branches = dao.getJPQLParams(Branch.class, sql , branch.getVendorId(), branch.getName());
            if(!branches.isEmpty()){
                return Response.status(409).build();
            }
            branch.setCreated(new Date());
            dao.persist(branch);
            return Response.status(201).build();
        }catch (Exception ex){
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



    @SecuredUser
    @GET
    @Path("search-keywords/{vendorId}")
    public Response getKeywords(@PathParam(value ="vendorId") int vendorId){
        try{
            String sql = "select b from QvmSearchKeyword b where b.vendorId = :value0 order by b.created desc";
            List<QvmSearchKeyword> keywords = dao.getJPQLParamsMax(QvmSearchKeyword.class, sql ,10, vendorId);
            return Response.status(200).entity(keywords).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @GET
    @Path("vendor-users/{vendorId}")
    public Response getVendorUsers(@PathParam(value = "vendorId") int vendorId){
        try{
            return Response.status(200).entity(getVendorUserHolders(vendorId)).build();
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.status(500).build();
        }
    }

    private List<AccessToken> getAccessTokens(int vendorId){
        String sql = "select b from AccessToken b where b.vendorUserId in (" +
                "select c.id from VendorUser c where c.vendorId = :value0) order by b.created desc";
        return dao.getJPQLParams(AccessToken.class, sql , vendorId);
    }

    private List<VendorUserHolder> getVendorUserHolders(int vendorId){
        String jpql = "select b from VendorUser b where b.vendorId = :value0 order by b.id";
        List<VendorUser> vendorUsers = dao.getJPQLParams(VendorUser.class, jpql, vendorId);
        List<VendorUserHolder> holders = new ArrayList<>();
        for(VendorUser vu : vendorUsers){
            String sql2 = "select b.role from VendorUserRole b where b.vendorUser = :value0";
            List<Role> roles = dao.getJPQLParams(Role.class, sql2, vu);
            VendorUserHolder holder = new VendorUserHolder();
            holder.setRoles(roles);
            holder.setActivities(getUserActivities(vu));
            holder.setVendorUser(vu);
            AccessToken at = dao.findTwoConditions(AccessToken.class, "vendorUserId", "status", vu.getId(), 'A');
            if(at != null) {
                holder.setLastLogin(at.getCreated());
            }
            holders.add(holder);
        }
        return holders;
    }



    @ValidApp
    @POST
    @Path("login")
    public Response login(@HeaderParam("Authorization") String header, Map<String,Object> map) {
        try {
            WebApp webApp = getWebAppFromAuthHeader(header);
            String password = Helper.cypher((String) map.get("password"));
            String email = ((String) map.get("email")).trim().toLowerCase();
            String sql = "select b from VendorUser b where b.status in (:value0, :value1) and b.email = :value2 and b.password = :value3";
            VendorUser vendorUser = dao.findJPQLParams(VendorUser.class, sql, 'A', 'V', email, password);
            if (vendorUser != null) {
                VendorUserHolder holder = getLoginObject(vendorUser, webApp);
                return Response.status(200).entity(holder).build();
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            return Response.status(401).build();
        }
    }

    private VendorUserHolder getLoginObject(VendorUser vendorUser, WebApp webApp){
        VendorUserHolder holder = new VendorUserHolder();
        String sql = "select b from AccessToken b where  b.vendorUserId = :value0 and b.status = :value1 order by b.created desc";
        List<AccessToken> ats = dao.getJPQLParams(AccessToken.class, sql, vendorUser.getId(), 'A');
        if(!ats.isEmpty()) {
            holder.setLastLogin(ats.get(0).getCreated());
        }
        String token = issueToken(vendorUser, webApp, 500);
        String sql2 = "select b.role from VendorUserRole b where b.vendorUser = :value0";
        List<Role> roles = dao.getJPQLParams(Role.class, sql2, vendorUser);
        holder.setRoles(roles);
        holder.setActivities(getUserActivities(vendorUser));
        holder.setVendorUser(vendorUser);
        holder.setToken(token);
        return holder;
    }

    private List<Activity> getUserActivities(VendorUser user) {
        String sql = "select * from vnd_activity a where a.id in ("
                + "select ra.activity_id from vnd_role_activity ra where ra.role_id in ("
                + "select ur.role_id from vnd_user_role ur where ur.vendor_user_id = " + user.getId() + ") ) order by a.id";
        return dao.getNative(Activity.class, sql);
    }

    @SecuredVendor
    @POST
    @Path("price-policy")
    public Response createNewVendorPolicy(PricePolicy pricePolicy){
        try{
            PricePolicy pp = dao.findTwoConditions(PricePolicy.class, "vendorId", "policyName", pricePolicy.getVendorId(), pricePolicy.getPolicyName());
            if(pp != null){
                return Response.status(409).build();
            }
            pricePolicy.setCreated(new Date());
            dao.persist(pricePolicy);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredVendor
    @GET
    @Path("price-policy/vendor/{vendorId}/target/{targetId}")
    public Response getPricePolicyFromVendorAndTargetIds(@PathParam(value = "vendorId") int vendorId, @PathParam(value = "targetId") int targetId){
        try{
            String sql = "select b from PricePolicy b where b.id in (" +
                    "select c.pricePolicyId from VendorPricePolicy c where c.vendorId = :value0 and c.targetVendorId =:value1 )";
            PricePolicy pricePolicy = dao.findJPQLParams(PricePolicy.class, sql, vendorId, targetId);
            if(pricePolicy == null){
                return Response.status(404).build();
            }
            return Response.ok().entity(pricePolicy).build();
        }catch (Exception ex){
            return Response.serverError().build();
        }
    }


    @SecuredVendor
    @DELETE
    @Path("vendor-price-policy/target/{targetId}")
    public Response deleteVendorPricePolicy(@HeaderParam("Authorization") String header, @PathParam(value = "targetId") int targetId){
        try{
            int vendorId = getVendorUserFromHeader(header).getVendorId();
            VendorPricePolicy vpp = dao.findTwoConditions(VendorPricePolicy.class, "vendorId", "targetVendorId", vendorId, targetId);
            dao.delete(vpp);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }


    @SecuredVendor
    @POST
    @Path("vendor-price-policy")
    public Response createVendorPricePolicy(VendorPricePolicy vpp){
        try{
            String sql = "select b from VendorPricePolicy b where b.vendorId =:value0 and b.targetVendorId =:value1";
            VendorPricePolicy vpps = dao.findJPQLParams(VendorPricePolicy.class, sql , vpp.getVendorId(), vpp.getTargetVendorId());
            if(vpps != null){
                return Response.status(409).build();
            }
            vpp.setCreated(new Date());
            dao.persist(vpp);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }

    }

    @SecuredVendor
    @GET
    @Path("vendor-price-policies")
    public Response getVendorAppliedPricePolicies(@HeaderParam("Authorization") String header){
        try{
            int vendorId = getVendorUserFromHeader(header).getVendorId();
            String sql = "select b from VendorPricePolicy b where b.vendorId =:value0 order by b.targetVendorId";
            List<VendorPricePolicy> vpps = dao.getJPQLParams(VendorPricePolicy.class, sql , vendorId);
            return Response.status(200).entity(vpps).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }

    }


    @SecuredVendor
    @GET
    @Path("price-policies")
    public Response getVendorPricePolicies(@HeaderParam("Authorization") String header){
        try{
            int vendorId = getVendorUserFromHeader(header).getVendorId();
            String sql = "select b from PricePolicy b where b.vendorId = :value0 order by b.created desc";
            List<PricePolicy> policies = dao.getJPQLParams(PricePolicy.class, sql , vendorId);
            return Response.status(200).entity(policies).build();
        }
        catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @ValidApp
    @PUT
    @Path("reset-password")
    public Response resetPassword(@HeaderParam("Authorization") String authHeader, Map<String, String> map){
        try{
            WebApp webApp = getWebAppFromAuthHeader(authHeader);
            String token = map.get("token");
            String password = map.get("newPassword");
            String sql = "select b from PasswordReset b where b.token = :value0 and b.status = :value1";
            PasswordReset pr = dao.findJPQLParams(PasswordReset.class, sql , token, 'R');
            if(pr == null){
                return Response.status(404).build();
            }
            VendorUser vu = dao.find(VendorUser.class, pr.getVendorUserId());
            vu.setPassword(Helper.cypher(password));
            dao.update(vu);
            VendorUserHolder holder  = getLoginObject(vu, webApp);
            pr.setStatus('V');
            dao.update(pr);
            return Response.status(200).entity(holder).build();
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.status(500).build();
        }



    }

    @ValidApp
    @POST
    @Path("reset-password-verify")
    public Response verifyPasswordReset(Map<String,String> map){
        try{
            String token = map.get("token");
            String sql = "select b from PasswordReset b where b.token = :value0 and b.status = :value1";
            PasswordReset pr = dao.findJPQLParams(PasswordReset.class, sql, token, 'R');
            if(pr == null){
                return Response.status(404).build();
            }
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @ValidApp
    @POST
    @Path("email-verify")
    public Response verifyAccount(@HeaderParam("Authorization") String authHeader, Map<String ,String> map){
        try{
            WebApp webApp = getWebAppFromAuthHeader(authHeader);
            String code = map.get("code");
            String email = map.get("email");
            String sql = "select b from EmailVerification b where b.token = :value0 " +
                    " and b.email = :value1";
            EmailVerification ev = dao.findJPQLParams(EmailVerification.class, sql, code, email);
            if(ev == null){
                return Response.status(404).build();
            }
            if(ev.getExpire().before(new Date())){
                ev.setStatus('E');
                dao.update(ev);
                //delete verification
                return Response.status(410).entity("Resource Expired").build();
            }
            VendorUser vu = dao.find(VendorUser.class, ev.getVendorUserId());
            vu.setStatus('V');//verified
            dao.update(vu);
            ev.setStatus('V');
            dao.update(ev);
            VendorUserHolder holder = getLoginObject(vu, webApp);
            return Response.status(200).entity(holder).build();
        }catch(Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @POST
    @Path("send-verification")
    public Response sendVerification(VendorUser vendorUser){
        try{
            String activationCode = createVerificationObject(vendorUser);
            String subject2 = "Verify your email address - توثيق البريد الإلكتروني";
            Map<String,Object> map2 = new HashMap<>();
            map2.put("activationLink", AppConstants.getActivationLink(vendorUser.getEmail(), activationCode));
            String body2 = this.getHtmlTemplate(AppConstants.EMAIL_VERIFICATION_EMAIL_TEMPLATE, map2);
            async.sendHtmlEmail(vendorUser.getEmail(), subject2, body2);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @GET
    @Path("contact-us/pending")
    public Response getPendingContactUs(){
        try{
            String sql = "select b from ContactUs b where b.status = :value0 order by b.created asc";
            List<ContactUs> contactUsList = dao.getJPQLParamsMax(ContactUs.class, sql, 'N');
            return Response.status(200).entity(contactUsList).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @PUT
    @Path("contact-us")
    public Response updateContactUs(ContactUs contactUs){
        try{
            contactUs.setStatus('P');//Processed
            dao.update(contactUs);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @ValidApp
    @POST
    @Path("contact-us")
    public Response createContactUs(ContactUs contactUs){
        try{
            contactUs.setCreated(new Date());
            dao.persist(contactUs);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }


    @SecuredUser
    @PUT
    @Path("approve-vendor")
    public Response approveVendor(@HeaderParam("Authorization") String header, SignupHolder holder){
        try{
            String sql = "select b from SignupRequest b where b.id = :value0 and b.status = :value1";
            SignupRequest check = dao.findJPQLParams(SignupRequest.class, sql, holder.getSignupRequest().getId(), 'N');//new
            if(check == null){
                return Response.status(429).build();
            }
            holder.getSignupRequest().setStatus('A');//approved
            dao.update(holder.getSignupRequest());
            VendorUser vendorUser= null;
            PlanSubscription planSubscription = null;
            if(holder.isNewVendor()){
                Vendor vendor = createVendor(holder.getSignupRequest());
                vendorUser = createVendorUser(holder.getSignupRequest(), vendor);
                createBranch(holder.getSignupRequest(), vendor);
                planSubscription = createSubscription(vendor, holder);
                createReferralFromVendor(holder.getSignupRequest(), vendor);
            } else{
                Vendor vendor = dao.find(Vendor.class, holder.getExistingVendorId());
                //new to qvm
                if(vendor.getIntegrationType() == null){
                    vendor.setIntegrationType('V');
                    dao.update(vendor);
                    vendorUser = createVendorUser(holder.getSignupRequest(), vendor);
                    planSubscription = createSubscription(vendor, holder);
                    createReferralFromVendor(holder.getSignupRequest(), vendor);
                }
                else{
                    vendorUser = createVendorUser(holder.getSignupRequest(), vendor);
                    planSubscription = dao.findTwoConditions(PlanSubscription.class, "status", "vendorId", 'A', vendor.getId());
                }
                createBranch(holder.getSignupRequest(), vendor);
            }

            createRole(vendorUser, planSubscription);
            List<PlanReferral> refs = getReferrals(planSubscription);


            Map<String,Object> map = new HashMap<>();
            map.put("firstName", vendorUser.getFirstName());
            map.put("invitationCode", refs.get(0).getInvitationCode());
            map.put("invitations", refs.size());
            String subject = "Your Free Trial is Activated - تم تفعيل باقتكم التجريبية";
            String body = this.getHtmlTemplate(AppConstants.VENDOR_APPROVED_EMAIL_TEMPLATE, map);
            async.sendHtmlEmail(vendorUser.getEmail(), subject, body);
            sendVerification(vendorUser);
            return Response.status(200).build();
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.status(500).build();
        }
    }

    private void createReferralFromVendor(SignupRequest signupRequest, Vendor vendor){
        //check referral
        String referralCode = signupRequest.getReferralCode();
        if(referralCode != null && referralCode.length() > 0){
            String sql2 = "select b from PlanReferral b where b.status =:value0 and b.invitationCode =:value1 order by b.id";
            List<PlanReferral> referrals = dao.getJPQLParamsMax(PlanReferral.class, sql2, 1, 'A', referralCode);
            if(!referrals.isEmpty()){
                PlanReferral ref = referrals.get(0);
                ref.setTargetVendorId(vendor.getId());
                ref.setStatus('P');
                dao.update(ref);
            }
        }
    }

    private List<PlanReferral> getReferrals(PlanSubscription ps){
        List<PlanReferral> referrals = dao.getTwoConditions(PlanReferral.class, "subscriptionId", "status", ps.getId(), 'A');
        return referrals;
    }

    private PlanSubscription createSubscription(Vendor vendor, SignupHolder signupHolder){
        Plan plan = dao.find(Plan.class, signupHolder.getPlanId());
        PlanOption option = dao.find(PlanOption.class, signupHolder.getOptionId());
        List<PlanOffer> offers = dao.getTwoConditions(PlanOffer.class, "planId", "planOption.id", plan.getId(), option.getId());
        PlanSubscription ps = new PlanSubscription();
        ps.setStatus('I');
        ps.setCreatedBy(signupHolder.getCreatedBy());
        ps.setOptionId(option.getId());
        ps.setPlanId(plan.getId());
        ps.setVendorId(vendor.getId());
        ps.setCreated(new Date());
        ps.setStartDate(new Date());
        ps.setEndDate(new Date());
        dao.persist(ps);
        for(PlanOffer offer : offers){
            if(offer.getOfferType() == 'S'){
                Date endDate = Helper.addMinutes(ps.getStartDate(), 60*24*offer.getDurationDays());
                ps.setEndDate(endDate);
            }
            if(offer.getOfferType() == 'R'){
                this.createReferrals(ps, offer);
            }
        }
        ps.setStatus('A');
        dao.update(ps);
        return ps;
    }

    private void createRole(VendorUser vendorUser, PlanSubscription planSubscription){
        String sql = "select b.role from PlanRole b where b.plan.id = :value0";
        List<Role> roles = dao.getJPQLParams(Role.class, sql, planSubscription.getPlanId());
        for(Role role : roles){
            String sql2 = "insert into vnd_user_role (role_id, vendor_user_id) values(" + role.getId() + "," + vendorUser.getId() + ")";
            dao.insertNative(sql2);
        }
    }

    private void createBranch(SignupRequest sr, Vendor vendor){
        Branch branch = dao.findTwoConditions(Branch.class, "vendorId", "name", vendor.getId(), "Main Branch");
        if(branch == null){
            //create branch
            Branch b = new Branch();
            b.setCityId(sr.getCityId());
            b.setVendorId(vendor.getId());
            b.setCountryId(sr.getCountryId());
            b.setName("Main Branch");
            b.setNameAr("الفرع الرئيسي");
            b.setCreated(new Date());
            b.setCreatedBy(0);
            b.setStatus('A');
            dao.persist(b);
        }
    }

    private Vendor createVendor(SignupRequest sr){
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
        return vendor;
    }

    private VendorUser createVendorUser(SignupRequest sr, Vendor vendor){
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
        return u;
    }


    @ValidApp
    @POST
    @Path("password-reset-request")
    public Response RequestPasswordReset(Map<String,String> map){
       try{
           String sql = "select b from VendorUser b where b.email = :value0";
           VendorUser vendorUser = dao.findJPQLParams(VendorUser.class, sql, map.get("email").trim().toLowerCase());
           if(vendorUser != null){
               String token = createPasswordResetObject(vendorUser);
               Map<String, Object> vmap = new HashMap<>();
               vmap.put("firstName", vendorUser.getFirstName());
               vmap.put("passwordResetLink", AppConstants.getPasswordResetLink(token));
               String subject = "Password Reset - إعادة تهيئة كلمة المرور";
               String body = this.getHtmlTemplate(AppConstants.PASSWORD_RESET_TEMPLATE, vmap);
               async.sendHtmlEmail(vendorUser.getEmail(), subject, body);
           }
           return Response.status(201).build();
       }catch (Exception ex){
           return Response.status(500).build();
       }
    }



    @ValidApp
    @POST
    @Path("signup-request")
    public Response signupRequest(@HeaderParam("Authorization") String header, SignupRequest signupRequest) {
        try {
            signupRequest.setEmail(signupRequest.getEmail().trim().toLowerCase());
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
            Map<String,Object> map = new HashMap<>();
            map.put("firstName", signupRequest.getFirstName());
            String subject = "Thank you for signing up application- شكرا لطلب انتضمامكم";
            String body = this.getHtmlTemplate(AppConstants.REGISTRATION_COMPLETE_EMAIL_TEMPLATE, map);
            async.sendHtmlEmail(signupRequest.getEmail(), subject, body);
            return Response.status(201).build();
        } catch (Exception ex) {
            ex.printStackTrace();
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
                            String endpoint = vendor.getHealthCheckAddress();
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


    @SecuredUserVendor
    @POST
    @Path("subscribe")
    public Response createSubscription(QvmSales qvmSales){
        try{
            List<PlanSubscription> subs = getSubscription(qvmSales.getPaymentOrder().getVendorId());
            PlanSubscription last = null;
            for(var sub : subs){
                if(sub.getStatus() == 'A'){
                    last = sub;
                    break;
                }
            }
            PlanSubscription newSub = new PlanSubscription();
            newSub.setStatus('A');
            newSub.setCreated(new Date());
            newSub.setVendorId(qvmSales.getPaymentOrder().getVendorId());
            newSub.setPlanId(qvmSales.getPaymentOrder().getPlanId());
            newSub.setSalesId(qvmSales.getId());

            Plan plan = dao.find(Plan.class, qvmSales.getPaymentOrder().getPlanId());
            initPlan(plan);
            newSub.setOptionId(qvmSales.getPaymentOrder().getOptionId());
            newSub.setCreatedBy(0);
            if(last == null){
                //new subscription
                newSub.setStartDate(qvmSales.getPaymentOrder().getPlanStartDate());
                newSub.setEndDate(qvmSales.getPaymentOrder().getPlanEndDate());
                dao.persist(newSub);
            }
            else{
                //migrate old subscription to new one
                last.setStatus('E');
                dao.update(last);
                //check how many days remaining in last
                Helper h = new Helper();
                long diffDays = ChronoUnit.DAYS.between(h.convertToLocalDate(new Date()), h.convertToLocalDate(last.getEndDate()));
                Date newEndDate = Helper.addDays(qvmSales.getPaymentOrder().getPlanEndDate(), diffDays);
                newSub.setEndDate(newEndDate);
                newSub.setStartDate(new Date());
                dao.persist(newSub);
            }
            return Response.status(200).entity(getSubscription(qvmSales.getPaymentOrder().getVendorId())).build();
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @POST
    @Path("subscription/free-trial")
    public Response createFreeTrial(PlanSubscription planSubscription){
        try{
            List<PlanSubscription> check = dao.getTwoConditions(PlanSubscription.class, "status", "vendorId", 'A', planSubscription.getVendorId());
            if(!check.isEmpty()){
                //already subscribed
                return Response.status(409).build();
            }
            planSubscription.setCreated(new Date());
            planSubscription.setStartDate(new Date());
            planSubscription.setStatus('I');//Inactive
            dao.persist(planSubscription);
            List<PlanOffer> offers = dao.getTwoConditions(PlanOffer.class, "planId" , "planOption.id", planSubscription.getPlanId(), planSubscription.getOptionId());
            for(PlanOffer offer : offers){
                if(offer.getOfferType() == 'S'){
                    //free trial
                    Date endDate = Helper.addMinutes(planSubscription.getStartDate(), 60*24*offer.getDurationDays());
                    planSubscription.setEndDate(endDate);
                }
                if(offer.getOfferType() == 'R'){
                    createReferrals(planSubscription, offer);
                }
            }
            planSubscription.setStatus('A');
            dao.update(planSubscription);
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }


    private void createReferrals(PlanSubscription planSubscription, PlanOffer offer){
        String random = Helper.getRandomString(6);
        boolean found = true;
        while(found){
            PlanReferral pr = dao.findCondition(PlanReferral.class, "invitationCode", random);
            if(pr == null){
                found = false;
            }
        }
        for(int i = 0; i < offer.getInvitations(); i++){
            PlanReferral ref = new PlanReferral();
            ref.setVendorId(planSubscription.getVendorId());
            ref.setCreated(new Date());
            ref.setCreatedBy(planSubscription.getCreatedBy());
            ref.setStatus('A');
            ref.setSubscriptionId(planSubscription.getId());
            ref.setInvitationCode(random);
            dao.persist(ref);
        }
    }

    private void initPlan(Plan plan){
        if(plan != null) {
            List<PlanOption> planOptions = dao.getCondition(PlanOption.class, "planId", plan.getId());
            List<PlanOffer> planOffers = dao.getCondition(PlanOffer.class, "planId", plan.getId());
            String sql = "select b.role from PlanRole b where b.plan =:value0";
            List<Role> roles = dao.getJPQLParams(Role.class, sql, plan);
            for(Role role : roles){
                sql = "select b.activity from RoleActivity b where b.role = :value0";
                List<Activity> acts = dao.getJPQLParams(Activity.class, sql, role);
                role.setActivityList(acts);
            }
            plan.setRoles(roles);
            plan.setPlanOptions(planOptions);
            plan.setPlanOffers(planOffers);
        }
    }

    private void saveSearchKeyword(String query, Integer vendorUserId, Integer vendorId, WebApp webApp){
        if(query.length() > 0) {
            QvmSearchKeyword sk = new QvmSearchKeyword();
            sk.setCreated(new Date());
            sk.setQuery(query);
            sk.setVendorUserId(vendorUserId);
            sk.setVendorId(vendorId);
            sk.setAppCode(webApp.getAppCode());
            dao.persist(sk);
        }
    }




    @SecuredUser
    @GET
    @Path("latest-searches")
    public Response getSearchKeywords(){
        try{
            List<QvmSearchKeyword> kwds = dao.getOrderByOriented(QvmSearchKeyword.class, "created", "desc", 50);
            return Response.status(200).entity(kwds).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @GET
    @Path("vendor-joined/from/{from}/to/{to}")
    public Response getVendorsJoinedDate(@PathParam(value = "from") long fromLong, @PathParam(value = "to") long toLong){
        try{
            Helper h = new Helper();
            Date toDate = new Date(toLong);
            Date fromDate = new Date(fromLong);
            List<Date> dates = h.getAllDatesBetween(fromDate, toDate);
            List<VendorsDateGroup> vdgs = new ArrayList<>();
            for(Date date : dates){
                String sql = "select b from Vendor b where cast(b.created as date) = cast(:value0 as date)";
                List<Vendor> dailyVendors = dao.getJPQLParams(Vendor.class, sql, date);
                String sql2 = "select b from Vendor b where cast(b.created as date) between cast(:value0 as date) and cast(:value1 as date)";
                List<Vendor> totalVendors = dao.getJPQLParams(Vendor.class, sql2, fromDate, date);
                VendorsDateGroup vdg = new VendorsDateGroup();
                vdg.setDate(date);
                vdg.setDailyVendors(dailyVendors);
                vdg.setTotalVendors(totalVendors);
                vdgs.add(vdg);
            }
            return Response.status(200).entity(vdgs).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @GET
    @Path("latest-searches-date/from/{from}/to/{to}")
    public Response getSearchKeywordsDate(@PathParam(value = "from") long fromLong, @PathParam(value = "to") long toLong){
        try{
            Helper h = new Helper();
            List<Date> dates = h.getAllDatesBetween(new Date(fromLong) , new Date(toLong));
            List<KeywordGroup> kgs = new ArrayList<>();
            for(Date date : dates){
                String sql = "select count(*) from QvmSearchKeyword b where cast(b.created as date) = cast(:value0 as date)";
                Number n = dao.findJPQLParams(Number.class, sql, date);
                KeywordGroup kg = new KeywordGroup();
                kg.setCount(n.intValue());
                kg.setLastSearch(date);
                kgs.add(kg);
            }
            return Response.status(200).entity(kgs).build();
        }
        catch (Exception ex){
            return Response.status(500).build();
        }

    }

    /**
     * Active vendor is the vendor that was active at least 5 days of the the past 7 days
     */
    @SecuredUser
    @GET
    @Path("search-keywords/active-vendors")
    public Response getActiveVendors(){
        try{
            Date tenDaysAgo = Helper.addMinutes(new Date(), -60*24*10);
            Helper h = new Helper();
            String dateString = h.getDateFormat(tenDaysAgo, "yyyy-MM-dd");
            String sql ="select * from vnd_vendor v where v.created < '"+dateString+"' and 4 < " +
                    " (select count (*) from (select cast(created as date), count(*) from vnd_search_keyword where vendor_id = v.id and created > '"+dateString+"' group by cast(created as date) order by created ) x)";
            List<Vendor> vendors = dao.getNative(Vendor.class, sql);
            return Response.status(200).entity(vendors).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }



    /**
     * Inactive vendor is the vendor that did not search anything in the past 10 days and is older than 10 days
     */
    //@SecuredUser
    @GET
    @Path("search-keywords/inactive-vendors")
    public Response getInactiveVendors(){
        try{
            Date tenDaysAgo = Helper.addMinutes(new Date(), -60*24*10);
            String sql = "select b from Vendor b where b.created < :value0 and b.integrationType is not null and b.id not in (" +
                    "select c.vendorId from QvmSearchKeyword c where c.created > :value0)";
            List<Vendor> vendors = dao.getJPQLParams(Vendor.class, sql , tenDaysAgo);
            return Response.status(200).entity(vendors).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }



    /**
     * Somehow active vendor is the vendor that searched at least once but less than four days in the past 10 days
     */
    @SecuredUser
    @GET
    @Path("search-keywords/somehow-active-vendors")
    public Response getSomehowActiveVendors(){
        try{
            Date tenDaysAgo = Helper.addMinutes(new Date(), -60*24*10);
            Helper h = new Helper();
            String dateString = h.getDateFormat(tenDaysAgo, "yyyy-MM-dd");
            String sql ="select * from vnd_vendor v where v.created < '"+dateString+"' and " +
                    " (select count (*) from (select cast(created as date), count(*) from vnd_search_keyword where vendor_id = v.id and created > '"+dateString+"' group by cast(created as date) order by created ) x) between 1 and 3";
            List<Vendor> vendors = dao.getNative(Vendor.class, sql);
            return Response.status(200).entity(vendors).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @GET
    @Path("latest-searches-date/from/{from}/to/{to}/vendor/{vendorId}")
    public Response getVendorSearchKeywordsDate(@PathParam(value = "from") long fromLong, @PathParam(value = "to") long toLong, @PathParam(value = "vendorId") int vendorId){
        try{
            Helper h = new Helper();
            List<Date> dates = h.getAllDatesBetween(new Date(fromLong) , new Date(toLong));
            List<KeywordGroup> kgs = new ArrayList<>();
            for(Date date : dates){
                String sql = "select count(*) from QvmSearchKeyword b where cast(b.created as date) = cast(:value0 as date) and b.vendorId = :value1";
                Number n = dao.findJPQLParams(Number.class, sql, date, vendorId);
                KeywordGroup kg = new KeywordGroup();
                kg.setCount(n.intValue());
                kg.setLastSearch(date);
                kgs.add(kg);
            }
            return Response.status(200).entity(kgs).build();
        }
        catch (Exception ex){
            return Response.status(500).build();
        }

    }

    @SecuredUser
    @GET
    @Path("latest-search-group/vendor")
    public Response getLatestVendorSearchesGroup(){
        try{
            Helper h = new Helper();
            String dateString = h.getDateFormat(new Date(), "yyyy-MM-dd");
            String sql = "select z.* from (select vendor_id, count(*) from vnd_search_keyword where created > '"+dateString+"' group by vendor_id order by count desc) z";
            List<Object> ss = dao.getNativeMax(sql, 10);
            List<VendorSearchCount> vscs = new ArrayList<>();
            for(Object o : ss){
                if(o instanceof Object[]){
                    Object[] objArray = (Object[]) o;
                    int vendorId =  ((Number) objArray[0]).intValue();
                    int count =  ((Number) objArray[1]).intValue();
                    VendorSearchCount vsc = new VendorSearchCount();
                    Vendor vendor = dao.find(Vendor.class, vendorId);
                    vsc.setCount(count);
                    vsc.setVendor(vendor);
                    vscs.add(vsc);
                }
            }
            return Response.status(200).entity(vscs).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @GET
    @Path("latest-searches-group")
    public Response getSearchKeywordGroups(){
        try{
            String sql = "select query, count(*) from vnd_search_keyword where length(query) > 0 group by query order by count desc";
            List<Object> ss = dao.getNativeMax(sql, 100);
            List<KeywordGroup> kgs = new ArrayList<>();
            for(Object o : ss){
                if(o instanceof Object[]){
                    Object[] objArray = (Object[]) o;
                    String keyword = (String) objArray[0];
                    int count = ((Number) objArray[1]).intValue();
                    KeywordGroup kg = new KeywordGroup();
                    kg.setCount(count);
                    kg.setKeyword(keyword);
                    sql = "select * from vnd_search_keyword where query = '" + keyword +"' order by created desc";
                    List<QvmSearchKeyword> qvmSearchKeyword = dao.getNativeMax(QvmSearchKeyword.class, sql, 1);
                    if(!qvmSearchKeyword.isEmpty()){
                        kg.setLastSearch(qvmSearchKeyword.get(0).getCreated());
                    }

                    sql = "select * from vnd_vendor where id in (select distinct vendor_id from vnd_search_keyword where query = '"+keyword+"')";
                    List<Vendor> vendors = dao.getNative(Vendor.class, sql);
                    kg.setVendors(vendors);
                    kgs.add(kg);
                }
            }
            return Response.status(200).entity(kgs).build();
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.status(500).build();
        }
    }

    @SecuredUserVendor
    @Path("search-parts")
    @POST
    public Response searchParts(@HeaderParam("Authorization") String header, Map<String,Object> map){
        try{
            String query = Helper.undecorate((String) map.get("query"));
            Response r = postSecuredRequest(AppConstants.POST_QVM_SEARCH_PARTS, query, header);
            if(r.getStatus() != 200) {
                throw new Exception();
            }
            Object o = r.readEntity(Object.class);
            return Response.status(200).entity(o).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @SecuredUserVendor
    @Path("search-availability")
    @POST
    public Response searchAvailabilitty(@HeaderParam("Authorization") String header, Map<String, Object> map){
        try{
            WebApp webApp = getWebAppFromAuthHeader(header);
            String query = Helper.undecorate((String) map.get("query"));
            int userId = ((Number) map.get("userId")).intValue();
            int vendorId = ((Number) map.get("vendorId")).intValue();
            boolean attachProduct = (Boolean) map.get("attachProduct");
            QvmSearchRequest searchRequest = new QvmSearchRequest();
            searchRequest.setVendorCreds(new ArrayList<>());
            searchRequest.setQuery(query);
            searchRequest.setAttachProduct(attachProduct);
            searchRequest.setRequesterId(userId);
            if(vendorId > 0) {
                saveSearchKeyword(query, userId, vendorId, webApp);
                searchRequest.setRequesterType('V');
            }
            else{
                searchRequest.setRequesterType('U');
            }

            List<Vendor> vendors = dao.getTwoConditions(Vendor.class, "integrationType", "status", 'I', 'A');
            for(Vendor vendor: vendors){
                QvmVendorCredentials creds = new QvmVendorCredentials();
                creds.setEndpointAddress(vendor.getEndpointAddress());
                creds.setSecret(vendor.getIntegrationSecretCode());
                creds.setVendorId(vendor.getId());
                searchRequest.getVendorCreds().add(creds);
            }
            Response r = postSecuredRequest(AppConstants.POST_QVM_SEARCH_AVAILABILITY, searchRequest, header);
            if(r.getStatus() != 200) {
                throw new Exception();
            }
            List<QvmObject> list = r.readEntity(new GenericType<List<QvmObject>>(){});
            updateQvmObjectWithBranches(list);
            return Response.status(200).entity(list).build();
        }catch (Exception ex){
           return Response.status(500).build();
        }
    }

    @SecuredUserVendor
    @PUT
    @Path("search-availability/update-branches")
    public Response repopulateQvmObjects(List<QvmObject> list){
        try{
            updateQvmObjectWithBranches(list);
            return Response.status(200).entity(list).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }

    }

    public void updateQvmObjectWithBranches(List<QvmObject> list) throws Exception{
        try{
            for(QvmObject o : list){
                if(o.getAvailability() != null){
                    for(QvmAvailability av : o.getAvailability()){
                        try{
                            if(o.getSource() == 'L') {
                                Branch branch = dao.findTwoConditions(Branch.class, "vendorId", "clientBranchId", o.getVendorId(), av.getBranch().getBranchId());
                                if(branch != null){
                                    branch.setBranchContacts(getBranchContacts(branch.getId(), o.getVendorId()));
                                }
                                av.getBranch().setLocalBranch(branch);
                            }
                            else if (o.getSource() == 'U' || o.getSource() == 'S'){
                                Branch branch = dao.find(Branch.class, av.getBranch().getqBranchId());
                                if(branch != null){
                                    branch.setBranchContacts(getBranchContacts(branch.getId(), o.getVendorId()));
                                }
                                av.getBranch().setLocalBranch(branch);
                            }
                        }catch (Exception ignore){

                        }
                    }
                }
            }
        }catch (Exception x){
            x.printStackTrace();
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



    @POST
    @Path("match-token/ws")
    public Response matchTokenWs(Map<String, Object> map) {
        try {
            String token = ((String) map.get("token"));
            int vendorUserId = ((Number) map.get("username")).intValue();
            String jpql = "select b from AccessToken b where b.vendorUserId = :value0 and b.status = :value1 and b.token = :value2 and b.expire > :value3";
            List<AccessToken> l = dao.getJPQLParams(AccessToken.class, jpql, vendorUserId, 'A', token, new Date());
            if (!l.isEmpty()) {
                return Response.status(200).build();
            } else {
                throw new Exception();
            }
        }catch(Exception ex) {
            ex.printStackTrace();
            return Response.status(403).build();// unauthorized
        }
    }

    @SecuredVendor
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

    private List<VendorCategory> getCategories(int vendorId){
       return dao.getCondition(VendorCategory.class, "vendorId", vendorId);
    }
    private List<VendorContact> getContacts(int vendorId){
        return dao.getCondition(VendorContact.class, "vendorId", vendorId);
    }

    private List<Branch> getVendorBranches(int vendorId){
        List<Branch> branches = dao.getCondition(Branch.class, "vendorId", vendorId);
        for(Branch branch : branches){
            branch.setBranchContacts(getBranchContacts(branch.getId(), vendorId));
        }
        return branches;
    }

    private List<BranchContact> getBranchContacts(int branchId, int vendorId){
        return dao.getTwoConditions(BranchContact.class, "vendorId", "branchId", vendorId, branchId);
    }


    private List<VendorUser> getAllVendorUsers(int vendorId){
        return dao.getCondition(VendorUser.class, "vendorId", vendorId);
    }


    private List<VendorPricePolicy> getVendorPolicies(int vendorId){
        String sql = "select b from VendorPricePolicy b where b.vendorId = :value0";
        List<VendorPricePolicy> pricePolicies = dao.getJPQLParams(VendorPricePolicy.class, sql, vendorId);
        for(var vpp : pricePolicies){
            PricePolicy pp = dao.find(PricePolicy.class, vpp.getPricePolicyId());
            vpp.setPricePolicy(pp);
        }
        return pricePolicies;
    }

    private List<QvmSearchKeyword> getVendorSearchKeywords(int vendorId){
        String sql = "select b from QvmSearchKeyword b where b.vendorId =: value0 order by created desc";
        return dao.getJPQLParams(QvmSearchKeyword.class, sql, vendorId);
    }

    private List<PlanSubscription> getSubscription(int vendorId){
        String sql = "select b from PlanSubscription b where b.vendorId = :value0 order by b.endDate desc";
        List<PlanSubscription> subs = dao.getJPQLParams(PlanSubscription.class, sql, vendorId);
        for(PlanSubscription ps : subs){
            if(ps.getStatus() == 'A'){
                if(ps.getEndDate().before(new Date())){
                    ps.setStatus('E');
                    dao.update(ps);
                }
            }
        }
        return subs;
    }

    private List<PlanReferral> getReferrals(int vendorId){
        String sql = "select b from PlanReferral b where b.vendorId = :value0 order by b.id";
        return dao.getJPQLParams(PlanReferral.class, sql, vendorId);
    }



    private VendorUser getVendorUserFromHeader(String header) throws Exception {
        try {
            String[] values = header.split("&&");
            String username = values[1].trim();
            VendorUser vu = dao.find(VendorUser.class, Integer.parseInt(username));
            return vu;
        } catch (Exception ex) {
            return null;
        }
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


    @SecuredUser
    @GET
    @Path("roles")
    public Response getAllRoles() {
        try {
            List<Role> roles = dao.get(Role.class);
            for (Role role : roles) {
                role.setActivityList(this.getRoleActivities(role));
            }
            return Response.status(200).entity(roles).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }


    // idempotent
    @SecuredUser
    @POST
    @Path("/role")
    public Response createRole(Role role) {
        try {
            List<Role> roles = dao.getCondition(Role.class, "name", role.getName());
            if (!roles.isEmpty()) {
                return Response.status(409).build();
            }

            Role r = new Role();
            r.setName(role.getName());
            r.setNameAr(role.getNameAr());
            r.setStatus(role.getStatus());
            dao.persist(r);
            for (Activity act : role.getActivityList()) {
                if (act.isAccess()) {
                    String sql = "insert into vnd_role_activity (role_id, activity_id) values" + "(" + r.getId() + ","
                            + act.getId() + ") on conflict do nothing";
                    dao.insertNative(sql);
                }
            }
            return Response.status(200).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }



    @SecuredUser
    @GET
    @Path("/all-activities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllActivities() {
        try {
            List<Activity> act = dao.get(Activity.class);
            return Response.status(200).entity(act).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }


    @SecuredUser
    @PUT
    @Path("/role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRole(Role role) {
        try {
            List<Activity> all = role.getActivityList();
            dao.update(role);
            for (Activity activity : all) {
                if (activity.isAccess()) {
                    String sql = "insert into vnd_role_activity(role_id, activity_id) values " + "(" + role.getId()
                            + "," + activity.getId() + ") on conflict do nothing";
                    dao.insertNative(sql);
                } else {
                    String sql = "delete from vnd_role_activity where role_id = " + role.getId() + " and activity_id = "
                            + activity.getId();
                    dao.updateNative(sql);
                }
            }
            dao.update(role);
            return Response.status(200).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }



    private List<Activity> getRoleActivities(Role role) {
        List<Activity> allActs = dao.getOrderBy(Activity.class, "name");
        for (Activity a : allActs) {
            RoleActivity roleAct = dao.findTwoConditions(RoleActivity.class, "role", "activity", role, a);
            if (roleAct != null) {
                a.setAccess(true);
            }
        }
        return allActs;
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
