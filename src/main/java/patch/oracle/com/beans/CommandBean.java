package patch.oracle.com.beans;

import java.util.HashMap;

/**
 * Created by kgurupra on 5/17/2016.
 */
public class CommandBean {
    private String hostName = null;
    private String hostPort = null;
    private String sshUserName = null;
    private String sshPassword = null;
    private String componentType = null;
    private String wlsUserName = null;
    private String wlsPassword = null;
    private String domain_home = null;
    private String[] wg_oracle_home_list = null;
    private String[] ohs_instance_home_list = null;
    private String patchOperation = null;
    private String[] patchList = null;
    private String commadToExecute = null;

    //The following are not got from JSON
    private String mwHome = null;
    private String patchId = null;
    private String workDir = null;
    private String javaHome = null;
    private HashMap<String, String> reports = null;

    public HashMap<String, String> getReports() {
        return reports;
    }

    public void setReports(HashMap<String, String> reports) {
        this.reports = reports;
    }

    public String getCommadToExecute() {
        return commadToExecute;
    }

    public void setCommadToExecute(String commadToExecute) {
        this.commadToExecute = commadToExecute;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public String getSshUserName() {
        return sshUserName;
    }

    public void setSshUserName(String sshUserName) {
        this.sshUserName = sshUserName;
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getWlsUserName() {
        return wlsUserName;
    }

    public void setWlsUserName(String wlsUserName) {
        this.wlsUserName = wlsUserName;
    }

    public String getWlsPassword() {
        return wlsPassword;
    }

    public void setWlsPassword(String wlsPassword) {
        this.wlsPassword = wlsPassword;
    }

    public String getDomain_home() {
        return domain_home;
    }

    public void setDomain_home(String domain_home) {
        this.domain_home = domain_home;
    }

    public String[] getWg_oracle_home_list() {
        return wg_oracle_home_list;
    }

    public void setWg_oracle_home_list(String[] wg_oracle_home_list) {
        this.wg_oracle_home_list = wg_oracle_home_list;
    }

    public String[] getOhs_instance_home_list() {
        return ohs_instance_home_list;
    }

    public void setOhs_instance_home_list(String[] ohs_instance_home_list) {
        this.ohs_instance_home_list = ohs_instance_home_list;
    }

    public String getPatchOperation() {
        return patchOperation;
    }

    public void setPatchOperation(String patchOperation) {
        this.patchOperation = patchOperation;
    }

    public String[] getPatchList() {
        return patchList;
    }

    public void setPatchList(String[] patchList) {
        this.patchList = patchList;
    }

    public String getMwHome() {
        return mwHome;
    }

    public void setMwHome(String mwHome) {
        this.mwHome = mwHome;
    }

    public String getPatchId() {
        return patchId;
    }

    public void setPatchId(String patchId) {
        this.patchId = patchId;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public String getJavaHome() {
        return javaHome;
    }

    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }
}
