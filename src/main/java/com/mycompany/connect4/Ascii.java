/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.connect4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Anderson
 */
public class Ascii
{
    public static void print()
    {
        try
        {
            Scanner scan = new Scanner(new File(Connect4.path + "ascii.txt"));
            while (scan.hasNextLine())
            {
                System.out.println(scan.nextLine());
            }
        } catch (FileNotFoundException ex)
        {
            //Logger.getLogger(Ascii.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
