package model;

import chess.ChessGame;

public record GameData(Integer gameID, String whiteUsername, String blackUsername, String gameName, String game) {
    public boolean available_color (String requested_color) throws IllegalAccessException {
        if (requested_color.equalsIgnoreCase("WHITE")) {
            return (this.whiteUsername == null);
        } else if (requested_color.equalsIgnoreCase("BLACK"))
        {
            return (this.blackUsername == null);
        }
        else
        {
            throw new IllegalAccessException("Error: invalid parameter");
        }

    }

}
