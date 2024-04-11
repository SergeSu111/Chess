package ui;

import chess.ChessGame;

import request.CreateGameRequest;
import request.JoinGameRequest;

import result.ListGameResult;

import websocket.NotificationHandler;
import websocket.webSocketFacade;

import java.io.IOException;
import java.util.*;

public class PostLogin
{
    private Scanner scanner;
    private ServerFacade serverFacade;
    private String Username;

    private NotificationHandler notificationHandler;

    private final webSocketFacade webSocketFacade = new webSocketFacade("http://localhost:8080");
    public PostLogin(Scanner scanner, ServerFacade server) throws ResponseException {
        this.scanner = new Scanner(System.in);
        this.serverFacade = server;
    }

    public void run() throws ResponseException, IOException {
        System.out.println("Welcome to your account.");
        System.out.println("""
                create game
                list games
                join game
                observe game
                logout
                quit
                help
                """);
        while(true)
        {
            System.out.print("play a game >>> " +"\n");
            String command = scanner.nextLine();
            switch (command)
            {
                case "help" -> help();
                case "logout" -> logout();
                case "create game" -> createGame();
                case "list game" -> joinGame();
                case "join game" -> joinGame();
                case "observe game" -> observeGame();
                case "quit" -> quit();
                default -> run();
            }
        }
    }
    public void help() throws ResponseException, IOException {
        System.out.println("Choose one of the options to start:");
        run();
    }

    public void logout() throws ResponseException
    {
        try
        {
            serverFacade.logout();
            System.out.println("Successfully logged out.");
            PreLogin preLogin = new PreLogin(scanner, serverFacade);
            preLogin.run();
        }
        catch (ResponseException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void createGame() throws ResponseException, IOException {
        try
        {
            System.out.println("New Game Name: ");
            String gameName = scanner.nextLine();
            serverFacade.createGame(new CreateGameRequest(gameName));
            System.out.println("Successfully create game " + gameName);
        }
        catch (ResponseException e)
        {
            System.out.println(e.getMessage());
            help();
        }
    }

    public void listGames() throws ResponseException, IOException {
        int gameIndex = 1;
        try
        {
            System.out.println("Games: ");
            ListGameResult gameList = serverFacade.listGames();
//            ListGameResult games = new ArrayList<>(Collections.singleton(gameList));
            for (int i = 0; i < gameList.games().size(); i++)
            {
                System.out.println(gameIndex + i + ". " + gameList.games().get(i).gameName());
                int gameID = gameList.games().get(i).gameID();
                System.out.println("Game ID: " + gameID);
                String blackUsername = gameList.games().get(i).blackUsername();
                String whiteUsername = gameList.games().get(i).whiteUsername();
                if (blackUsername != null)
                {
                    System.out.println("black player: " + blackUsername);
                }
                if (whiteUsername != null)
                {
                    System.out.println("White player: " + whiteUsername);
                }
                System.out.println("\n");
            }
        }
        catch(ResponseException e)
        {
            System.out.println(e.getMessage());
            help();
        }
    }
    public void joinGame() throws ResponseException, IOException {
        ListGameResult gameList = serverFacade.listGames();
        System.out.println("Enter the number of the game you want to play: ");
        int gameIndex = scanner.nextInt();
        int gameID = gameList.games().get(gameIndex - 1).gameID();
        System.out.println("Player color: ");
        String playerColor = scanner.next();
        if (playerColor == null)
        {
            observeGame();
        }
        else
        {
            try
            {
                serverFacade.joinGame(new JoinGameRequest(playerColor, gameID));
                System.out.println("successfully joined");

                // websocket
                if (playerColor.equalsIgnoreCase("White"))
                {
                    webSocketFacade.joinPlayer(serverFacade.authToken, gameID, ChessGame.TeamColor.WHITE);
//                    gameUI.color =
                }
                else
                {
                    webSocketFacade.joinPlayer(serverFacade.authToken, gameID, ChessGame.TeamColor.BLACK);
                }
//                GamePlay gamePlay = new GamePlay(scanner, serverFacade, playerColor, gameID);
//                gamePlay.help();

            }
            catch (ResponseException e)
            {
                System.out.println(e.getMessage());
                help();
            }
        }
    }

    public void observeGame() throws ResponseException, IOException {
        ListGameResult gameList = serverFacade.listGames();
        System.out.println("Enter the number of the game you want to observe: ");
        int gameIndex = scanner.nextInt();
        int gameID = gameList.games().get(gameIndex - 1).gameID();
        webSocketFacade.joinObserver(serverFacade.authToken, gameID);
    }
    public void quit()
    {
        System.exit(0);
        System.out.println("Exit");
    }


}
