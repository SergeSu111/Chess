package chess;

import java.time.temporal.Temporal;
import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor turn;
    public ChessGame() {
        this.turn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn()
    {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team)
    {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     * Returns true if the specified team’s King could be captured by an opposing piece.
     */
    public boolean isInCheck(TeamColor teamColor)
    {
        List<ChessPiece> not_myking = new ArrayList<>();  // 存储不是我家King的所有棋子.
        ChessPiece current_piece = board.getPiece(new ChessPosition(1, 1));  // 先得到棋盘上每一个棋子.
        ChessPiece King_piece = null;
        Map<Integer, Integer> row_col = new HashMap<>();
        Map<Integer, Integer> for_king = new HashMap<>();
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (current_piece.getPieceType() == ChessPiece.PieceType.KING)
                {
                        if (current_piece.getTeamColor() == teamColor)   // 因为我要确定这个King是我的King. 根据颜色确定
                        {
                            King_piece = current_piece;
                            for_king.put(i, j);
                        }
                        else  // 是一个敌人的king 可以把他放到not_King里
                        {
                            not_myking.add(current_piece);
                            row_col.put(i,j); // 既然

                        }
                }
                else   // means the current_piece is not king
                {
                    not_myking.add(current_piece);
                }
            }
        }

        for (int i = 0; i < not_myking.size(); i++)
        {
            ChessPiece enemyPiece = not_myking.get(i);
            if (enemyPiece.getTeamColor() != teamColor)  // if the current piece's color is different with King's
            {
                ArrayList<ChessMove> current_moves = (ArrayList<ChessMove>) not_myking.get(i).pieceMoves(board, new ChessPosition(,row_col.get(i))); // get all the moves this current can go
                for (int index = 0; index < current_moves.size(); index++)
                {
                    ChessPosition curr_end_position = current_moves.get(index).getEndPosition(); // get the end position of the current ChessMove
                    Set<Integer> row = for_king.keySet();
                    ChessPosition King_position = new ChessPosition(1,1);
                    for (int key: row)
                    {
                        King_position = new ChessPosition(key, for_king.get(key));  // 得到了国王的位置
                    }

                    if (curr_end_position == King_position) {
                        return true;
                    }
                }

            }
        }
        return false;


        if (King_piece == null)  // 如果King_piece 还是null 证明前面都没做 证明真的没有我要的King.
        {
            return false;
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard()
    {
        throw new RuntimeException("Not implemented");
    }

}
