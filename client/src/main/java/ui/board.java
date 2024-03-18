package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class board {
    private static final int BOARD_SIZE_IN_SQUARES = 8; // board size :8*8
    private static final int LINE_WIDTH_IN_CHARS = 0; // margin 单元格和单元格之间的距离
    private static final int SQUARE_SIZE_IN_CHARS = 2; // 每个单元格的大小

    private static final String EMPTY = "   ";  //

    public static void main(String[] args) {
        /*System out 用于将文本输出到控制台, true代表每次使用print 或println的时候都会自动输出 没有缓冲, */
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN); // 清屏

        drawHeaders(out);

        drawChessBoard(out);

        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void drawHeaders(PrintStream out) {
        setGray(out); // 将背景和字体都设置为深灰色
        String[] headers = {"h", "g", "f", "e", "d", "c", "b", "a"};  // header
        for (int column = 0; column < BOARD_SIZE_IN_SQUARES; column++) {
            drawHeader(out, headers[column]);
        }

        out.println(); // to next line


    }

    private static void drawHeader(PrintStream out, String headerText) {
        printHeaderText(out, headerText);
    }

    private static void printHeaderText(PrintStream out, String letter) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);

        out.print(letter);

        setGray(out);
    }

    private static void drawBoard(PrintStream out)
    {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++)
        {

        }
    }



    private static void setGray(PrintStream out)
    {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }



    private static void drawChessBoard(PrintStream out)
    {}





}