package patch.oracle.com.beans;

/**
 * Created by kgurupra on 5/24/2016.
 */

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GsonExample {

    public static void main(String[] args) {

        JsonInputBean staff = createDummyObject();

        //1. Convert object to JSON string
        Gson gson = new Gson();
        String json = gson.toJson(staff);
        System.out.println(json);

        //2. Convert object to JSON string and save into a file directly
        try (FileWriter writer = new FileWriter("D:\\jsonInputBean.json")) {

            gson.toJson(staff, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static JsonInputBean createDummyObject() {
        String[] host1 = new String[]{"slc08dzc.us.oracle.com", "kgurupra", "myOra123", "/scratch/kgurupra/A1/view/kgurupra_dte7464/oracle/work/FMW_WEBT_CFG1/instances/webtier_inst3030", "stop"};
        String[] host2 = new String[]{"slc03qoh.us.oracle.com", "kameneni", "123Pandu", "/scratch/kameneni/AUTO_WORK/view/kameneni_dte4106/oracle/work/FMW_WEBT_CFG1/instances/webtier_inst3861", "stop"};
        String[] wg_oracle_home_list = new String[]{"/scratch/kgurupra/A1/view/kgurupra_dte7464/oracle/work/FMW_WEBT_CFG1", "/scratch/kgurupra/A1/view/kgurupra_dte7464/oracle/work/FMW_WEBT_CFG2"};
        String[] ohs_instance_home_list = new String[]{"/scratch/kgurupra/A1/view/kgurupra_dte7464/oracle/work/FMW_WEBT_CFG1/instances/webtier_inst3030", "/scratch/kgurupra/A1/view/kgurupra_dte7464/oracle/work/FMW_WEBT_CFG1/instances/webtier_inst3031"};
        String[] patchList = new String[]{"/scratch/kameneni/patch_apply/wg/p21118593_111230_Linux-x86-64.zip", "/scratch/kameneni/patch_apply/wg/p21118593_111230_Linux-x86-64.zip"};

        PatchInputBean patchInputBean1 = new PatchInputBean();

        patchInputBean1.setHostName("slc08dzc.us.oracle.com");


        patchInputBean1.setGuid("kgurupra");
        patchInputBean1.setPassword("myOra123");

        MwHome mwHome = new MwHome();


        PatchInputBean patchInputBean2 = new PatchInputBean();

        patchInputBean2.setHostName("slc03qoh.us.oracle.com");
        patchInputBean2.setGuid("kameneni");
        patchInputBean2.setPassword("123Pandu");

        MwHome mwHome1 = new MwHome();
        Component component = new Component();

        component.setComponentType("webgate");
        component.setDomain_home("DOMAIN_HOME");
        component.setWg_oracle_home_list(wg_oracle_home_list);
        component.setOhs_instance_home_list(ohs_instance_home_list);
        component.setUsername("weblogic");
        component.setPassword("welcome1");
        component.setPatchList(patchList);
        component.setPatchOperation("apply");

        Component component1 = new Component();

        component1.setComponentType("webgate");
        component1.setDomain_home("DOMAIN_HOME");
        component1.setOhs_instance_home_list(ohs_instance_home_list);
        component1.setWg_oracle_home_list(wg_oracle_home_list);
        component1.setUsername("weblogic");
        component1.setPassword("welcome1");
        component1.setPatchList(patchList);
        component1.setPatchOperation("apply");


        mwHome.setComponents(component);
        mwHome.setComponents(component1);


        mwHome1.setComponents(component);
        mwHome1.setComponents(component1);


        patchInputBean1.setMwHome(mwHome);
        patchInputBean2.setMwHome(mwHome1);
/*

        List<String> skills = new ArrayList<>();
        skills.add("java");
        skills.add("python");
        skills.add("shell");

        jsonInputBean.setSkills(skills);
*/
        JsonInputBean jsonInputBean = new JsonInputBean();
        jsonInputBean.setPatchInputBean(patchInputBean1);
        jsonInputBean.setPatchInputBean(patchInputBean2);
        return jsonInputBean;

    }

}