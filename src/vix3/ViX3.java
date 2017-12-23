/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vix3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Orom
 */
public class ViX3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        try {
//            URL paginaVitrasa=new URL("http://infobus.vitrasa.es:8002/Default.aspx?parada=1850");
//            
//            //De aqui al sigueinte comentario lo uso para ir viendo por consola la web y saber que tengo que sacar de la misma
//            URLConnection con= paginaVitrasa.openConnection();
//            
//            InputStream is=con.getInputStream();
//            
//            BufferedReader br=new BufferedReader(new InputStreamReader(is));
//            
//            String line = null;
//            
//            while ((line = br.readLine()) != null) {
//                if(line.contains("id=lblParada")){
//                    System.out.println("Parada Nº ");
//                }
//                System.out.println(line);
//            }
//            
//            //Cargamos el documento html con Jsoup (URL,timeout en milisegundos)
//            Document doc=Jsoup.parse(paginaVitrasa, 100000);           
//            
//            //Obtenemos el codigo de la parada             
//            String paradaCod=doc.getElementById("lblParada").text();
//            System.out.println(paradaCod);
//            
//            //La hora a la que se recoge la info de la web
//            String hora=doc.getElementById("lblHora").text();
//            System.out.println(hora);
//            
//            //Direccion de la parada
//            String direccion=doc.getElementById("lblNombre").text();
//            System.out.println(direccion);
//            
//            //Lista de buses?
//            Element tablaBuses=doc.getElementById("GridView1");
//            Elements tbody=tablaBuses.children();
//            for(Element e:tbody){
//                    for(Element f:e.children()){
//                        //imprime el nombre de la etiqueta (ahora mismo estoy en los tr de la tabla
//                        System.out.println(f.nodeName());
//                        //imprime el texto de la etiqueta y todos sus hijos
//                        System.out.println(f.children().text());
//                    }
//                
//            }
//            
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
//        }

        /*
            SEGUNDA SERIE DE PRUEBAS
         */
        for (int i = 0; i < 20000; i+=10) {
            infoParada(i);
        }
    }

    public static void infoParada(int paradaCod) {
        try {
            URL paginaVitrasa = new URL("http://infobus.vitrasa.es:8002/Default.aspx?parada=" + paradaCod);
            //Cargamos el documento html con Jsoup (URL,timeout en milisegundos)
            Document doc = Jsoup.parse(paginaVitrasa, 100000);

            //Obtenemos el codigo de la parada             
            String paradaCodigo = doc.getElementById("lblParada").text();

            //Direccion de la parada
            String direccion = doc.getElementById("lblNombre").text();

            if (!direccion.contains("Parada Inexistente")) {
                System.out.println("Parada Nº " + paradaCodigo + "  " + direccion);
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
