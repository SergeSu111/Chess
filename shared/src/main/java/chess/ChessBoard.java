package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    /*
    * 创建了array of array, 8行8列
    * */
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard(ChessPiece[][] squares) {
        this.squares = squares;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        /*
        * call ChessPosition和ChessPiece里的方法来得到放置的行和列. 让piece放到这个位置*/
        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position)
    {
        if (this.squares[position.getRow()][position.getColumn()] == null)
        {
            return null;
        }
        return this.squares[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        /*
        * 将board清空到最开始的时候. 是有一个固定的放置模式. 根据这个模式来写*/

    }
}
