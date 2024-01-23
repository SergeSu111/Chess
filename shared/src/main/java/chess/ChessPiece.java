package chess;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type)
    {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */

    // 这是一个enum类型的数据类型叫做PieceType, 是public的, 所以在其他class里也可以使用PieceType.
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;

    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // 1. 创建一个current Position的piece.  根据这个piece来得到color.
        ChessPiece piece_now = board.getPiece(myPosition);
        ChessGame.TeamColor current_color  = piece_now.pieceColor;

        // get the location of current piece
        int column = myPosition.getColumn();
        int row = myPosition.getRow();


        // 2. 创建一个空的chess_move ArrayList, 根据这个piece 的type来选择switch的branch, 然后把the way of movement 放到这个ListArray中
        ArrayList<ChessMove> my_movements = new ArrayList<>();
        // Switch
        switch(piece_now.type)
        {
            case KING:

                break;
            case BISHOP:
                // 因为diagonal返回的是一个ArrayList, 最后让返回的ArrayList 复制给my_movement就可以了
                my_movements = diagonal(board, column, row, current_color, piece_now.type);
                break;
            case QUEEN:
                break;
            case KNIGHT:
                break;
            case ROOK:
                break;
            case PAWN:
                break;
        }




        // Each type piece's end positions. if pawn, add all 4 moves to my_movements. Same position, different types.
        //my_movements.add(new ChessMove(myPosition, new ChessPosition(1,4), null));
        return  my_movements;

    }

    /*
    *  Diagonal function 接受一个board, 当前piece的行和列, 以及当前piece的color和当前Piece的Type.
    *  它会返回Bishop斜着走的每一种走法. 得到每一种走法的起点和终点位置. 
    * */
    public static ArrayList<ChessMove> diagonal(ChessBoard board, int column, int row, ChessGame.TeamColor my_color, PieceType my_type)
    {

        // create the current position of this piece
        ChessPosition start_position = new ChessPosition(row, column);

        // initialize the empty ArrayList for storing the movement ways of piece type
        ArrayList<ChessMove> moves  = new ArrayList<>();

        // right-down
        for (int i = row - 1, j = column + 1; i >= 1 && j <= 8; i--, j++) {
            moves.add(new ChessMove(start_position, new ChessPosition(i, j), null));


        }




        return moves;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
