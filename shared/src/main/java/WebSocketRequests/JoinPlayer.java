package WebSocketRequests;

import chess.ChessGame;

public record JoinPlayer(Integer gameID, ChessGame.TeamColor playerColor) {
}
