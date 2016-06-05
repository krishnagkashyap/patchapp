/*
package patch.oracle.com.patchoperation;

import java.util.HashMap;
import java.util.concurrent.*;

*/
/**
 * Created by kgurupra on 6/1/2016.
 *//*

public class Main {

    public static void main(final String[] argv) {
        final ExecutorService service;
        final Future<HashMap> task;
        final Future<HashMap> task1;

        service = Executors.newFixedThreadPool(10);
        task = service.submit(new Foo());
        task1 = service.submit(new Foo());


        try {
            final String str;

            // waits the 10 seconds for the Callable.call to finish.
            HashMap map = task.get(); // this raises ExecutionException if thread dies
            HashMap map1 = task1.get(); // this raises ExecutionException if thread dies
            System.out.println(map);
            System.out.println(map1);
        } catch (final InterruptedException ex) {
            ex.printStackTrace();
        } catch (final ExecutionException ex) {
            ex.printStackTrace();
        }

        service.shutdownNow();
    }
}

class Foo implements Callable<HashMap> {
    public HashMap call() {
        HashMap map = new HashMap();
        try {
            // sleep for 10 seconds
            Thread.sleep(10 * 1000);
            map.put("1","ONE");
            map.put("2","TWO");
            map.put("3","THREE");
            map.put("4","FOUR");

        } catch (final InterruptedException ex) {
            ex.printStackTrace();
        }

        return map;
    }
}*/
