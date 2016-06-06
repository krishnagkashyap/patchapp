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
 * Created by kgurupra on 5/24/2016.
 */
public class PatchOp implements Runnable {

    private CommandBean commandBean = null;
    private HashMap<String, String> reportContents = new HashMap<String, String>();

    public PatchOp(CommandBean commandBean) {
        this.commandBean = commandBean;
    }

    @Override
    public void run() {
        connection(commandBean);
    }

    public void connection(CommandBean commandBean) {
        commandBean.setCommadToExecute("stop");
        ohsOperation(commandBean);

//        envPatchStatus(commandBean);
        patchOperation(commandBean);

        commandBean.setCommadToExecute("start");
        ohsOperation(commandBean);
    }

    public void patchOperation(CommandBean commandBean) {
        try {
            String logLoc = null;
            String[] patchList = commandBean.getPatchList();
            String patchId = null;

            Connection conn = new Connection(commandBean.getHostName());
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(commandBean.getSshUserName(),
                    commandBean.getSshPassword());
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            Session sess = conn.openSession();

            for (String patchLoc : patchList) {
                String[] patchSplit = patchLoc.split("/");
                //read only the patch name say p21516999_111230_Generic.zip
                String patchName = patchSplit[(patchSplit.length) - 1];
                System.out.println("pathchName ======== " + patchName);

                String[] patchIdStr = patchName.split("_");
                //read only the patch Id say 21516999
                patchId = patchIdStr[0].substring(1);
                commandBean.setPatchId(patchId);
                System.out.println("Patch ID ========= " + patchId);

                String oPatchSH = commandBean.getWg_oracle_home_list()[0] + "/OPatch";
                commandBean.setJavaHome("/usr/local/packages/jdk7");
                String patchUnzipLoc = commandBean.getWorkDir() + "/" + patchId;
                String invLoc = commandBean.getWg_oracle_home_list()[0] + "/oraInst.loc";
                logLoc = commandBean.getWorkDir() + "/" + patchId + ".log";

                if (commandBean.getPatchOperation().equalsIgnoreCase("apply")) {
                    String unzipPatch = "unzip " + patchLoc + " -d " + commandBean.getWorkDir() + " >> " + logLoc + "&&";
//                    sess.execCommand(unzipPatch);
//                    sess.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 10000);
                    String runPatch = unzipPatch + oPatchSH + "/opatch " + commandBean.getPatchOperation() +
                            " -verbose -silent -oh " + commandBean.getWg_oracle_home_list()[0] + " -jdk " + commandBean.getJavaHome() + " -invPtrLoc " + invLoc +
                            " " + patchUnzipLoc + " >> " + logLoc;

                    System.out.println("Patch apply command ========= " + runPatch);
                    sess.execCommand(runPatch);
                    sess.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 10000);

                    System.out.println("Final log file at ========= " + logLoc);
                } else {
                    String runPatch = oPatchSH + "/opatch " + commandBean.getPatchOperation() +
                            " -verbose -silent -oh " + commandBean.getWg_oracle_home_list()[0] + " -jdk " + commandBean.getJavaHome() + " -invPtrLoc " + invLoc +
                            " -id " + patchId + " >> " + logLoc;

                    System.out.println("Patch rollback command ========= " + runPatch);
                    sess.execCommand(runPatch);

                    System.out.println("Final log file at ========= " + logLoc);

                }
            }
            reportContents.put("patch_status", "true");
            reportContents.put("oracle_home", commandBean.getWg_oracle_home_list().toString());
            reportContents.put("patch_list", commandBean.getPatchList().toString());
            reportContents.put("log_location", logLoc);
            reportContents.put("patch_operation","apply");
            commandBean.setReports(reportContents);
            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close();
            conn.close();
        } catch (Exception e) {

            System.out.println(Level.SEVERE + "StopStart" + e.getMessage());
        }

    }

    public void ohsOperation(CommandBean commandBean) {
        try {
            String commandTorun = "";
            String ohsInstance = commandBean.getOhs_instance_home_list()[0];
            String operation = commandBean.getCommadToExecute();
            if (operation.equals("start")) {
                commandTorun = ohsInstance + "/" + "bin" + "/" + "opmnctl"
                        + " startall";

            } else if (operation.equals("stop")) {
                commandTorun = ohsInstance + "/" + "bin" + "/" + "opmnctl"
                        + " stopall";

            }
            System.out.println("Level.INFO" + "StopStart" + commandTorun);
            Connection conn = new Connection(commandBean.getHostName());
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(commandBean.getSshUserName(),
                    commandBean.getSshPassword());
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            Session sess = conn.openSession();
            sess.execCommand(commandTorun);
            System.out
                    .println("Here is some information about the remote host:");
            InputStream stdout = new StreamGobbler(sess.getStdout());

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(stdout));

            while (true) {
                String line = br.readLine();
                if (line.equals("") || line.equals(null))
                    break;
                System.out.println(line);
            }
            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close();
            conn.close();

        } catch (Exception e) {

            System.out.println(Level.SEVERE + "StopStart" + e.getMessage());
        }
    }


    public void envPatchStatus(CommandBean commandBean) {
        Session sess = null;
        Session sess1 = null;
        Connection conn = null;
        HashMap<String, String> reports = new HashMap<String, String>();
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
            reports.put("lsinventory", remoteOutput);
            reports.put("lspatches", remoteOutput1);

            commandBean.setReports(reports);

            System.out.println("ExitStatus: " + sess.getExitStatus());
            System.out.println("ExitSignal: " + sess.getExitSignal());
            System.out.println("State: " + sess.getState());

        } catch (Exception e) {
            System.out.println(Level.SEVERE + "StopStart" + e.getMessage());
        } finally {
            sess.close();
            sess1.close();
            conn.close();

        }
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
