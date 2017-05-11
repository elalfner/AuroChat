/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion_server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import org.json.JSONObject;

/**
 *
 * @author elalf
 */
public class CerrarSesion implements Runnable {

    public static ServerSocket miSS;

    public void run() {
        do {
            try {
                Socket conec = new Socket();
                conec = miSS.accept();

                InputStream inn = conec.getInputStream();
                ObjectInputStream in = new ObjectInputStream(inn);

                String json;
                json = (String) in.readObject();
                JSONObject jobj = new JSONObject(json);
                
                String username=jobj.getString("username");
                
                Iterator<Usuario> itee = Login.usuarios.iterator();
                while (itee.hasNext()) {
                    Usuario us = itee.next();
                    if (us.nombre.equals(username)) {
                        Login.usuarios.remove(us);
                        break;
                    }
                }
                
                jobj.put("status","OK");
                
                ObjectOutputStream out = new ObjectOutputStream(conec.getOutputStream());
                out.writeObject(jobj.toString());
                
            } catch (Exception e) {
                Server.request.append("Error: " + e + "\n");
            }
        } while (true);
    }
}
