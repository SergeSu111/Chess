

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import chess.ChessGame;
import ui.board;

class DrawBoardTest {
    @BeforeEach
    void setUp() {}

    @Test
    void printBoardDefault() {
        board.drawBoard(board.board, ChessGame.TeamColor.BLACK);
    }

    @Test
    void printBoardAllDefault() {
        board.drawWholeBoard(board.board);
    }
}