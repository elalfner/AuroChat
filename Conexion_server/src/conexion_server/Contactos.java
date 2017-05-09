/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion_server;

import static conexion_server.Login.miSS;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author elalf
 */
public class Contactos implements Runnable {

    public static ServerSocket miSS;

    public void run() {
        do {
            try {
                Socket conec = new Socket();
                conec = miSS.accept();

                Iterator<Usuario> itee = Login.usuarios.iterator();
                
                JSONObject conect = new JSONObject();
                JSONArray nomb = new JSONArray();

                while (itee.hasNext()) {
                    Usuario us = itee.next();

                    nomb.put(us.nombre);
                }
                
                conect.put("conect",nomb);
                
                ObjectOutputStream out = new ObjectOutputStream(conec.getOutputStream());
                out.writeObject(conect.toString());
                //Server.request.setText(Server.request.getText() + "Conectados: " + conect +"\n");
            } catch (Exception ex) {
                Server.request.setText(Server.request.getText() + "Error: " + ex + "\n");
            }
        } while (true);
    }
}
