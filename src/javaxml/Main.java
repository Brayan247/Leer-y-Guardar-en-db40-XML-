/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaxml;

/**
 *
 * @author Ordenador
 */
public class Main {
    public static void main(String[] args) {
        JavaXML xml = new JavaXML();
       Persona persona = new Persona();
       Persona[] p = new Persona[10];
       p = xml.guardarArreglo();
        for (int i = 0; i < p.length; i++) {// Dentro de este for se me iran presentando las personas
            System.out.println(p[i]);
        }
    }
}
