/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vix3;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
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
        try {
            URL paginaVitrasa=new URL("http://infobus.vitrasa.es:8002/Default.aspx?parada=1850");
            
            //De aqui al sigueinte comentario lo uso para ir viendo por consola la web y saber que tengo que sacar de la misma
            URLConnection con= paginaVitrasa.openConnection();
            
            InputStream is=con.getInputStream();
            
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            
            String line = null;
            
            while ((line = br.readLine()) != null) {
                if(line.contains("id=lblParada")){
                    System.out.println("Parada Nº ");
                }
                System.out.println(line);
            }
            
            //Cargamos el documento html con Jsoup (URL,timeout en milisegundos)
            Document doc=Jsoup.parse(paginaVitrasa, 100000);           
            
            //Obtenemos el codigo de la parada             
            String paradaCod=doc.getElementById("lblParada").text();
            System.out.println(paradaCod);
            
            //La hora a la que se recoge la info de la web
            String hora=doc.getElementById("lblHora").text();
            System.out.println(hora);
            
            //Direccion de la parada
            String direccion=doc.getElementById("lblNombre").text();
            System.out.println(direccion);
            
            //Lista de buses?
            Element tablaBuses=doc.getElementById("GridView1");
            Elements tbody=tablaBuses.children();
            for(Element e:tbody){
                    for(Element f:e.children()){
                        //imprime el nombre de la etiqueta (ahora mismo estoy en los tr de la tabla
                        System.out.println(f.nodeName());
                        //imprime el texto de la etiqueta y todos sus hijos
                        System.out.println(f.children().text());
                        //tengo k obtener los atos de cada linea de manera que peuda trabajar con ellos, y hacer un 
                        //metodo para pillar los buses de cada parada
                    }
                
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*
            SEGUNDA SERIE DE PRUEBAS (los mertodos de abajo)
         */
        
    }

    public static String[] infoParada(int paradaCod) {
        String[] parada = null;
        try {
            URL paginaVitrasa = new URL("http://infobus.vitrasa.es:8002/Default.aspx?parada=" + paradaCod);
            //Cargamos el documento html con Jsoup (URL,timeout en milisegundos)
            Document doc = Jsoup.parse(paginaVitrasa, 100000);

            //Obtenemos el codigo de la parada             
            String paradaCodigo = doc.getElementById("lblParada").text();

            //Direccion de la parada
            String direccion = doc.getElementById("lblNombre").text();

            if (!direccion.contains("Parada Inexistente")) {
                //Guardamos los datos en un array
                parada = new String[2];
                parada[0] = paradaCodigo;
                parada[1] = direccion;
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        }
        return parada;
    }

    public static void escribirParadas() {
        try {
            XMLOutputFactory xmlof;
            XMLStreamWriter xmlsw;

            xmlof = XMLOutputFactory.newInstance();
            xmlsw = xmlof.createXMLStreamWriter(new FileWriter("listaParadas.xml"));

            xmlsw.writeStartDocument();
            xmlsw.writeStartElement("paradas");

            for (int i = 0; i < 20000; i++) {
                String[] estaParada = infoParada(i);

                if (estaParada != null) {
                    System.out.println("Parada Nº " + estaParada[0] + "  " + estaParada[1]);
                    xmlsw.writeStartElement("parada");
                    xmlsw.writeAttribute("codigo", estaParada[0]);
                    xmlsw.writeCharacters(estaParada[1]);
                    xmlsw.writeEndElement();
                }
            }
            xmlsw.writeEndDocument();
            xmlsw.close();

        } catch (XMLStreamException | IOException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
