package q.rest.vendor.operation;

import q.rest.vendor.dao.DAO;
import q.rest.vendor.filter.SecuredUser;
import q.rest.vendor.model.entity.Courier;
import q.rest.vendor.model.entity.Vendor;
import q.rest.vendor.model.entity.VendorCategory;
import q.rest.vendor.model.entity.VendorContact;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;


@Path("/internal/api/v2/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VendorInternalApiV2 {

    @EJB
    private DAO dao;

    @SecuredUser
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
}
