package ui;

import chess.ChessBoard;
import websocket.WebSocketFacade;

import java.util.Scanner;

public class GamePlay
{
    private String playerColor;
    private Scanner scanner;
    private int gameID;
    private ServerFacade serverFacade;
    private WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:8080");
    public static ChessBoard board;

    public GamePlay(Scanner scanner, ServerFacade server, String playerColor, int gameID) throws ResponseException
    {
        this.scanner = new Scanner(System.in);
        this.serverFacade = server;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public void run()
    {
        System.out.println("Welcome to the game! Enjoy!");
        System.out.println("""
                Help
                Leave
                Make Move
                Resign
                HighLight Potential Moves
                Redraw Chess Board
                """);
        while(true)
        {
            System.out.print("Play a game >>>   ");
            String cd = scanner.nextLine();
            switch (cd)
            {
                case "Help" -> help();
                case "Redraw chess board" -> reDrawBoard();
                case "Leave this game" -> leave();
                case "Make Move" -> makeMoves();
                case "Resign" -> resign();
                case "highLight Potential Moves" -> highLightMoves();
                default -> run();
            }
        }
    }

    public void help()
    {
        System.out.println("Choose one of the options to start:");
        run();
    }

}