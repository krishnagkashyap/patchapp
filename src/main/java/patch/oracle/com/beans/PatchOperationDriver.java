package patch.oracle.com.beans;

import com.google.gson.Gson;
import patch.oracle.com.patchoperation.PatchOp;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by kgurupra on 5/24/2016.
 */
public class PatchOperationDriver {
    public static void main(String[] args) {
        Gson gson = new Gson();

        try (Reader reader = new FileReader("D:\\jsonInputBean.json")) {

            // Convert JSON to Java Object
            JsonInputBean jsonInputBean = gson.fromJson(reader, JsonInputBean.class);
            ArrayList<PatchInputBean> patchInputBeans = jsonInputBean.getPatchInputBean();

/*

            ArrayList<CommandBean> alist = new ArrayList<CommandBean>();

            for (PatchInputBean patchIpBean : patchInputBeans
                    ) {
                CommandBean commandBean = new CommandBean();
                ArrayList<Component> components = patchIpBean.getMwHome().getComponents();
                for (Component component :
                        components
                        ) {
                    commandBean.setComponentType(component.getComponentType());
                    commandBean.setDomain_home(component.getDomain_home());
                    commandBean.setOhs_instance_home_list(component.getOhs_instance_home_list());
                    commandBean.setWlsPassword(component.getPassword());
                    commandBean.setWlsUserName(component.getUsername());
                    commandBean.setPatchList(component.getPatchList());
                    commandBean.setPatchOperation(component.getPatchOperation());
                    commandBean.setWg_oracle_home_list(component.getWg_oracle_home_list());
                }
                commandBean.setHostName(patchIpBean.getHostName());
                commandBean.setSshUserName(patchIpBean.getGuid());
                commandBean.setSshPassword(patchIpBean.getPassword());
                commandBean.setWorkDir("/scratch/" + commandBean.getSshUserName());
                commandBean.setJavaHome("/usr/local/packages/jdk7");
                alist.add(commandBean);
            }
*/


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

            PatchOp patchOperationCommon = new PatchOp(commandBean);
//            patchOperationCommon.connection(commandBean);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    private void getCommandBeans(JsonInputBean jsonInputBean) {
        CommandBean
        ArrayList<PatchInputBean> patchInputBean = jsonInputBean.getPatchInputBean();
        for (PatchInputBean patchipBean: patchInputBean){

        }
    }*/
}
