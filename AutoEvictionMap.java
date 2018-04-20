/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoevictionmap;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author nishanth
 */
public class AutoEvictionMap {

    public static void main(String[] args) {
         AutoEvictionMapUtils<String,String> newMap = new AutoEvictionMapUtils<String,String>(5000,30, TimeUnit.SECONDS,false,"");

        newMap.put("nirmal","1");
        newMap.put("Bala","2");
        try
        {
            System.out.println("Sleeping 1");
            Thread.sleep(20000);
        }
        catch(Exception e)
        {
        }

        System.out.println("MapKeys = "+newMap.keySet());
        newMap.put("nirmal","3");
        newMap.put("sebaty","4");

        for (Map.Entry<String,String> entry : newMap.entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
        try
        {
            System.out.println("Sleeping 2");
            Thread.sleep(20000);
        }
        catch(Exception e)
        {
        }
        System.out.println("MapKeys = "+newMap.keySet());
        System.out.println("Value for a key = "+newMap.get("nirmal"));

        try
        {
            System.out.println("Sleeping 3");
            Thread.sleep(10000);
        }
        catch(Exception e)
        {
        }
        System.out.println("MapKeys = "+newMap.keySet());
    }
    
}
