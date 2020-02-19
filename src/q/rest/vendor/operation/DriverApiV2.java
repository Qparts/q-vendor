package q.rest.vendor.operation;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import q.rest.vendor.dao.DAO;
import q.rest.vendor.filter.SecuredUser;
import q.rest.vendor.filter.SecuredUserVendor;
import q.rest.vendor.filter.SecuredVendor;
import q.rest.vendor.filter.ValidApp;
import q.rest.vendor.helper.AppConstants;
import q.rest.vendor.helper.Helper;
import q.rest.vendor.model.contract.*;
import q.rest.vendor.model.contract.qvm.QvmAvailability;
import q.rest.vendor.model.contract.qvm.QvmObject;
import q.rest.vendor.model.contract.qvm.QvmSearchRequest;
import q.rest.vendor.model.contract.qvm.QvmVendorCredentials;
import q.rest.vendor.model.entity.*;
import q.rest.vendor.model.entity.branch.Branch;
import q.rest.vendor.model.entity.branch.BranchContact;
import q.rest.vendor.model.entity.driver.Driver;
import q.rest.vendor.model.entity.driver.DriverCity;
import q.rest.vendor.model.entity.plan.*;
import q.rest.vendor.model.entity.registration.EmailVerification;
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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Path("/internal/api/v2/driver/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DriverApiV2 {

    @EJB
    private DAO dao;


    @Context
    private ServletContext context;

    @EJB
    private AsyncService async;

    @SecuredUserVendor
    @GET
    @Path("drivers")
    public Response getAllVendors() {
        try {
            List<Driver> drivers = dao.getCondition(Driver.class, "status", 'A');
            for(Driver driver : drivers){
                driver.setDriverCities(getDriverCities(driver.getId()));
            }
            return Response.status(200).entity(drivers).build();
        } catch (Exception ex) {
            return Response.status(500).build();
        }
    }

    @SecuredUser
    @POST
    @Path("driver")
    public Response createDriver(Driver driver){
        try{
            Driver check = dao.findCondition(Driver.class, "mobile", driver.getMobile());
            if(check != null){
                return Response.status(409).build();
            }
            driver.setCreated(new Date());
            dao.persist(driver);
            for(DriverCity dc : driver.getDriverCities()){
                dc.setDriverId(driver.getId());
                dao.persist(dc);
            }
            return Response.status(201).build();
        }catch (Exception ex){
            return Response.status(500).build();
        }
    }

    private List<DriverCity> getDriverCities(int driverId){
        return dao.getCondition(DriverCity.class, "driverId", driverId);
    }

}
