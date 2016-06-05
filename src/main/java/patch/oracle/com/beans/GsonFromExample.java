package patch.oracle.com.beans;

/**
 * Created by kgurupra on 5/24/2016.
 */
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class GsonFromExample {

    public static void main(String[] args) {

        Gson gson = new Gson();

        try (Reader reader = new FileReader("D:\\jsonInputBean.json")) {

            // Convert JSON to Java Object
            JsonInputBean jsonInputBean = gson.fromJson(reader, JsonInputBean.class);
            System.out.println(jsonInputBean.getPatchInputBean().get(1).getHostName());

            // Convert JSON to JsonElement, and later to String
            /*JsonElement json = gson.fromJson(reader, JsonElement.class);
            String jsonInString = gson.toJson(json);
            System.out.println(jsonInString);*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}