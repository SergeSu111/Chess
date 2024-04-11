package ui;

import chess.ChessBoard;
import websocket.WebSocketFacade;

import java.io.IOException;
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

    public void run() throws ResponseException {
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

    public void help() throws ResponseException {
        System.out.println("Choose one of the options to start:");
        run();
    }

    public void reDrawBoard() throws ResponseException
    {
        BOARD.drawGeneralBoard(board, playerColor);
        this.run();
    }

    public void leave() throws ResponseException, IOException {
        System.out.println("You are leaving this game\n");
        webSocketFacade.leave(serverFacade.authToken, gameID);
        PostLogin postLogin = new PostLogin(scanner, serverFacade);
        postLogin.help(); // 回到游戏前help的界面
    }

    // not finished
    public void makeMoves()
    {
        System.out.print("Tell me your start position.");
        int startPosition = scanner.nextInt();
        System.out.print("Tell me where you want to go.");
        int endPosition = scanner.nextInt();
        System.out.print("Do you want to promote any piece? If so, enter what piece you want to promote: ");
    }




}