package patch.oracle.com.patchoperation;

/**
 * Created by kgurupra on 5/18/2016.
 */
public class OHSOperation implements Runnable {
    private String[] host = null;
//    private String[] host2 = null;

    public OHSOperation(String[] host) {
        this.host = host;
    }

    @Override
    public void run() {
//        SSHClient sshClient = new SSHClient();
//        PatchOperationCommon patchOperationCommon = new PatchOperationCommon();
//        patchOperationCommon.connection(host);
//        sshClient.connection(host);
//        sshClient.connection(host2);
    }

    public static void main(String[] args) {

/*
        "HOST": "slc01.us.oracle.com",
                "guid": "kgurupra",
                "password": "kjkejh",
                */
//String hostName ="";
//String hostGuid ="";
//String hostPassword="";

/*
                "MW_HOME":
        {
            "COMPONENT":
            {
                "TYPE":"OAM",
                    "ORACLE_HOME":"/oracle/work/home",
                    "DOMAIN_HOME":"/oracle/work/home",
                    "username":"weblogic",
                    "password":"weblogic1",
                    "PATCHLIST":"1,2,3,4",
                    "PATCHOPEARTION":"apply"
*/


        String[] patchList = new String[]{"/scratch/kameneni/patch_apply/wg/p21118593_111230_Linux-x86-64.zip"};

        String[] host1 = new String[]{"slc08dzc.us.oracle.com", "kgurupra", "myOra123", "/scratch/kgurupra/A1/view/kgurupra_dte7464/oracle/work/FMW_WEBT_CFG1/instances/webtier_inst3030", "stop"};
        String[] host2 = new String[]{"slc03qoh.us.oracle.com", "kameneni", "123Pandu", "/scratch/kameneni/AUTO_WORK/view/kameneni_dte4106/oracle/work/FMW_WEBT_CFG1/instances/webtier_inst3861", "stop"};


        Thread oneThread = new Thread(new OHSOperation(host1));
        Thread twoThread = new Thread(new OHSOperation(host2));
//        oneThread.start();
        twoThread.start();
    }
}
