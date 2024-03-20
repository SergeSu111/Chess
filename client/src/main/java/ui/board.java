package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class board {

    public static ChessBoard board = new ChessBoard();
    static  // 确保每次加载board前重置board
    {
        board.resetBoard();
    }

//    private static final int BOARD_SIZE_IN_SQUARES = 8; // board size :8*8
//    private static final int LINE_WIDTH_IN_CHARS = 1; // margin 单元格和单元格之间的距离
//    private static final int SQUARE_SIZE_IN_CHARS = 3; // 每个单元格的大小

    private static final String boardSpaceBlackColor = SET_BG_COLOR_GREEN;
    private static final String boardSpaceWhiteColor = SET_BG_COLOR_RED;
    private static final String SeparateColor = RESET_BG_COLOR; // 分离两个board的中间颜色;
    private static final String boardEdgeColor = SET_BG_COLOR_LIGHT_GREY; // 棋子四周的颜色

    //private static final String EMPTY = "   ";  //

    public static void drawWholeBoard(ChessBoard board) {
        /*System out 用于将文本输出到控制台, true代表每次使用print 或println的时候都会自动输出 没有缓冲, */
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN); // 清屏
        drawBoard( board, ChessGame.TeamColor.BLACK); // 先画棋子是black的board
        drawEmptyRows(System.out, SeparateColor);
        drawBoard(board, ChessGame.TeamColor.WHITE);
    }


    public static void drawBoard(ChessBoard board, ChessGame.TeamColor callColor)
    {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        // get the sequences

        int [] rowSequence;
        int [] colSequence;
        int spaceColorDesign;
        if (callColor == ChessGame.TeamColor.BLACK)
        {
            spaceColorDesign = 1; // 用来判断row的行数是否是奇数还是偶数来判断body里第一个小空格是白色还是黑色
            rowSequence = new int [] {1,2,3,4,5,6,7,8};
            colSequence = new int [] {8,7,6,5,4,3,2,1};
        }
        else // white
        {
            spaceColorDesign = 0;
            rowSequence = new int [] {8,7,6,5,4,3,2,1};
            colSequence = new int [] {1,2,3,4,5,6,7,8};
        }

        drawHeaders(out, callColor); // 将所有headers都打印好了

        // 打印左右两边的rows sequences 和board里的棋子
        ChessGame.TeamColor currSpaceColor; // 当前每一个小空格里的背景颜色
        for (int row : rowSequence) // 先打印左边的rows sequence
        {
            out.print(boardEdgeColor); // 背景颜色也是灰色  和headers一样
            out.print(SET_TEXT_COLOR_BLACK); // sequences text颜色是黑色的
            drawLetterinSpace(out, String.valueOf(row));    // 将每一个字母都分开打印 将row 数字转换为字符串

            // 接下来开始判断board里开头是黑色还是白色了 开始画棋盘body了
            if (row % 2 == spaceColorDesign)  // 不需要知道是0还是1 teamcolor是黑色还是白色. 只要一个不是 那肯定是另一个分支
            {
                currSpaceColor = ChessGame.TeamColor.WHITE; // 当前小空格颜色为白色
            }
            else
            {
                currSpaceColor = ChessGame.TeamColor.BLACK; // 当前小空格颜色为黑色
            }
            // 接下来开始搞每一行里的剩余的小格子
            for (int column: colSequence)
            {
                if (currSpaceColor == ChessGame.TeamColor.WHITE) // 如果当前行开头颜色是白色
                {
                    out.print(boardSpaceBlackColor); // 则下一个棋子颜色是黑色
                    currSpaceColor = ChessGame.TeamColor.BLACK; // 然后将当前的背景颜色设置为黑色
                }
                else
                {
                    out.print(boardSpaceWhiteColor); // 如果开头棋子是黑色 则下一个棋子是白色
                    currSpaceColor = ChessGame.TeamColor.WHITE; // 然后将当前背景颜色设置为白色
                }
                // 分层好了body里每一个棋子背景颜色以后后开始搞里面的piece call getPiece来得到棋子
                ChessPiece currPiece = board.getPiece(new ChessPosition(row, column));
                if (!(currPiece == null)) // 如果确实得到了棋子
                {
                    drawPeice(out, currPiece.getPieceType(), currPiece.getTeamColor()); // 将当前棋子打印到board里
                }
                else
                {
                    out.print(EMPTY); // 否则的话则是空的 那么就打印全角空格string 表示空的
                }
            }

            // 棋子也都打印好了 接下来就是恢复出厂设置
            out.print(boardEdgeColor); // 恢复成灰色
            out.print(" \u2003 ");
            out.print(RESET_BG_COLOR);
            out.print(System.lineSeparator());
        }
        drawFooter(out); // 接下来打印footer
    }

    private static void drawHeaders(PrintStream out, ChessGame.TeamColor callColor)
    {
        // 以下是设置title的整体背景颜色 和第一个letter前的空隙 以及设置颜色为黑色
        // 并且设置两种情况自己的headers是什么
        String [] headers;
        out.print(boardEdgeColor); // board四周边缘的颜色
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

    /*画每一个piece*/
    private static void drawPeice(PrintStream out, ChessPiece.PieceType type, ChessGame.TeamColor color)
    {
        if (color == ChessGame.TeamColor.WHITE) // 如果要画的棋子是白色
        {
            out.print(SET_TEXT_COLOR_WHITE);  // 那么就设置text颜色为白色
        }
        else
        {
            out.print(SET_TEXT_COLOR_BLACK); // 否则是黑色  则设置text颜色为黑色
        }
        drawPieceIcon(out, type);
    }

    private static void drawPieceIcon(PrintStream out, ChessPiece.PieceType type) // 将棋子画出来
    {
        String pieceString;
        switch (type)
        {
            case KING -> pieceString = BLACK_KING;
            case QUEEN -> pieceString = BLACK_QUEEN;
            case BISHOP -> pieceString = BLACK_BISHOP;
            case ROOK -> pieceString = BLACK_ROOK;
            case KNIGHT -> pieceString = BLACK_KNIGHT;
            case PAWN -> pieceString = BLACK_PAWN;
            default -> pieceString = " \u2003 ";
        }
        out.print(pieceString);

    }

    private static void drawFooter(PrintStream out)
    {
        drawEmptyRows(out, boardEdgeColor);
    }

    private static void drawEmptyRows(PrintStream out, String color)
    {
        out.print(color);
        for (int i = 0; i <= 9; i++)
        {
            out.print(" \u2003 ");
        }
        out.print(RESET_BG_COLOR);
        out.print(System.lineSeparator());
    }

}
