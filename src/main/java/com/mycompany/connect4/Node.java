/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.connect4;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Anderson
 */
public class Node implements Serializable
{

    private Board board;
    private Player winner;
    private int move;
    private Player player;
    private Player opponent;

    private int wins;
    private int games;
    private ArrayList<Node> children;
    private Node parent;

    public Node(Board board, Player winner, int move, Player player, Player opponent)
    {
        this.board = board;
        this.winner = winner;
        this.move = move;
        this.player = player;
        this.opponent = opponent;

        this.wins = this.games = 0;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public Board getBoard()
    {
        return board;
    }

    public Player getWinner()
    {
        return winner;
    }

    public int getMove()
    {
        return move;
    }

    public Player getPlayer()
    {
        return player;
    }

    public ArrayList<Node> getChildren()
    {
        return children;
    }

    public Node getParent()
    {
        return parent;
    }

    public double getUCT()
    {
        if (games == 0)
        {
            return Double.NaN;
        }
        return (((double) wins) / games) + Math.sqrt(2 * Math.log(parent.games) / games);
    }

    public Node getBestMove()
    {
        if (children.isEmpty())
        {
            return null;
        }

        ArrayList<Node> winningMoves = new ArrayList<>();
        for (Node child : children)
        {
            if (child.winner == player)
            {
                winningMoves.add(child);
            }
        }

        if (!winningMoves.isEmpty())
        {
            return winningMoves.get(0);
        }

        ArrayList<Double> game = new ArrayList<>();
        for (Node child : children)
        {
            if (child.games > 0)
            {
                game.add(((double) child.wins) / child.games);
            } else
            {
                game.add(0.0);
            }
        }

        return children.get(Connect4.argmax(game));

    }

    public void addChild(Board state, Player winning, int move)
    {
        Node child = new Node(state, winning, move, board.getCurrentPlayer(), board.getOpponent());
        child.parent = this;
        children.add(child);
    }

    public Node getChildWithMove(int move)
    {
        if (children.isEmpty())
        {
            throw new NullPointerException("Empty");
        }

        for (Node child : children)
        {
            if (child.move == move)
            {
                return child;
            }
        }

        throw new NullPointerException("No child found.");
    }

    public Node getBestUCT()
    {
        ArrayList<Double> ucts = new ArrayList<>();
        for (Node child : children)
        {
            ucts.add(child.getUCT());
        }
        if (ucts.contains(Double.NaN))
        {
            return (Node) Connect4.randomChoice(children);
        }
        return children.get(Connect4.argmax(ucts));
    }

    public void addGame()
    {
        games++;
    }

    public void addWin()
    {
        wins++;
    }

    public void export(String path)
    {
        FileOutputStream f = null;
        ObjectOutputStream o = null;
        try
        {
            f = new FileOutputStream(new File(path));
            o = new ObjectOutputStream(f);
            o.writeObject(this);
        } catch (IOException ex)
        {
            Logger.getLogger(MCTS.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                f.close();
                o.close();
            } catch (IOException | NullPointerException ex)
            {
                Logger.getLogger(MCTS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Node load(String path)
    {
        System.out.println("Reading save file...");
        FileInputStream f = null;
        ObjectInputStream o = null;
        Object tree = null;
        try
        {
            f = new FileInputStream(new File(path));
            o = new ObjectInputStream(f);
            tree = o.readObject();
        } catch (IOException  ex)
        {
            //Logger.getLogger(MCTS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(MCTS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
            finally
        {
            try
            {
                f.close();
                o.close();
            } catch (IOException | NullPointerException ex)
            {
                //Logger.getLogger(MCTS.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

        return (Node) tree;
    }
}
