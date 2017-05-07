/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion_server;

import static conexion_server.Login.miSS;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
/**
 *
 * @author Petite
 */
public class Mensajes implements Runnable {
    
    public static ServerSocket miSS;
    
    public void run() {

        do {
            Socket conec = new Socket();

            try {

                conec = miSS.accept();
                
                InputStream inn = conec.getInputStream();
                ObjectInputStream in = new ObjectInputStream(inn);

                String json;

                try {
                    json = (String) in.readObject();
                    JSONObject jobj = new JSONObject(json);

                    String destinatario = jobj.getString("destinatario");
                    
                    String horaF;
                    int hora, minutos, segundos;
                    Calendar calendario = Calendar.getInstance();
                    
                    hora = calendario.get(Calendar.HOUR_OF_DAY);
                    minutos = calendario.get(Calendar.MINUTE);
                    segundos = calendario.get(Calendar.SECOND);
                    
                    horaF = hora + ":" + minutos + ":" + segundos;
                    
                    jobj.put("status", "Problema del servidor... Intentalo más tarde");
                    
                    //-------------------------------------------------------------------
                    
                    jobj.put("hora", horaF);
                    
                    ObjectOutputStream out = new ObjectOutputStream(conec.getOutputStream());
                    out.writeObject(jobj.toString());
                    
                    conec.close();
                    
                } catch (Exception ex) {
                    Server.request.setText(Server.request.getText() + "Error: " + ex + "\n");
                    System.out.println(ex);
                }
                
                
            } catch (Exception ex) {
                Server.request.setText(Server.request.getText() + "Error: " + ex + "\n");
                System.out.println(ex+"1");
            }
            

        } while (true);
    }
}
