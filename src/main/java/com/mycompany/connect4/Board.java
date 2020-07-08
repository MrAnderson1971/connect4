/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.connect4;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Thomas Anderson
 */
public class Board implements Cloneable, Serializable
{

    private int[][] board;
    private Player player1;
    private Player player2;

    private Player currentPlayer;

    public Board(int[][] board, Player player1, Player player2)
    {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;

        this.currentPlayer = this.player2;
    }

    public Board(Player player1, Player player2)
    {
        this(new int[Connect4.WIDTH][Connect4.HEIGHT], player1, player2);
    }

    public int[][] getBoard()
    {
        return board;
    }

    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    public Player win()
    {
        ArrayList<Player> p = new ArrayList<>(Arrays.asList(player1, player2));

        for (Player player : p)
        {
            int n = player.getNumber();

            // horizontals
            for (int x = 0; x < board.length - 3; x++)
            {
                for (int y = 0; y < board[x].length; y++)
                {
                    if (board[x][y] == n && board[x + 1][y] == n && board[x + 2][y] == n && board[x + 3][y] == n)
                    {
                        return player;
                    }
                }
            }

            // verticals
            for (int[] board1 : board)
            {
                for (int y = 0; y < board1.length - 3; y++)
                {
                    if (board1[y] == n && board1[y + 1] == n && board1[y + 2] == n && board1[y + 3] == n)
                    {
                        return player;
                    }
                }
            }

            // / diagonals
            for (int x = 0; x < board.length - 3; x++)
            {
                for (int y = 3; y < board[x].length; y++)
                {
                    if (board[x][y] == n && board[x + 1][y - 1] == n && board[x + 2][y - 2] == n && board[x + 3][y - 3] == n)
                    {
                        return player;
                    }
                }
            }

            // \ diagonals
            for (int x = 0; x < board.length - 3; x++)
            {
                for (int y = 0; y < board[x].length - 3; y++)
                {
                    if (board[x][y] == n && board[x + 1][y + 1] == n && board[x + 2][y + 2] == n && board[x + 3][y + 3] == n)
                    {
                        return player;
                    }
                }
            }
        }
        return null;
    }

    public boolean isValidMove(int move)
    {
        return board[move][0] == 0;
    }

    public Board move(int space)
    {

        for (int i = 0; i < board[space].length; i++)
        {
            if (board[space][i] != 0)
            {
                board[space][i - 1] = currentPlayer.getNumber();
                currentPlayer = getOpponent();
                return this;
            }
        }

        board[space][board[space].length - 1] = currentPlayer.getNumber();
        currentPlayer = getOpponent();
        return this;
    }

    public Board move(int space, Player player)
    {
        for (int i = 0; i < board[space].length; i++)
        {
            if (board[space][i] != 0)
            {
                board[space][i - 1] = player.getNumber();
                return this;
            }
        }

        board[space][board[space].length - 1] = player.getNumber();
        return this;
    }

    public Player getOpponent()
    {
        return (currentPlayer == player1) ? player2 : player1;
    }

    public ArrayList<Integer> getPossibleMoves()
    {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        for (int move = 0; move < board.length; move++)
        {
            if (isValidMove(move))
            {
                possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }

    @Override
    public Object clone()
    {
        int[][] copy = new int[board.length][board[0].length];
        for (int x = 0; x < copy.length; x++)
        {
            System.arraycopy(board[x], 0, copy[x], 0, copy[0].length);
        }

        Board newBoard = new Board(copy, player1, player2);
        newBoard.currentPlayer = currentPlayer;
        return newBoard;
    }

    public int getBestPossibleMove()
    {
        currentPlayer.getNn().train();
        currentPlayer.getNn().setBestMove();
        int bestMove = currentPlayer.getNn().getRoot().getMove();
        currentPlayer.getNn().train();
        return bestMove;
    }

    public boolean isFull()
    {
        return getPossibleMoves().isEmpty();
    }
}
