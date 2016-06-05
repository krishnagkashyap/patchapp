package patch.oracle.com.beans;

import java.util.ArrayList;

/**
 * Created by kgurupra on 5/23/2016.
 */
public class JsonInputBean {

    private ArrayList<PatchInputBean> patchData = new ArrayList<PatchInputBean>();

    public ArrayList<PatchInputBean> getPatchInputBean() {
        return patchData;
    }

    public void setPatchInputBean(PatchInputBean patchInputBean) {
        patchData.add(patchInputBean);
    }
}
