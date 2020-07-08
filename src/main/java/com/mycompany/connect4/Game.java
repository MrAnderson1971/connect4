/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.connect4;

import java.util.*;

/**
 *
 * @author Thomas Anderson
 */
public class Game
{

    private Player player1;
    private Player player2;

    private Board board;

    public Game(Player player1, Player player2)
    {
        this.player1 = player1;
        this.player2 = player2;

        this.board = new Board(this.player1, this.player2);
    }

    public Board getBoard()
    {
        return board;
    }

    public int update()
    {
        int move = getMove();
        move(move);
        return move;
    }

    public void start()
    {
        if (player1.getNn() != null)
        {
            Node load = Node.load(Connect4.path + "player1.txt");
            if (load != null)
            {
                player1.getNn().setRoot(load);
            }
            player1.getNn().train();
            player1.parent = player1.getNn().getRoot();
        }
    }

    public int getMove()
    {
        Scanner scan = new Scanner(System.in);
        if (board.getCurrentPlayer().getNn() == null)
        {
            do
            {
                System.out.println(board.getCurrentPlayer() + "'s turn: ");
                System.out.println("Enter a number between 0-6:");
                String value = scan.nextLine();

                if (!isDigits(value))
                {
                    System.out.println("Please enter a valid number.");
                    continue;
                }

                int i = Integer.parseInt(value);
                if (i < 0 || i >= board.getBoard().length)
                {
                    System.out.println("Please enter a valid number.");
                    continue;
                }

                if (!board.isValidMove(i))
                {
                    System.out.println("Space occupied.");
                    continue;
                }
                return i;

            } while (true);

        }

        System.out.println(board.getCurrentPlayer() + "'s turn:");
        int bestPossibleMove = board.getBestPossibleMove();
        return bestPossibleMove;
    }

    public static boolean isDigits(String s)
    {
        try
        {
            int i = Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException ex)
        {
            return false;
        }
        return true;
    }

    public Player win()
    {
        return board.win();
    }

    public void move(int space)
    {
        board.move(space);
    }

    public boolean isFull()
    {
        return board.isFull();
    }

    @Override
    public String toString()
    {
        String rep = "\n0|1|2|3|4|5|6\n-------------";
        int[][] inverse = new int[board.getBoard()[0].length][board.getBoard().length];

        for (int x = 0; x < inverse.length; x++)
        {
            for (int y = 0; y < inverse[x].length; y++)
            {
                inverse[x][y] = board.getBoard()[y][x];
            }
        }

        for (int[] inverse1 : inverse)
        {
            String line = "\n0|1|2|3|4|5|6\n-------------";
            for (int j = 0; j < inverse1.length; j++)
            {
                switch (inverse1[j])
                {
                    case 1:
                        line = line.replace("" + j, "O");
                        break;
                    case -1:
                        line = line.replace("" + j, "X");
                        break;
                    default:
                        line = line.replace("" + j, " ");
                        break;
                }
            }
            rep += line;
        }

        return rep;
    }
}
