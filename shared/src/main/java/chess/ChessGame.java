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
     *  Takes as input a position on the chessboard and returns all moves the piece there can legally make. If there is no piece at that location, this method returns null.
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition)
    {
        // 创建return的moves
        ChessPiece current_piece = board.getPiece(startPosition);
        if (current_piece == null)
        {
            return null;
        }
        else
        {
            ArrayList<ChessMove> curr_moves = new ArrayList<ChessMove>();
            Collection<ChessMove> moves = current_piece.pieceMoves(this.board, startPosition);
            ChessPiece removed = null; // for update

            // loop curr_moves里的每一个小步
            for (ChessMove move : moves)
            {
                // 将当前start position的piece 从board 里删掉
                removed = this.board.getPiece(move.getEndPosition());
                this.board.removePiece(startPosition);
                // 把他加到endposition离去
                this.board.addPiece(move.getEndPosition(), current_piece);
                // call is_In_Check
                if (!isInCheck(current_piece.getTeamColor()))  // 如果为false 则不会被check 则加到moves 里
                {
                    curr_moves.add(move);
                }
                this.board.addPiece(startPosition, current_piece);
                this.board.addPiece(move.getEndPosition(), removed);

                // 把board 改回baseboard.
            }
            return curr_moves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     * Receives a given move and executes it, provided it is a legal move. If the move is illegal,
     * it throws an InvalidMoveException. A move is illegal if the chess piece cannot move there,
     * if the move leaves the team’s king in danger, or if it’s not the corresponding team's turn.
     */
    // 此函数要做的是传进来一个move 看看这个move里有哪些步是合法能走的. 然后就走过去 否则就抛出异常
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ArrayList<ChessMove> result_move = (ArrayList<ChessMove>) validMoves(move.getStartPosition());  // 得到了这个move的piece能走的合法的所有走法
        if (!result_move.contains(move)) {
            throw new InvalidMoveException("It is illegal.");
        }
        ChessPiece temp = this.board.getPiece(move.getStartPosition());
        if (!temp.getTeamColor().equals(this.turn)) {
            throw new InvalidMoveException("It is illegal.");
        }

        this.board.removePiece(move.getStartPosition());
        ChessPiece removed_piece = this.board.getPiece(move.getEndPosition());

        ChessPiece promotion_piece = null;
        if (move.getPromotionPiece() != null) {
            promotion_piece = new ChessPiece(temp.getTeamColor(), move.getPromotionPiece());
            this.board.addPiece(move.getEndPosition(), promotion_piece);
        }
        else
        {
            this.board.addPiece(move.getEndPosition(), temp);
        }

        if (isInCheck(temp.getTeamColor())) {
            this.board.addPiece(move.getStartPosition(), temp);
            this.board.addPiece(move.getEndPosition(), removed_piece);
            throw new InvalidMoveException("Its illegal");
        }
        if (this.turn == TeamColor.WHITE) {
            this.turn = TeamColor.BLACK;
        } else {
            this.turn = TeamColor.WHITE;
        }
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
        ChessPiece King_piece = null;
        ArrayList<ChessPiece> enemy_piece = new ArrayList<>();
        ArrayList<ChessPosition> enemy_position = new ArrayList<>();
        ChessPosition king_position  = null;

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                ChessPiece current_piece = board.getPiece(new ChessPosition(i + 1, j + 1));
                if (current_piece != null)
                {
                    if (current_piece.getPieceType() == ChessPiece.PieceType.KING)
                    {
                        if (current_piece.getTeamColor() == teamColor)   // 因为我要确定这个King是我的King. 根据颜色确定
                        {
                            King_piece = current_piece;
                            king_position = new ChessPosition(i + 1, j + 1); // 得到king的position

                        }
                        else  // 是一个敌人的king 可以把他放到not_King里
                        {
                            enemy_piece.add(current_piece);
                            enemy_position.add(new ChessPosition(i + 1, j + 1));

                        }
                    }
                    else   // means the current_piece is not king
                    {
                        if (current_piece.getTeamColor() != teamColor)
                        {
                            enemy_piece.add(current_piece);
                            enemy_position.add(new ChessPosition(i + 1, j + 1));
                        }

                    }
                }

            }
        }
        if (King_piece == null)  // 如果King_piece 还是null 证明前面都没做 证明真的没有我要的King.
        {
            return false;
        }
        for (int i = 0; i <enemy_piece.size(); i++)
        {

            ArrayList<ChessMove> current_moves = (ArrayList<ChessMove>) enemy_piece.get(i).pieceMoves(board, enemy_position.get(i)); // get all the moves this current can go
            for (int index = 0; index < current_moves.size(); index++)
            {
                ChessPosition curr_end_position = current_moves.get(index).getEndPosition(); // get the end position of the current ChessMove
                if (king_position.getRow() == curr_end_position.getRow() && king_position.getColumn() == curr_end_position.getColumn())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */

    public boolean not_valid_move(TeamColor teamColor)
    {
        ArrayList<ChessMove> result = new ArrayList<>();
        for(int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (this.board.getPiece(new ChessPosition(i+1, j+1)) != null)
                {
                    if (this.board.getPiece(new ChessPosition(i+1, j+1)).getTeamColor() == teamColor)
                    {
                        result.addAll(validMoves(new ChessPosition(i + 1, j +1)));
                    }
                }

            }
        }
        if (result.isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isInCheckmate(TeamColor teamColor)
    {

        if (isInCheck(teamColor) && not_valid_move(teamColor))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor)
    {
        if (!isInCheck(teamColor) && not_valid_move(teamColor))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board)
    {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard()
    {
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", turn=" + turn +
                '}' + "/n";
    }
}
