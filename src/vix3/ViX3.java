/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vix3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
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
//        try {
//            URL paginaVitrasa = new URL("http://rutas.vitrasa.es/DisplayParadas.aspx?Linea=1-1-0");
//
//            //De aqui al sigueinte comentario lo uso para ir viendo por consola la web y saber que tengo que sacar de la misma
//            URLConnection con = paginaVitrasa.openConnection();
//
//            InputStream is = con.getInputStream();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//            String line = null;
//
//            while ((line = br.readLine()) != null) {                
//                System.out.println(line);
//            }
//
//            //Cargamos el documento html con Jsoup (URL,timeout en milisegundos)
//            Document doc = Jsoup.parse(paginaVitrasa, 100000);
//
//            //Obtenemos el codigo de la parada             
//            String paradaCod = doc.getElementById("lblParada").text();
//            System.out.println(paradaCod);
//
//            //La hora a la que se recoge la info de la web
//            String hora = doc.getElementById("lblHora").text();
//            System.out.println(hora);
//
//            //Direccion de la parada
//            String direccion = doc.getElementById("lblNombre").text();
//            System.out.println(direccion);
//
//            //Lista de buses?
//            Element tablaBuses = doc.getElementById("GridView1");
//            Elements tbody = tablaBuses.children();
//            for (Element e : tbody) {
//                for (Element f : e.children()) {
//                    //imprime el nombre de la etiqueta (ahora mismo estoy en los tr de la tabla
//                    //System.out.println(f.nodeName());
//                    //imprime el texto de la etiqueta y todos sus hijos
//                    //System.out.println(f.children().text());
//                    if (f.childNodeSize() == 5) {
//                        System.out.println("Linea: " + f.child(0).text());
//                        System.out.println("Ruta: " + f.child(1).text());
//                        System.out.println("Min: " + f.child(2).text());
//                    }
//                    //tengo k obtener los atos de cada linea de manera que peuda trabajar con ellos, y hacer un 
//                    //metodo para pillar los buses de cada parada
//                }
//
//            }
//
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
//        }

        /*
            SEGUNDA SERIE DE PRUEBAS (los mertodos de abajo)
         */
        //escribirParadas();
        //obtenerCoordenadasYListaDeParadasDeUnaLinea("txt lineas/C1.txt");
        leerXml("pruebasXml.xml");
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

            System.out.println("Parada: " + paradaCodigo + ", " + direccion);
            //Lista de autobusses
            String buses = "";

            if (!direccion.contains("SIN ESTIMACIONES")) {
                Element tablaBuses = doc.getElementById("GridView1");
                Elements tbody = tablaBuses.children();
                for (Element e : tbody) {
                    for (Element f : e.children()) {
                        if (f.childNodeSize() == 5) {
//                        System.out.println("Linea: " + f.child(0).text());
//                        System.out.println("Ruta: " + f.child(1).text());
//                        System.out.println("Min: " + f.child(2).text());
                            buses += "Linea: " + f.child(0).text() + "Ruta: " + f.child(1).text() + "///";
                        }

                    }
                }
            }
            System.out.println("Buses: " + buses);

            if (!direccion.contains("Parada Inexistente")) {
                //Guardamos los datos en un array
                parada = new String[3];
                parada[0] = paradaCodigo;
                parada[1] = direccion;
                parada[2] = buses;

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
            xmlsw = xmlof.createXMLStreamWriter(new FileWriter("listaParadas4.xml"));

            xmlsw.writeStartDocument();
            xmlsw.writeStartElement("paradas");

            for (int i = 50000; i < 100000; i++) {
                String[] estaParada = infoParada(i);

                if (estaParada != null) {
                    System.out.println("Parada Nº " + estaParada[0] + "  " + estaParada[1]);
                    xmlsw.writeStartElement("parada");
                    xmlsw.writeAttribute("codigo", estaParada[0]);
                    xmlsw.writeStartElement("direccion");
                    xmlsw.writeCharacters(estaParada[1]);
                    xmlsw.writeEndElement();
                    xmlsw.writeStartElement("autobuses");
                    xmlsw.writeCharacters(estaParada[2]);
                    xmlsw.writeEndElement();
                    xmlsw.writeEndElement();
                }
            }
            xmlsw.writeEndDocument();
            xmlsw.close();

        } catch (XMLStreamException | IOException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void obtenerCoordenadasYListaDeParadasDeUnaLinea(String rutaTxt) {
        String[] paradas;
        try {
            BufferedReader br = new BufferedReader(new FileReader(rutaTxt));
            while (br.ready()) {
                paradas = br.readLine().split(";");
                for (String p : paradas) {
                    //Esto imprime cada linea del txt, separada por ;
                    //System.out.println(p);
                    if (p.contains("marcaParada =")) {
                        //Localizo el codigo de la parada
                        int i = p.indexOf(":");
                        int f = p.indexOf(".", i);
                        System.out.println(i + ", " + f);
                        System.out.println("Parada codigo:" + p.substring(i + 2, f));
                        //Localizo la latitud
                        int g = p.indexOf("LatitudParada=");
                        int h = p.indexOf("&", g);
                        System.out.println("Lat: " + p.substring(g + 14, h));
                        //Localizo la longitud
                        int j = p.indexOf("LongitudParada=");
                        int k = p.indexOf("&", j);
                        System.out.println("Lon: " + p.substring(j + 15, k));
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int[] leerXml(String ruta) {
        int[] paradasCod = null;
        try {            
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            XMLStreamReader xmlsr = xmlif.createXMLStreamReader(new FileReader(ruta));
            System.out.println(xmlsr.hasNext());
            while(xmlsr.hasNext()){
                System.out.println(xmlsr.next());
                if(xmlsr.isStartElement()){
                System.out.println("?");
                }
            }
            
        } catch (XMLStreamException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ViX3.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paradasCod;
    }
}
