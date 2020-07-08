/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.connect4;

import java.io.*;
import java.util.*;

/**
 * @author Thomas Anderson
 */
public class MCTS implements Serializable
{

    private Node root;
    private Player player;
    private Player opponent;

    public MCTS(Board board, Player player, Player opponent)
    {
        this.root = new Node(board, null, Integer.MIN_VALUE, player, opponent);
        this.player = player;
        this.opponent = opponent;
    }

    public void setRoot(Node root)
    {
        this.root = root;
    }

    public Node getRoot()
    {
        return root;
    }

    public void train(int times)
    {
        Node leaf = traverse(root);

        ArrayList<Integer> moves = leaf.getBoard().getPossibleMoves();

        if (!moves.isEmpty())
        {
            Player result;

            if (leaf.getWinner() == null)
            {
                // Brute force all possible moves.
                for (int move : moves)
                {
                    Board boardCopy = (Board) leaf.getBoard().clone();
                    boardCopy.move(move);
                    Player winning = boardCopy.win();
                    leaf.addChild(boardCopy, winning, move);
                }

                ArrayList<Node> winningNodes = new ArrayList<>();
                for (Node child : leaf.getChildren())
                {
                    if (child.getWinner() != null)
                    {
                        winningNodes.add(child);
                    }
                }

                if (!winningNodes.isEmpty())
                {
                    leaf = winningNodes.get(0);
                    result = leaf.getWinner();
                } else
                {
                    leaf = (Node) Connect4.randomChoice(leaf.getChildren());
                    result = rollout((Board) leaf.getBoard().clone());
                }
            } else
            {
                result = leaf.getWinner();
            }
            backpropagate(leaf, result);

        }

        if (times > 0)
        {
            train(times - 1);
        }
    }

    private void backpropagate(Node node, Player result)
    {
        if (node == null)
        {
            return;
        }
        node.addGame();

        if (node.getPlayer() == result)
        {
            node.addWin();
        }

        backpropagate(node.getParent(), result);
    }

    private Player rollout(Board board)
    {
        while (true)
        {
            ArrayList<Integer> moves = board.getPossibleMoves();
            if (moves.isEmpty())
            {
                return null;
            }

            ArrayList<Integer> winningMoves = new ArrayList<>();
            ArrayList<Integer> losingMoves = new ArrayList<>();

            for (int move : moves)
            {
                Board boardCopy = (Board) board.clone();
                boardCopy.move(move);
                if (boardCopy.win() == board.getCurrentPlayer())
                {
                    winningMoves.add(move);
                }
            }

            for (int move : moves)
            {
                Board boardCopy = (Board) board.clone();
                boardCopy.move(move, board.getOpponent());
                if (boardCopy.win() == board.getOpponent())
                {
                    losingMoves.add(move);
                }
            }

            int selectedMove;
            if (!winningMoves.isEmpty())
            {
                selectedMove = winningMoves.get(0);
            } else if (losingMoves.size() == 1)
            {
                selectedMove = losingMoves.get(0);
            } else
            {
                selectedMove = (int) Connect4.randomChoice(moves);
            }

            board.move(selectedMove);
            if (board.win() != null)
            {
                return board.getOpponent();
            }
        }
    }

    public void train()
    {
        train(128);
    }

    private Node traverse(Node node)
    {
        while (!node.getChildren().isEmpty())
        {
            node = node.getBestUCT();
        }
        return node;
    }

    public void setBestMove()
    {
        root = root.getBestMove();
    }

    public void setChildWithMove(int move)
    {
        root = root.getChildWithMove(move);
    }
}
