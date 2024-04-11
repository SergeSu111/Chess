package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class BOARD {

    public static ChessBoard board = new ChessBoard();
    static  // 确保每次加载board前重置board
    {
        board.resetBoard();
    }

    public static ChessBoard getDefaultBoard()
    {
        return board;
    }


    private static final String boardSpaceBlackColor = SET_BG_COLOR_GREEN;
    private static final String boardSpaceWhiteColor = SET_BG_COLOR_RED;
    private static final String SeparateColor = RESET_BG_COLOR; // 分离两个board的中间颜色;
    private static final String EdgeColor = SET_BG_COLOR_LIGHT_GREY; // 棋子四周的颜色



    private static void drawHeaders(PrintStream out, ChessGame.TeamColor callColor)
    {
        // 以下是设置title的整体背景颜色 和第一个letter前的空隙 以及设置颜色为黑色
        // 并且设置两种情况自己的headers是什么
        String [] headers;
        out.print(EdgeColor); // board四周边缘的颜色
        out.print(" \u2003 "); // 全角空格 目的是将title一开始有一些空隙
        // 设置title颜色为黑色
        out.print(SET_TEXT_COLOR_BLACK);
        // 如果当前颜色是team color的白色的话
        if(callColor == ChessGame.TeamColor.WHITE)
        {
            // header就是a b c d ..
            headers = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        }
        else    // 如果当前颜色是黑色的话
        {
            headers = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
        }


        // 开始将header里每一个letter都根据间隙打印出来

        // 循环每一个letter in headers
        for (String letter: headers)
        {
            // call drawHeader;
            drawLetterinSpace(out, letter);
        }


        // headers全部打印完以后开始重新设置空隙 背景颜色 等等
        out.print(" \u2003 ");
        out.print(RESET_BG_COLOR);
        out.println(); // 换行 结束headers

    }

    /*将标题里每一个小letter都隔开打印*/
    private static void drawLetterinSpace(PrintStream out, String letter)
    {
        out.print("\u2006 "); // 1/6 空格
        out.print(letter);
        out.print("\u2006 "); // 1/6 空格 为下一个letter做准备
    }



}