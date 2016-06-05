package patch.oracle.com.beans;

/**
 * Created by kgurupra on 5/23/2016.
 */
public class Component {
    private String componentType = null;
    private String username = null;
    private String password = null;
    private String domain_home = null;
    private String[] wg_oracle_home_list = null;
    private String[] ohs_instance_home_list = null;
    private String[] patchList = null;
    private String patchOperation = null;

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String[] getPatchList() {
        return patchList;
    }

    public void setPatchList(String[] patchList) {
        this.patchList = patchList;
    }

    public String getPatchOperation() {
        return patchOperation;
    }

    public void setPatchOperation(String patchOperaton) {
        this.patchOperation = patchOperation;
    }
}
