package chess;

import java.awt.font.FontRenderContext;
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
                // 因为KING 既可以直线走 也可以斜着走.
                my_movements.addAll(diagonal(board, column, row, current_color, piece_now.type));
                my_movements.addAll(straight(board, current_color, piece_now.type, row, column));
                break;
            case BISHOP:
                // 因为diagonal返回的是一个ArrayList, 最后让返回的ArrayList 复制给my_movement就可以了
                my_movements = diagonal(board, column, row, current_color, piece_now.type);
                break;
            case QUEEN:
                // 因为QUEEN 既可以直线走 也可以斜着走.
                my_movements.addAll(diagonal(board, row, column, current_color, piece_now.type));
                my_movements.addAll(straight(board, current_color, piece_now.type, row, column));
                break;
            case KNIGHT:
                my_movements = knightMove(board,row, column, current_color, piece_now.type);
                break;
            case ROOK:
                my_movements = straight(board, current_color, piece_now.type, row, column);
                break;
            case PAWN:
                break;
        }


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
            if (move_check(board, my_color, start_position, moves, i, j))
            {
                break;
            }
            // 如果我把对面King吃掉了.
            if (my_type == PieceType.KING)
            {
                break;
                // 那游戏直接结束了
            }
        }

        // left-down
        for (int i = row - 1, j = column - 1; i >= 1 && j >= 1; i--, j--)
        {
            // if 在left-down的情况下, 当前的下一个路径有障碍. 那么if 是true. 就直接break.
            if (move_check(board, my_color, start_position, moves, i, j ))
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
            if( move_check(board, my_color, start_position, moves, i, j))
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
            if (move_check(board, my_color, start_position, moves, i, j))
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
        ChessPosition start_position = new ChessPosition(row, column);
        // 在当前函数中也创建一个局部的moves ArrayList来存放能够走到的所有走法的ChessMove.
        ArrayList<ChessMove> moves = new ArrayList<>();

        // up
        // for 循环 根据move_check检查是否有障碍, 有障碍break 检查是否会吃掉KING, break;
        for (int i = row + 1; i <= 8; i++)  // row往上加. 最少走一格 最多走到小于等于8 否则出界了
        {
            move_check(board, this_color, start_position, moves, i, column);
            // 检查往上走的每一步是否有障碍 没障碍会把当前的步数加到moves里, 或者如果吃掉对方棋子, 也会加上去.
        }


        // down
        // for 循环 根据move_check检查是否有障碍, 有障碍break 检查是否会吃掉KING, break;
        for (int i = row - 1; i >= 1; i--)  // row往下减， 最好减一格，最多减到不低于1. 否则出界了
        {
            move_check(board, this_color, start_position, moves, i, column);
            // 检查往下走的每一步是否有障碍, 没障碍的话会把当前步数更新到moves里, 或者吃掉对方的棋子, 也会加上去.
        }


        // left
        // for 循环 根据move_check检查是否有障碍, 有障碍break 检查是否会吃掉KING, break;
        for (int j = column -1; j >= 1; j--)  // column 会往做移 最少移动一位, 最多不能低于1 因为会出界
        {
            move_check(board, this_color, start_position, moves, row, j);
        }

        // right
        // for 循环 根据move_check检查是否有障碍, 有障碍break 检查是否会吃掉KING, break;
        for (int j = column + 1; j <= 8; j++)
        {
            move_check(board, this_color, start_position, moves, row, j);
        }

        // 返回被存储好的 MOVES.
        return moves;
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
        ChessPosition start_position = new ChessPosition(row, column);
        // 创建一个临时的局部ArrayList moves 用来存储Knight可以走的走法.
        ArrayList<ChessMove> moves = new ArrayList<>();

        //声明nexRow 没有赋值的原因是你此时还不确定Knight到底先走哪个方向.
        int next_row;
        // 声明nextCol 没有赋值的原因是你此时还不确定Knight到底先走哪个方向.
        int next_column;

        // 先上两格 再 左一格
            // 根据每种情况 更新nextRow 和NextCol
        next_row = row + 2;
        next_column = column - 1;
        if (inbounds(next_row, next_column))   // 如果继续走还在界内的话
            {
                // 则继续move_check来判断是否遇到了阻碍. 如果没遇到阻碍就把当前的位置加到moves里去
                move_check(board, this_color, start_position, moves, next_row, next_column); // 如果为true 则遇到了阻碍.
            }

        // 先上两格 再 右一格
        next_row = row + 2;
        next_column = column + 1;
        if (inbounds (next_row, next_column))
        {
            move_check(board, this_color, start_position, moves, next_row, next_column); // 如果为true 则遇到了阻碍.
        }


        // 先上一格 再 左两格
        next_row = row + 1;
        next_column = column - 2;
        if (inbounds (next_row, next_column))
        {
            move_check(board, this_color, start_position, moves, next_row, next_column); // 如果为true 则遇到了阻碍.
        }

        // 先上一格 再 右两格
        next_row = row + 1;
        next_column = column + 2;
        if (inbounds(next_row, next_column))  // 为True证明没有出界
        {
            move_check(board, this_color, start_position, moves, next_row, next_column); // 如果为true 则遇到了阻碍.
        }

        // 先下两格 再 左一格
        next_row = row - 2;
        next_column = column - 1;
        if (inbounds(next_row, next_column)) // 为True证明没有出界
        {
            move_check(board, this_color, start_position, moves, next_row, next_column); // 如果为true 则遇到了阻碍.
        }

        // 先下两格 再 右一格
        next_row = row - 2;
        next_column = column + 1;
        if (inbounds(next_row, next_column))
        {
            move_check(board, this_color, start_position, moves, next_row, next_column); // 如果为true 则遇到了阻碍.
        }

        // 先下一格 再 左两格
        next_row = row - 1;
        next_column = column - 2;
        if (inbounds(next_row, next_column))
        {
            move_check(board, this_color, start_position, moves, next_row, next_column); // 如果为true 则遇到了阻碍.
        }

        // 先下一格 再 右两格
        next_row = row - 1;
        next_column = column + 2;
        if (inbounds(next_row, next_column))
        {
            move_check(board, this_color, start_position, moves, next_row, next_column); // 如果为true 则遇到了阻碍.
        }

        return moves;
        // 因为每一次调用move_check的时候 只要不遇到障碍, 或者遇到的障碍是敌方的都会把当前的位置更新到moves里. 最后返回就可以了
    }


    /*
    * 判断当前的位置是否还在界内 如果在 返回true 不在返回false*/
    public static boolean inbounds(int row, int col)
    {
        return row <= 8 && row >= 1 && col >= 1 && col <= 8; // 代表在界内
// 代表在界外
    }



    public static ArrayList<ChessMove> pawn_moves (ChessBoard board, int row, int column, ChessGame.TeamColor my_color, PieceType my_type) {
        // 得到当前pawn的起始位置根据Position
        ChessPosition start_position = new ChessPosition(row, column);
        // 创建局部临时moves来存储pawns的走法.
        ArrayList<ChessMove> moves = new ArrayList<>();

        // 声明起始的row;
        int start_row;
        // 声明pawn到底往下走还是往上走.
        int up_down;

        // 如果pawn的颜色是白色.
        if (my_color == ChessGame.TeamColor.WHITE) {
            // 则起始的row是第二行, 且是往上走的. 所以up_down 是1.
            start_row = 2;
            up_down = 1;
        } else {
            // 否则 pawn的颜色是黑色的话
            // 则起始的row在第七行, 且是往下走的. 所以up_down 是-1, 这样每次加up_down的时候行都会-1.
            start_row = 7;
            up_down = -1;
        }
        int next_row = start_row + up_down;  //得到nextRow 的行数.


        boolean empty = false;
        // 如果pawn走的下一行没有出界 并且走的下一个位置为空的话
        if (next_row >= 1 && next_row <= 8 && board.getPiece(new ChessPosition(next_row, column)) == null)
        {
            // 那么你就需要检查pawn下一步是否有走到最低线. 第一行或者第八行.
            // 如果走到了第一行或第八行, 则pawn可以升级成任意的ROOK 或 KNIGHT BISHOP 或QUEEN
            if (next_row == 1 || next_row == 8)
            {
                moves.add(new ChessMove(start_position, new ChessPosition(next_row, column), PieceType.BISHOP));
                moves.add(new ChessMove(start_position, new ChessPosition(next_row, column), PieceType.QUEEN));
                moves.add(new ChessMove(start_position, new ChessPosition(next_row, column), PieceType.KNIGHT));
                moves.add(new ChessMove(start_position, new ChessPosition(next_row, column), PieceType.ROOK));
            }
            // 否则的话,证明没有走到底线 就不需要升级. 但还是要把走过的路加进去.
            moves.add(new ChessMove(start_position, new ChessPosition(next_row, column), null));
            empty = true; // 证明下一步是空的.


            // 如果pawn的左右协方有东西可以吃, 则pawn可以去左右协防然后吃掉对方

            // right 斜方
            // 右斜方的话pawn的下一个column肯定会+1
            int next_column = column + 1;
            // 然后搞出来右斜方的piece. 因为next_row 前面已经加了
            ChessPiece next_piece = board.getPiece(new ChessPosition(next_row, next_column));

            // 接下来开始判断这个右斜方的piece是否为空.
            // 如果右斜方piece不为空且颜色跟当前颜色不等. 证明我们可以吃他.
            if (next_piece != null && my_color != next_piece.pieceColor)
            {
                // 且还是要判断我们到了右斜方是不是会到达底线
                if (next_row == 1 || next_row == 8)
                {
                    // 如果到达了底线 则需要把四种可能的升级类型都加进去, 且需要加这个路线进moves.
                    moves.add(new ChessMove(start_position, new ChessPosition(next_row, next_column), PieceType.BISHOP));
                    moves.add(new ChessMove(start_position, new ChessPosition(next_row, next_column), PieceType.ROOK));
                    moves.add(new ChessMove(start_position, new ChessPosition(next_row, next_column), PieceType.KNIGHT));
                    moves.add(new ChessMove(start_position, new ChessPosition(next_row, next_column), PieceType.QUEEN));
                }
                else
                {
                    // 否则如果没有到达底线. 而且可以吃它, 那么就直接也他它吃掉然后把它的location加进moves里
                    moves.add(new ChessMove(start_position, new ChessPosition(next_row, next_column), null));
                }
            }


            // 左斜方
            next_column -= 2; // 因为前面已经走了右斜方, 所以想回到左斜方需要将列-2;
            next_piece = board.getPiece(new ChessPosition(next_row, next_column));  // 得到了当前的左斜方piece

            // 如果左斜方piece不为空且颜色和当前颜色不等，证明我们可以吃他
            if (next_piece != null && my_color != next_piece.pieceColor)
            {
                // 且还要判断往右协方走会不会到底线.
                if (next_row == 1 || next_row == 8)
                {
                    // 如果到了最底线. 我们既可以吃了它, pawn也可以升级. 则我们可以把他们放到moves里.
                    moves.add(new ChessMove(start_position, new ChessPosition(next_row, next_column), PieceType.BISHOP));
                    moves.add(new ChessMove(start_position, new ChessPosition(next_row, next_column), PieceType.QUEEN));
                    moves.add(new ChessMove(start_position, new ChessPosition(next_row, next_column), PieceType.ROOK));
                    moves.add(new ChessMove(start_position, new ChessPosition(next_row, next_column), PieceType.KNIGHT));
                }
                // 否则没有达到底线, 但你任然可以吃他 因为对方颜色和你不一样. 但是你不需要升级.
                moves.add(new ChessMove(start_position, new ChessPosition(next_row, next_column),null));
            }


            // 如果最开始的开始, pawn就在它自己的start point呢?
            // 如果一开始就在start point 且前面没东西. empty == true 表示前面没东西
            if (next_row == row && empty)
            {
                next_row += up_down; // depends on the color.
                //如果pawn的下一行在bound 以内. 并且下一个piece为空.
                if (next_row >= 1 && next_row <= 8 && board.getPiece(new ChessPosition(next_row,column))== null)
                {
                    moves.add(new ChessMove(start_position, new ChessPosition(next_row, column), null));
                }
            }
            return moves;




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
