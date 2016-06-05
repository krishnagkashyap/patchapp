/*
package patch.oracle.com.patchoperation;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import patch.oracle.com.beans.CommandBean;


import java.io.*;
import java.util.logging.Level;

*/
/**
 * Created by kgurupra on 5/23/2016.
 *//*

public class PatchOperationCommon {


    private CommandBean commandBean = null;

    public void connection(String[] args) {
//        SSHClient sshClient = new SSHClient();
        PatchOperationCommon patchOperationCommon = new PatchOperationCommon();
        CommandBean commandBean = new CommandBean();
        String[] patchList = new String[]{"/scratch/kameneni/patch_apply/wg/p21118593_111230_Linux-x86-64.zip"};

//        commandBean.setHostName("slc08dzc.us.oracle.com");
        commandBean.setHostName(args[0]);
        commandBean.setHostPort("");
//        commandBean.setUserName("kgurupra");
        commandBean.setUserName(args[1]);
//        commandBean.setSysPassword("myOra123");
        commandBean.setSysPassword(args[2]);
//        commandBean.setOracleHome("/scratch/kgurupra/A1/view/kgurupra_dte7464/oracle/work/FMW_WEBT_CFG1/instances/webtier_inst3030");
        commandBean.setOracleHome(args[3]);
//        commandBean.setCommandToRun("start");
        commandBean.setCommandToRun(args[4]);
        commandBean.setCommandToRun("apply");
        commandBean.setOracleHome("/scratch/kameneni/AUTO_WORK/mw8968/wg4237");
        commandBean.setWorkDir("/scratch/" + commandBean.getUserName());

//        patchOperationCommon.ohsOperation(commandBean);
//        return sshClient;
        commandBean.setPatchList(patchList);
        patchOperationCommon.patchOperation(commandBean);


    }
*/
/*    public static void main(String[] args) {
        SSHClient sshClient = new SSHClient();
        CommandBean commandBean = new CommandBean();
        commandBean.setHostName("slc08dzc.us.oracle.com");
        commandBean.setHostPort("");
        commandBean.setUserName("kgurupra");
        commandBean.setSysPassword("myOra123");
        commandBean.setOracleHome("/scratch/kgurupra/A1/view/kgurupra_dte7464/oracle/work/FMW_WEBT_CFG1/instances/webtier_inst3030");
        commandBean.setCommandToRun("start");
        sshClient.ohsOperation(commandBean);
    }*//*


    public void patchOperation(CommandBean commandBean) {
        try {
            String[] patchList = commandBean.getPatchList();
            String patchId = null;

            Connection conn = new Connection(commandBean.getHostName());
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(commandBean.getUserName(),
                    commandBean.getSysPassword());
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

                String oPatchSH = commandBean.getOracleHome() + "/OPatch";
                commandBean.setJavaHome("/usr/local/packages/jdk7");
                String patchUnzipLoc = commandBean.getWorkDir() + "/" + patchId;
                String invLoc = commandBean.getOracleHome() + "/oraInst.loc";
                String logLoc = commandBean.getWorkDir() + "/" + patchId + ".log";


//          System.out
//          .println("Here is some information about the remote host:");
//  InputStream stdout = new StreamGobbler(sess.getStdout());
//
//  BufferedReader br = new BufferedReader(
//          new InputStreamReader(stdout));
//
//  while (true) {
//      String line = br.readLine();
//      if (line.equals("") || line.equals(null))
//          break;
//      System.out.println(line);
//  }
                if (commandBean.getCommandToRun().toLowerCase().equals("apply")) {
                    String unzipPatch = "unzip " + patchLoc + " -d " + commandBean.getWorkDir() + " >> " + logLoc + "&&";
//                    sess.execCommand(unzipPatch);
//                    sess.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 10000);
                    String runPatch = unzipPatch + oPatchSH + "/opatch " + commandBean.getCommandToRun() +
                            " -verbose -silent -oh " + commandBean.getOracleHome() + " -jdk " + commandBean.getJavaHome() + " -invPtrLoc " + invLoc +
                            " " + patchUnzipLoc + " >> " + logLoc;

                    System.out.println("Patch apply command ========= " + runPatch);
                    sess.execCommand(runPatch);
                    sess.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 10000);

                    System.out.println("Final log file at ========= " + logLoc);
                } else {
                    String runPatch = oPatchSH + "/opatch " + commandBean.getCommandToRun() +
                            " -verbose -silent -oh " + commandBean.getOracleHome() + " -jdk " + commandBean.getJavaHome() + " -invPtrLoc " + invLoc +
                            " -id " + patchId + " >> " + logLoc;

                    System.out.println("Patch rollback command ========= " + runPatch);
                    sess.execCommand(runPatch);

                    System.out.println("Final log file at ========= " + logLoc);

                }


//
//          System.out
//                  .println("Here is some information about the remote host:");
//          InputStream stdout = new StreamGobbler(sess.getStdout());
//
//          BufferedReader br = new BufferedReader(
//                  new InputStreamReader(stdout));
//
//          while (true) {
//              String line = br.readLine();
//              if (line.equals("") || line.equals(null))
//                  break;
//              System.out.println(line);
//          }
            }

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
            String ohsInstance = commandBean.getOracleHome();
            String operation = commandBean.getCommandToRun();
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
            boolean isAuthenticated = conn.authenticateWithPassword(commandBean.getUserName(),
                    commandBean.getSysPassword());
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
}*/
