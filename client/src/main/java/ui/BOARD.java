package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

import static ui.EscapeSequences.*;

import static ui.EscapeSequences.SET_BG_COLOR_DARK_GREY;

public class BOARD {
    public static void main(String[] args) {
        drawGeneralBoard(new ChessBoard(), "White");
    }

    public static void drawGeneralBoard(ChessBoard chessBoard, String teamColor) {
        for (int row = 0; row < 10; row++) {
            if (row == 0 || row == 9) {
                //print out the top and bottom line headers
                String[] header = new String[]{"  ", "a", "b", "c", "d", "e", "f", "g", "h"};
                if (teamColor.equalsIgnoreCase("white")) {
                    System.out.print(" \u2003 ");
                    for (String letter : header) {
                        System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_BOLD + letter);
                        System.out.print(" \u2003");
                    }
                    System.out.print("\n");
                } else if (teamColor.equalsIgnoreCase("black")) {
                    System.out.print("    \u2003 ");
                    for (int index = header.length - 1; index >= 0; index--) {
                        System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_BOLD + header[index]);
                        System.out.print(" \u2003");
                    }
                    System.out.print("\n");
                }
            } else {
                for (int col = 0; col < 10; col++) {
                    if (col == 0 || col == 9) {
                        System.out.print(SET_BG_COLOR_DARK_GREY + " \u2003");
                        // print the number on the side
                        if (teamColor.equalsIgnoreCase("white")) {
                            System.out.print(SET_TEXT_BOLD + (9 - row + " \u2003"));
                        } else {
                            System.out.print(SET_TEXT_BOLD + (row + " \u2003"));
                        }

                    } else {
                        ChessPiece currentPiece;
                        if (teamColor.equalsIgnoreCase("white")) {
                            currentPiece = chessBoard.getPiece(new ChessPosition(9 - row, col));
                        } else {
                            currentPiece = chessBoard.getPiece(new ChessPosition(row, 9 - col));
                        }
                        String piece = "";
                        // print the actual board
                        if ((row + col) % 2 == 0) {
                            System.out.print(SET_BG_COLOR_WHITE);
                        } else {
                            System.out.print(SET_BG_COLOR_BLACK);
                        }
                        if (currentPiece != null) {
                            if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                                System.out.print(SET_TEXT_BOLD);
                                switch (currentPiece.getPieceType()) {
                                    case KING -> piece = BLACK_KING;
                                    case QUEEN -> piece = BLACK_QUEEN;
                                    case BISHOP -> piece = BLACK_BISHOP;
                                    case ROOK -> piece = BLACK_ROOK;
                                    case KNIGHT -> piece = BLACK_KNIGHT;
                                    case PAWN -> piece = BLACK_PAWN;
                                }
                            } else {
                                switch (currentPiece.getPieceType()) {
                                    case KING -> piece = WHITE_KING;
                                    case QUEEN -> piece = WHITE_QUEEN;
                                    case BISHOP -> piece = WHITE_BISHOP;
                                    case ROOK -> piece = WHITE_ROOK;
                                    case KNIGHT -> piece = WHITE_KNIGHT;
                                    case PAWN -> piece = WHITE_PAWN;

                                }
                            }
                            System.out.print(piece);

                        } else {
                            System.out.print(EMPTY);
                        }
                    }
                }
                System.out.print("\n");
            }
        }
    }
}