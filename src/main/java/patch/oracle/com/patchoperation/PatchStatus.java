package patch.oracle.com.patchoperation;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.google.gson.Gson;
import patch.oracle.com.beans.CommandBean;
import patch.oracle.com.beans.Component;
import patch.oracle.com.beans.JsonInputBean;
import patch.oracle.com.beans.PatchInputBean;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;


/**
 * Created by kgurupra on 6/1/2016.
 */
public class PatchStatus implements Runnable {

    private CommandBean commandBean = null;
    private static HashMap<String, String> reportContents = new HashMap<String,String>();

    public PatchStatus(CommandBean commandBean) {
        this.commandBean = commandBean;
//        reportContents = commandBean.getReports();
    }

    @Override
    public void run() {
        envPatchStatus(commandBean);
    }


    public void envPatchStatus(CommandBean commandBean) {
        Session sess = null;
        Session sess1 = null;
        Connection conn = null;
//        HashMap<String, String> reports = new HashMap<String, String>();
        try {
            String[] commandTorun = new String[]{"lsinventory", "lspatches"};
            String oPatchCommand = commandBean.getWg_oracle_home_list()[0] + "/OPatch/opatch ";

            String lsinventory = oPatchCommand + commandTorun[0];
            String lspatches = oPatchCommand + commandTorun[1];

            System.out.println("Level.INFO" + "lsinventory" + lsinventory);
            System.out.println("Level.INFO" + "lspatches" + lspatches);

            conn = new Connection(commandBean.getHostName());
            conn.connect();

            boolean isAuthenticated = conn.authenticateWithPassword(commandBean.getSshUserName(),
                    commandBean.getSshPassword());

            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");

            sess = conn.openSession();
            sess.execCommand(lsinventory);
            sess.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 10000);
            System.out.println("Here is some information about the remote host:");
            InputStream stdout = new StreamGobbler(sess.getStdout());
            String remoteOutput = remoteOutput(stdout);
            System.out.printf("REMOTEOUTPUT lsinventory>>>>" + remoteOutput);

            sess1 = conn.openSession();
            sess1.execCommand(lsinventory);
            sess1.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 10000);
            System.out.println("Here is some information about the remote host:");
            InputStream stdout1 = new StreamGobbler(sess1.getStdout());
            String remoteOutput1 = remoteOutput(stdout1);
            System.out.printf("REMOTEOUTPUT lspatches>>>>" + remoteOutput1);

            reportContents.put("lsinventory", remoteOutput);
            reportContents.put("lspatches", remoteOutput1);

//            commandBean.setReports(reportContents);

/*
            System.out.println("ExitStatus: " + sess.getExitStatus());
            System.out.println("ExitSignal: " + sess.getExitSignal());
            System.out.println("State: " + sess.getState());
*/

        } catch (Exception e) {
            System.out.println(Level.SEVERE + "StopStart" + e.getMessage());
        } finally {
            sess.close();
            sess1.close();
            conn.close();

        }
    }


    public static HashMap<String, String> getReportContents() {
        return reportContents;
    }


    private String remoteOutput(InputStream stdout) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(stdout));
        String line = null;
        StringBuilder strBuilder = new StringBuilder();
        while ((line = br.readLine()) != null) {
//            System.out.println(line);
            strBuilder.append(line).append("\n");
        }
        return strBuilder.toString();
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        JsonInputBean jsonInputBean = null;
        try (Reader reader = new FileReader("D:\\jsonInputBean.json")) {

            // Convert JSON to Java Object
            jsonInputBean = gson.fromJson(reader, JsonInputBean.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
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

        PatchOp patchOp = new PatchOp(commandBean);
        patchOp.envPatchStatus(commandBean);

        System.out.println("FINAL" + gson.toJson(commandBean));
    }
}
