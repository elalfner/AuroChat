/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aurochat;

import static aurochat.Login.ip;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Susan Ramos
 */
public class ObtenerContacto implements Runnable {
    
    public static JSONObject listconect = new JSONObject();
    
    public void run() {
        do {
            try {

                Socket conec = new Socket(ip, 2226);
                InputStream inn = conec.getInputStream();
                ObjectInputStream in = new ObjectInputStream(inn);

                String json;
                json = (String) in.readObject();
                JSONObject jobj = new JSONObject(json);
                
                listconect=jobj;
                //System.out.println(jobj);
                
                sleep(1000);

            } catch (Exception ex) {
                Logger.getLogger(ObtenerContacto.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } while (true);
    }
}