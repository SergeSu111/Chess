package chess;

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


        // 2. 创建一个空的chess_move ArrayList, 根据这个piece 的type来选择switch的branch, 然后把the ways of movement 放到这个ListArray中
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
    *  它会返回斜着走的每一种走法. 得到每一种走法的起点和终点位置. 以及是否会升级. 最后把每一种走法都放到moves ArrayList里然后返回.
    * */
    public static ArrayList<ChessMove> diagonal(ChessBoard board, int column, int row, ChessGame.TeamColor my_color, PieceType my_type) {

        // create the current position of this piece
        ChessPosition start_position = new ChessPosition(row, column);

        // initialize the empty ArrayList for storing the movement ways of piece type
        // 要记住的是moves每一个元素都是ChessMove对象.
        ArrayList<ChessMove> moves = new ArrayList<>();

        // right-down
        for (int i = row - 1, j = column + 1; i >= 1 && j <= 8; i--, j++)
        {
            // 要么遇到同颜色的棋子, 停止了 要么遇到不同颜色棋子, 把它吃了 吸收了它的位置，然后停止
            // 如果move_check 为false. 那么就没有条件能走 直接到下一个循环条件, i和j都会更新.
            if (move_check(board, my_color, start_position, moves, row, column))
            {
                break;
            }

            // 如果我把对面King吃掉了.
            if (my_type == PieceType.KING)
            {
                break; // 那游戏直接结束了
            }
        }

        // left-down
        for (int i = row - 1, j = column - 1; i >= 1 && j >= 1; i--, j--)
        {
            // if 在left-down的情况下, 当前的下一个路径有障碍. 那么if 是true. 就直接break.
            if (move_check(board, my_color, start_position, moves, row, column ))
            {
                break;
            }
            // 或者当前的我的type走到了king把它吃掉了, 则也结束.

            if (my_type == PieceType.KING)
            {
                break;
            }

            // 否则move-check返回false, 证明还能继续走, 那么就回到for循环往下迭代到下一个i j 位置继续走.


        }

        // right up
        for (int i = row + 1, j = column + 1; i <= 8 && j <= 8; j++, i++)
        {
            // if 在right-up的情况下, 当前的下一个路径有障碍. 那么if 是true. 就直接break.
            if( move_check(board, my_color, start_position, moves, row, column))
            {
                break;
            }
            // 或者当前的我的type走到了king把它吃掉了, 则也结束.
            if (my_type == PieceType.KING)
            {
                break;
            }

            // 否则move-check返回false, 证明还能继续走, 那么就回到for循环往下迭代到下一个i j 位置继续走.

        }

        // left up
        for (int i = row + 1, j = column - 1; i <= 8 && j >= 1; i++, j--)
        {
            // if 在left-up的情况下, 当前的下一个路径有障碍. 那么if 是true. 就直接break.
            if (move_check(board, my_color, start_position, moves, row, column))
            {
                break;
            }
            // 或者当前的我的type走到了king把它吃掉了, 则也结束.
            if (my_type == PieceType.KING)
            {
                break;
            }
            // 否则move-check返回false, 证明还能继续走, 那么就回到for循环往下迭代到下一个i j 位置继续走.

        }

        return moves;
        // 在move-check的时候就已经把能走的都放到moves里了. 所以如果不能走直接返回moves.
        // 就能知道有多少种走法.

    }


    public static ArrayList<ChessMove> straight (ChessBoard board, ChessGame.TeamColor this_color, PieceType type, int row, int column)
    {
        // 根据当前传进来的row 和column来得到当前棋子的起始位置.
        // 在当前函数中也创建一个局部的moves ArrayList来存放能够走到的所有走法的ChessMove.

        // up
        // for 循环 根据move_check检查是否有障碍, 有障碍break 检查是否会吃掉KING, break;


        // down
        // for 循环 根据move_check检查是否有障碍, 有障碍break 检查是否会吃掉KING, break;



        // left
        // for 循环 根据move_check检查是否有障碍, 有障碍break 检查是否会吃掉KING, break;

        // right
        // for 循环 根据move_check检查是否有障碍, 有障碍break 检查是否会吃掉KING, break;

        // 返回被存储好的 MOVES.
    }

    /*
    * move_check function 检查每走一步是否有障碍, 如果障碍是别人的棋子, 则直接吃掉, 把对方棋子的位置也加到moves里. 因为我的棋子要往下走.
    * 如果是自己的棋子,则停止移动.
    * */
    public static Boolean move_check(ChessBoard board, ChessGame.TeamColor this_color, ChessPosition start_position, List<ChessMove> moves, int row, int column)
    {
        //  创建下一个observed piece. 位置根据当前的i 和j 来 因为i 和j 都是起始位置的下一个位置
        ChessPiece next_piece = board.getPiece(new ChessPosition(row, column));

        // if observed piece 不为空. 证明遇到了阻碍,
        if(next_piece != null)
        {
            // 观察这个observed piece的颜色, 如果和当前棋子颜色相同.
            if(next_piece.pieceColor == this_color)
            {
                // 么返回true.结束行走.  到此位置. 走不了了 遇到了障碍.
                return true;
            }
            // 如果颜色不相同, 证明遇到了对方棋子, 那么需要吃掉对方棋子, 把对方棋子的位置(i,j) 也放入moves里.
            // 因为i, j就是当前下一步要走的位置.
            moves.add(new ChessMove(start_position, new ChessPosition(row, column), null));
            // 然后再返回true 结束行走 到此位置. 因为也遇到了障碍.
            return true;
        }
        // if observed piece 为空, 证明没有遇到阻碍.那么就继续往前走, 当前走过的路都要当作一个ChessPosition来放到
        // moves里. 因为有可能会在这一步停下.
        moves.add(new ChessMove(start_position, new ChessPosition(row, column), null));
        return false;

    }

    /*
    * Knight走法, 和会有多少种走法.
    * Knight可以先向前或左右走一格或者两格 然后再向90°角走相关的一格或者两格.
    * 取决于Knight一开始走的方向和格数.
    * */
    public static ArrayList<ChessMove> knightMove(ChessBoard board, int row, int column, ChessGame.TeamColor this_color, PieceType this_type)
    {
        // 得到当前棋子的起始位置.
        // 创建一个临时的局部ArrayList moves 用来存储Knight可以走的走法.

        // 声明nextRow
        // 声明nextCol

        // 先上两格 再 左一格
        // 根据每种情况 更新nextRow 和NextCol
        // 如果继续走还在界内的话
            // 则继续move_check来判断是否遇到了阻碍. 如果没遇到阻碍就把当前的位置加到moves里去

        // 先上两格 再 右一格
        // 后面都和上面是一样的

        // 先上一格 再 左两格

        // 先上一格 再 右两格

        // 先下两格 再 左一格

        // 先下两格 再 右一格

        // 先下一格 再 左两格

        // 先下一格 再 右两格


        // return moves.
    }


    /*
    * 判断当前的位置是否还在界内 如果在 返回true 不在返回false*/
    public static boolean inbounds(int row, int col)
    {

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
