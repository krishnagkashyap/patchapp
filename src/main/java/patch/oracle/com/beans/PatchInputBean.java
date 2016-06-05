package patch.oracle.com.beans;

/**
 * Created by kgurupra on 5/24/2016.
 */
public class PatchInputBean {

    private String hostName = null;
    private String guid = null;
    private String password = null;
    private MwHome mwHome = null;


    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MwHome getMwHome() {
        return mwHome;
    }

    public void setMwHome(MwHome mwHome) {
        this.mwHome = mwHome;
    }

}
