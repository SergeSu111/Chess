package ui;

import chess.*;
import dataAccess.DataAccessException;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class GamePlay
{
    public static  String playerColor;
    private Scanner scanner;
    private int gameID;
    private ServerFacade serverFacade;
    private WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:8080");
    public static ChessBoard board;
    private ChessGame chessGame = new ChessGame();

    public GamePlay(ServerFacade server, String playerColor, int gameID) throws ResponseException
    {
        this.scanner = new Scanner(System.in);
        this.serverFacade = server;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public void run() throws ResponseException, IOException, DataAccessException {
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
            System.out.println("Play a game >>>   ");
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

    public void help() throws ResponseException, IOException, DataAccessException {
        System.out.println("Choose one of the options to start:");
        run();
    }

    public void reDrawBoard() throws ResponseException, IOException, DataAccessException {
        BOARD.drawGeneralBoard(board, playerColor);
        this.run();
    }

    public void leave() throws ResponseException, IOException, DataAccessException {
        System.out.println("You are leaving this game\n");
        webSocketFacade.leave(serverFacade.authToken, gameID);
        PostLogin postLogin = new PostLogin(scanner, serverFacade);
        postLogin.help(); // 回到游戏前help的界面
    }

    // not finished
    public void makeMoves() throws ResponseException, IOException, DataAccessException {
       System.out.println("Please enter startPosition: [row][col], endPosition: [row][col]");
       String[] answer = scanner.nextLine().toLowerCase().split(" ");
       String[] startPosition = answer[0].split("");
       // 得到起始位置
       ChessPosition start = new ChessPosition(parseInt(startPosition[0]), parseInt(startPosition[1]));
       String[] endPosition = answer[1].toLowerCase().split("");
       ChessPosition end = new ChessPosition(parseInt(endPosition[0]), parseInt(endPosition[1]));
       ChessMove move = new ChessMove(start, end, null); // 放入chessmove中
        try
        {
            chessGame.makeMove(move); // 让游戏里的棋子移动
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage());
        }
        webSocketFacade.makeMove(serverFacade.authToken, gameID, move); // 放入webscoket来更新给别人
        reDrawBoard(); //再重新画board
    }

    public void resign() throws ResponseException, IOException, DataAccessException {
        System.out.println("Are you sure you want to resign game? : YES/NO");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("YES"))
        {
            try
            {
                webSocketFacade.resign(serverFacade.getAuthToken(), gameID); // 将投降告诉别人
            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Game Over. You lose this game.");
            PostLogin postLogin = new PostLogin(scanner, serverFacade);
            postLogin.help(); // 投降完回到postLogin界面
        }
        else
        {
           System.out.println("");
        }
    }

    // need figure out later
    public void highLightMoves() {
        System.out.println("Enter highlight position: [row][col]");
        String[] answer = scanner.nextLine().toLowerCase().split(" ");
        String[] start = answer[0].split("");
        ChessPosition startPosition = new ChessPosition(parseInt(start[0]), parseInt(start[1]));
        Collection<ChessMove> potentialMoves = chessGame.validMoves(startPosition);
        BOARD.highLightMoves(startPosition, board, playerColor, potentialMoves);


    }


}