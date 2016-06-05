package patch.oracle.com.beans;

import java.util.ArrayList;

/**
 * Created by kgurupra on 5/23/2016.
 */
public class MwHome {
    ArrayList<Component> oracleComponents = new ArrayList<Component>();

    public ArrayList<Component> getComponents() {
        return oracleComponents;
    }

    public void setComponents(Component component) {
        oracleComponents.add(component);
    }
}
