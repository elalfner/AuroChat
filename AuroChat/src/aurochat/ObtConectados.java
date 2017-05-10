/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aurochat;

import static aurochat.Login.ip;
import static aurochat.ObtenerContacto.listconect;
import java.io.InputStream;
import java.io.ObjectInputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author elalf
 */
public class ObtConectados implements Runnable{
    public void run(){
        do {
            try {

                Socket conec = new Socket(ip, 2226);
                InputStream inn = conec.getInputStream();
                ObjectInputStream in = new ObjectInputStream(inn);

                String json;
                json = (String) in.readObject();
                JSONObject jobj = new JSONObject(json);
                
                listconect=jobj;
                
                JSONArray conectados=jobj.getJSONArray("conect");
                
                
                //System.out.println(conectados.get(0));
                
                DefaultListModel modelo = new DefaultListModel();
                for(int i=0; i<conectados.length();i++){
                modelo.addElement(conectados.get(i));
                }
                Home.list.setModel(modelo);
                
                sleep(1000);

            } catch (Exception ex) {
                Logger.getLogger(ObtenerContacto.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } while (true);
    }
}
