/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaxml;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Ordenador
 */
public class JavaXML {
    
    private JavaXML java;

    private static ObjectContainer oc;
    private static int id;
    private static String nombre, username, password;

    private static void open() {
        //Creamos la conexion y el archivo que almacenara los datos
        oc = Db4o.openFile("databasePersona.yap");
    }

    public static boolean Insertar(Persona objeto) {
        try {
            //Buscamos si existe el objeto, si no insertamos el objeto recibido en la base de datos
            open(); 
            ObjectSet result = oc.queryByExample(objeto.getId());// Con este metodo procedemos a buscar si hay personas con el id que vayamos a ingresar
            if(result != null){//Si el resultado es diferente de nulo eso quiere decir que si hay una persona registrada con ese did
            JOptionPane.showMessageDialog(null, "Ya hay una persona registrada con ese id", "ERROR", JOptionPane.ERROR_MESSAGE);
                System.out.println("Persona no guardda");
            return false;
            } else { // Caso contrario procedera a registrar la persona  
            oc.set(objeto);
            oc.close();
                System.out.println("perssona guardada");
            return true;
            }
        } catch (DatabaseClosedException | DatabaseReadOnlyException e) {
            System.out.println("bdoo.Controlador.InsertarPersona() : " + e);
            return false;
        }
    }
    public static boolean InsertarArreglo(Persona[] persona) {
        try {
            //Buscamos si existe el objeto, si no insertamos el objeto recibido en la base de datos
            
            open();
            oc.set(persona);
            oc.close();
            return true;
        } catch (DatabaseClosedException | DatabaseReadOnlyException e) {
            System.out.println("bdoo.Controlador.InsertarPersona() : " + e);
            return false;
        }
    }
    
    public Persona buscarp(Persona persona){
        open();
        Persona encontrado = null;
        ObjectSet resultados = oc.get(persona);
        if(resultados.hasNext()){
            encontrado = (Persona) resultados.next();
            
        }
        oc.close();
        return encontrado;
    }
    
    public Persona[] BuscarPersonas(Persona persona) {
        try {
            Persona[] personas = null;
            this.open();
            ObjectSet resultados = this.oc.get(persona);
            int i = 0;
            if (resultados.hasNext()) {
                personas = new Persona[resultados.size()];
                while (resultados.hasNext()) {  
                    personas[i] = (Persona) resultados.next();
                    i++;
                }
            }
            this.oc.close();
            return personas;
        } catch (DatabaseClosedException | DatabaseReadOnlyException e) {
            System.out.println("bdoo.controlador.buscar()" + e);
            return null;
        }
    }

    public static boolean InsertarPersona(int id, String nombre, String username, String password) {
        Persona persona = new Persona(id, nombre, username, password);
        return Insertar(persona);
    }
    
    public DefaultTableModel mostrarpersonas() {
        String titulos[] = {"ID", "NOMBRE", "USERNAME", "PASSWORD"};
        DefaultTableModel dtm = new DefaultTableModel(null, titulos);
        Persona persona = null;
        Persona[] p = BuscarPersonas(persona);
        if (p != null) {
            for (Persona per : p) {
                Object[] cli = new Object[4];
                cli[0] = per.getId();
                cli[1] = per.getNombre();
                cli[2] = per.getUsername();
                cli[3] = per.getPassword();
                dtm.addRow(cli);
            }
        }
        return dtm;
    }

    public  void leer() {
        try {
            File archivo = new File("Datos.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document d = documentBuilder.parse(archivo);
            d.getDocumentElement().normalize();
            NodeList listDocentes = d.getElementsByTagName("docente");
            for (int i = 0; i < listDocentes.getLength(); i++) {
                Node nodo = listDocentes.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodo;
                    JOptionPane.showMessageDialog(null, "Id: "+"'"+element.getAttribute("id")+"'    "
                            +" Nombre: "+"'"+element.getElementsByTagName("nombre").item(0).getTextContent()+"'    "
                            +" Username: "+"'" +element.getElementsByTagName("username").item(0).getTextContent()+"'    "
                            +" Password: "+"'" +element.getElementsByTagName("password").item(0).getTextContent()+"'");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void guardar() {
        try {
            File archivo = new File("Datos.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document d = documentBuilder.parse(archivo);
            d.getDocumentElement().normalize();
            NodeList listDocentes = d.getElementsByTagName("docente");
            for (int i = 0; i < listDocentes.getLength(); i++) {
                Node nodo = listDocentes.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodo;
                    id = Integer.parseInt(element.getAttribute("id"));
                    nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
                    username = element.getElementsByTagName("username").item(0).getTextContent();
                    password = element.getElementsByTagName("password").item(0).getTextContent();
                    boolean insertado = InsertarPersona(id, nombre, username, password);
                    if (insertado == true) {
                        JOptionPane.showMessageDialog(null, "Persona Guardada");
                    } else {
                        JOptionPane.showMessageDialog(null, "Algo Fallo al guardar la persona", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  Persona[] guardarArreglo(){// Este es el metodo donde las personas se guardan en el arreglo
        Persona[] arreglo = new Persona[10];
        Persona p;
        try {
            File archivo = new File("Datos.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document d = documentBuilder.parse(archivo);
            d.getDocumentElement().normalize();
            NodeList listDocentes = d.getElementsByTagName("docente");
            for (int i = 0; i < listDocentes.getLength(); i++) {
                Node nodo = listDocentes.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodo;
                    id = Integer.parseInt(element.getAttribute("id"));
                    nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
                    username = element.getElementsByTagName("username").item(0).getTextContent();
                    password = element.getElementsByTagName("password").item(0).getTextContent();
                    p = new Persona(id, nombre, username, password);
                    arreglo[i] = p;
                    InsertarArreglo(arreglo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arreglo;
    }

    }
    
