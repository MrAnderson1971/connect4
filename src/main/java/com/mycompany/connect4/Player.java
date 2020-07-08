/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.connect4;

import java.io.Serializable;

/**
 *
 * @author Thomas Anderson
 */
public class Player implements Serializable
{
    
    private MCTS nn;
    private int number;
    private String name;
    
    public Node parent;
    
    public Player(MCTS nn, int number, String name)
    {
        this.nn = nn;
        this.number = number;
        this.name = name;
        
        this.parent = null;
    }

    public MCTS getNn()
    {
        return nn;
    }
    
    public void setNn(MCTS nn)
    {
        this.nn = nn;
    }

    public int getNumber()
    {
        return number;
    }

    public Node getParent()
    {
        return parent;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
