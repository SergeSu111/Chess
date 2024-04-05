package WebSocketRequests;

import chess.ChessMove;

public record MakeMove(Integer gameID, ChessMove Move) {
}
