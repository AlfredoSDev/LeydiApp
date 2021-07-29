/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Alfredo
 */
public class Calendario {
    public static String anno(){
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy");
        return dtf.format(LocalDateTime.now());
    }
    public static String mes(){
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("MM");
        return dtf.format(LocalDateTime.now());
    }
    public static String dia(){
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("dd");
        return dtf.format(LocalDateTime.now());
    }
    public static String hora(){
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("hh");
        return dtf.format(LocalDateTime.now());
    }
    public static String minuto(){
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("mm");
        return dtf.format(LocalDateTime.now());
    }
    public static String fecha(){
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy_MM_dd");
        return dtf.format(LocalDateTime.now());
    }
    public static String fechaYHora(){
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy_MM_dd__hh:mm");
        return dtf.format(LocalDateTime.now());
    }
    
}
