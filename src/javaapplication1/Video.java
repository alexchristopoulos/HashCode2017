/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author Paris
 */
public class Video {
    
    public static int idcount=0;
    
    public int id;
    public int sizeMb;

    public Video(int sizeMb) {
        id=idcount;
        idcount++;
        this.sizeMb = sizeMb;
    }
    
    
}
