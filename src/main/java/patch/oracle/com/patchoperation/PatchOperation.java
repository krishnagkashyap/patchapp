package patch.oracle.com.patchoperation;

/**
 * Created by kgurupra on 5/24/2016.
 */

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.model.Resource;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import patch.oracle.com.beans.CommandBean;
import patch.oracle.com.beans.Component;
import patch.oracle.com.beans.JsonInputBean;
import patch.oracle.com.beans.PatchInputBean;

/**
 * Servlet implementation class MDCConfigServlet
 */

@Path("/patchoperation")
public class PatchOperation {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(PatchOperation.class.getName());


    public PatchOperation() {
        super();
        // TODO Auto-generated constructor stub
        logger.info("INSIDE PatchOperation constructor");
    }


    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/wgdata")
    public Response patchOperation(String wgdata) throws IOException, JSONException, InterruptedException {
        Gson gson = new Gson();
        // Convert JSON to Java Object
        JsonInputBean jsonInputBean = gson.fromJson(wgdata, JsonInputBean.class);

        ArrayList<PatchInputBean> patchInputBeans = jsonInputBean.getPatchInputBean();
        CommandBean commandBean = new CommandBean();
        PatchInputBean patchInputBean = patchInputBeans.get(0);

//            MwHome mwHome = new MwHome();
        ArrayList<Component> components = patchInputBean.getMwHome().getComponents();
        Component component = components.get(0);
        commandBean.setComponentType(component.getComponentType());
        commandBean.setDomain_home(component.getDomain_home());
        commandBean.setOhs_instance_home_list(component.getOhs_instance_home_list());
        commandBean.setWlsPassword(component.getPassword());
        commandBean.setWlsUserName(component.getUsername());
        commandBean.setPatchList(component.getPatchList());
        commandBean.setPatchOperation(component.getPatchOperation());
        commandBean.setWg_oracle_home_list(component.getWg_oracle_home_list());

        commandBean.setHostName(patchInputBean.getHostName());
        commandBean.setSshUserName(patchInputBean.getGuid());
        commandBean.setSshPassword(patchInputBean.getPassword());

        commandBean.setWorkDir("/scratch/" + commandBean.getSshUserName());
        commandBean.setJavaHome("/usr/local/packages/jdk7");

        //Start Second json
        CommandBean commandBean1 = new CommandBean();
        PatchInputBean patchInputBean1 = patchInputBeans.get(1);

//            MwHome mwHome = new MwHome();
        ArrayList<Component> components1 = patchInputBean1.getMwHome().getComponents();
        Component component1 = components1.get(0);
        commandBean1.setComponentType(component1.getComponentType());
        commandBean1.setDomain_home(component1.getDomain_home());
        commandBean1.setOhs_instance_home_list(component1.getOhs_instance_home_list());
        commandBean1.setWlsPassword(component1.getPassword());
        commandBean1.setWlsUserName(component1.getUsername());
        commandBean1.setPatchList(component1.getPatchList());
        commandBean1.setPatchOperation(component1.getPatchOperation());
        commandBean1.setWg_oracle_home_list(component1.getWg_oracle_home_list());

        commandBean1.setHostName(patchInputBean1.getHostName());
        commandBean1.setSshUserName(patchInputBean1.getGuid());
        commandBean1.setSshPassword(patchInputBean1.getPassword());

        commandBean1.setWorkDir("/scratch/" + commandBean1.getSshUserName());
        commandBean1.setJavaHome("/usr/local/packages/jdk7");

        PatchOp patchOp = new PatchOp(commandBean);
        PatchOp patchOp1 = new PatchOp(commandBean1);
        Thread oneThread = new Thread(patchOp);
        Thread twoThread = new Thread(patchOp1);

/*        oneThread.start();
        twoThread.start();

        oneThread.join();
        twoThread.join();*/

//        patchOperationCommon.connection(commandBean);
        System.out.printf("OK HERE");
        Response.ResponseBuilder responseBuilder = Response.ok();
        responseBuilder.entity(new Gson().toJson(new String("And who are you, the proud lord said,\n")));
        return responseBuilder.build();

    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/envPatchStatus")
    public Response envPatchStatus(String envPatchStatus) throws IOException, JSONException, InterruptedException {
        Gson gson = new Gson();
        HashMap<String, String> reportContents = new HashMap<String, String>();

        // Convert JSON to Java Object
        JsonInputBean jsonInputBean = gson.fromJson(envPatchStatus, JsonInputBean.class);

        ArrayList<PatchInputBean> patchInputBeans = jsonInputBean.getPatchInputBean();
        CommandBean commandBean = new CommandBean();
        PatchInputBean patchInputBean = patchInputBeans.get(0);

//            MwHome mwHome = new MwHome();
        ArrayList<Component> components = patchInputBean.getMwHome().getComponents();
        Component component = components.get(0);
        commandBean.setComponentType(component.getComponentType());
        commandBean.setDomain_home(component.getDomain_home());
        commandBean.setOhs_instance_home_list(component.getOhs_instance_home_list());
        commandBean.setWlsPassword(component.getPassword());
        commandBean.setWlsUserName(component.getUsername());
        commandBean.setPatchList(component.getPatchList());
        commandBean.setPatchOperation(component.getPatchOperation());
        commandBean.setWg_oracle_home_list(component.getWg_oracle_home_list());

        commandBean.setHostName(patchInputBean.getHostName());
        commandBean.setSshUserName(patchInputBean.getGuid());
        commandBean.setSshPassword(patchInputBean.getPassword());

        commandBean.setWorkDir("/scratch/" + commandBean.getSshUserName());
        commandBean.setJavaHome("/usr/local/packages/jdk7");
        commandBean.setReports(reportContents);

        //Start Second json
        CommandBean commandBean1 = new CommandBean();
        PatchInputBean patchInputBean1 = patchInputBeans.get(1);

//            MwHome mwHome = new MwHome();
        ArrayList<Component> components1 = patchInputBean1.getMwHome().getComponents();
        Component component1 = components1.get(0);
        commandBean1.setComponentType(component1.getComponentType());
        commandBean1.setDomain_home(component1.getDomain_home());
        commandBean1.setOhs_instance_home_list(component1.getOhs_instance_home_list());
        commandBean1.setWlsPassword(component1.getPassword());
        commandBean1.setWlsUserName(component1.getUsername());
        commandBean1.setPatchList(component1.getPatchList());
        commandBean1.setPatchOperation(component1.getPatchOperation());
        commandBean1.setWg_oracle_home_list(component1.getWg_oracle_home_list());

        commandBean1.setHostName(patchInputBean1.getHostName());
        commandBean1.setSshUserName(patchInputBean1.getGuid());
        commandBean1.setSshPassword(patchInputBean1.getPassword());

        commandBean1.setWorkDir("/scratch/" + commandBean1.getSshUserName());
        commandBean1.setJavaHome("/usr/local/packages/jdk7");

        PatchStatus patchOp = new PatchStatus(commandBean);
        PatchStatus patchOp1 = new PatchStatus(commandBean1);
        Thread oneThread = new Thread(patchOp);
        Thread twoThread = new Thread(patchOp1);

        oneThread.start();
        twoThread.start();


        oneThread.join();
        twoThread.join();

//        patchOperationCommon.connection(commandBean);
        HashMap m = PatchStatus.getReportContents();

        System.out.printf("OK HERE" + m);
        Response.ResponseBuilder responseBuilder = Response.ok();

        responseBuilder.entity(new Gson().toJson(commandBean.getReports().toString()));
        return responseBuilder.build();

    }


    @GET
    @Consumes("*/*")
    @Produces("*/*")
    @Path("/hello")
    public Response getServiceInfo(){
        Response.ResponseBuilder responseBuilder = Response.ok();

        responseBuilder.entity(new Gson().toJson("HELLO WORLD"));
        return responseBuilder.build();

    }
   /* @POST
    @Consumes("application/json")
    @Path("/removePartnerForMultiDataCentre")
    public void removePartnerForMultiDataCentre(String partnerId) throws IOException, JSONException {
        String output1 = null;
        String output2 = null;
        String output3 = null;

        System.out.println("MDC DATA mdcData >>>>>>>>>>>>>>>>>>>>" + partnerId);

        MDCData mdcDataFromJson = parseJsonToObj(partnerId);

        System.out.println("MDC DATA " + mdcDataFromJson);

        String mdcClusterName = mdcDataFromJson.getMdcClusterName();
        String remoteDataCentreClusterId = mdcDataFromJson.getRemoteDataCentreClusterId();
        String adminServerHost = mdcDataFromJson.getAdminServerHost();
        String adminServerPort = mdcDataFromJson.getAdminServerPort();
        String oamServerMode = mdcDataFromJson.getOamServerMode();
        String oamPrimaryServerHost = mdcDataFromJson.getOamPrimaryServerHost();
        String oamPrimaryServerPort = mdcDataFromJson.getOamPrimaryServerPort();
        String accessClientPasswd = mdcDataFromJson.getAccessClientPasswd();
        String wlsUserName = mdcDataFromJson.getWlsUserName();
        String wlsUserPassword = mdcDataFromJson.getWlsUserPassword();
        String mdcCombination = mdcDataFromJson.getMdcCombination();

        System.out.println(">>>>> remoteDataCentreClusterId = " + remoteDataCentreClusterId);
        System.out.println(">>>>> mdcClusterName = " + mdcClusterName);
        System.out.println(">>>>> adminServerHost = " + adminServerHost);
        System.out.println(">>>>> adminServerPort = " + adminServerPort);
        System.out.println(">>>>> oamServerMode = " + oamServerMode);
        System.out.println(">>>>> oamPrimaryServerHost = " + oamPrimaryServerHost);
        System.out.println(">>>>> oamPrimaryServerPort = " + oamPrimaryServerPort);
        System.out.println(">>>>> accessClientPasswd = " + accessClientPasswd);
        System.out.println(">>>>> wlsUserName = " + wlsUserName);
        System.out.println(">>>>> wlsUserPassword = " + wlsUserPassword);

        MDCWLSTClient mdcWlstClient = new MDCWLSTClient(remoteDataCentreClusterId, mdcClusterName, adminServerHost, adminServerPort, oamServerMode,
                oamPrimaryServerHost, oamPrimaryServerPort, accessClientPasswd, wlsUserName, wlsUserPassword);

        try {
            output1 = MDCWLSTClient.removePartnerForMultiDataCentre(remoteDataCentreClusterId);
            System.out.println(">>>>>> setMDCClusterName out = " + output1);

        } catch (Exception e) {

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("remoteDataCentreClusterId", output1);
        String result = "Output: \n\npartnerId: \n\n" + jsonObject;
//        return Response.status(200).entity(result).build();
    }

    private MDCData parseJsonToObj(String jsonStr) {
        final ObjectMapper mapper = new ObjectMapper();
        // mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        final JsonFactory factory = mapper.getFactory();
        MDCData mdcData = new MDCData();

        try {
            System.out.println("BEFORE" + factory);
            System.out.println("STRING " + jsonStr);

            JsonParser jp = factory.createParser(jsonStr);

            String str = jp.readValueAsTree().toString();

            System.out.println("STRRRRRRR" + str);

            mdcData = mapper.readValue(str, MDCData.class);

        } catch (JsonParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return mdcData;
    }*/

    // public static void main(String[] args) {
    // String client =
    // "{\"adminServerHost\":\"slc01hto.us.oracle.com\",\"adminServerPort\":\"17346\",\"oamServerMode\":\"OPEN\",\"oamPrimaryServerHost\":\"slc01hto.us.oracle.com\",\"oamPrimaryServerPort\":\"6766\",\"accessClientPasswd\":\"Welcome1\",\"wlsUserName\":\"weblogic\",\"wlsUserPassword\":\"welcome1\",\"mdcCombination\":\"13\"}";
    //
    // MDCData mdcData = parseJsonToObj(client);
    // System.out.println(mdcData);
    //
    // System.out.println("ADMINSERVER HOST" + mdcData.getAdminServerHost());
    // }

}