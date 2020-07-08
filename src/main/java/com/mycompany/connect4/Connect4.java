package com.mycompany.connect4;

import java.io.File;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Thomas Anderson
 */
public class Connect4
{

    public static final String path = new File(" ").getAbsolutePath().strip();

    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;

    public static void main(String[] args)
    {
        System.out.println("I've got to warn you, it's a learning robot. Every moment you spend fighting it only increases its knowledge of how to beat you.");
        Ascii.print();

        Scanner scan = new Scanner(System.in);
        System.out.println("Hello human. Please enter your name.");
        
        Player player1 = new Player(null, 1, "SCP-079");
        Player player2 = new Player(null, -1, scan.nextLine());
        System.out.println("Greetings, " + player2 + ".");

        Game game = new Game(player1, player2);
        
        player1.setNn(new MCTS((Board) game.getBoard().clone(), player1, player2));
        
        game.start();

        while (game.win() == null && !game.getBoard().isFull())
        {
            System.out.println(game);
            int move = game.update();
            
            if (game.getBoard().getCurrentPlayer() == player1)
            {
                player1.getNn().train();
                player1.getNn().setChildWithMove(move);
                player1.getNn().train();
            }
        }
        System.out.println(game);
        System.out.println(game.win() + " won.");
        
        player1.getNn().setRoot(player1.parent);
        player1.parent.export(path + "player1.txt");
        scan.nextLine();
    }

    public static int argmax(ArrayList<Double> arr)
    {
        double maxObject = arr.get(0);
        int maxIndex = 0;
        for (int i = 0; i < arr.size(); i++)
        {
            if (arr.get(i) > maxObject)
            {
                maxObject = arr.get(i);
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static Object randomChoice(List<?> items)
    {
        return items.get(new Random().nextInt(items.size()));
    }
}
