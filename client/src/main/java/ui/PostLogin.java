package ui;


import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;
import result.ListGameInformation;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.util.*;

public class PostLogin {
    private Scanner scanner;
    private ServerFacade serverFacade;
    private WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:8080");

    public PostLogin(Scanner scanner, ServerFacade server) throws ResponseException {
        this.scanner = new Scanner(System.in);
        this.serverFacade = server;
    }
    public void run() throws ResponseException, IOException, DataAccessException {
        System.out.println("Welcome to the chess game.");
        System.out.println("""
                create game
                list games
                join game
                observe game
                logout
                quit
                help
                """);
        while(true) {
            System.out.print("play a game >>> " +"\n");
            String command = scanner.nextLine();
            switch (command) {
                case "help" -> help();
                case "logout" -> logout();
                case "create game" -> createGame();
                case "list games" -> listGames();
                case "join game"-> joinGame();
                case "observe game" -> observeGame();
                case "quit" -> quit();
                default -> run();
            }
        }
    }
    public void help() throws ResponseException, IOException, DataAccessException {
        // display text informing user actions
        System.out.println("Choose on of the options to start:");
        run();
    }

    public void logout() throws ResponseException {
        try{
            serverFacade.logout();
            System.out.println("successfully logged out");
            PreLogin preLogin = new PreLogin(scanner, serverFacade);
            preLogin.run();
        } catch (ResponseException e){
            System.out.println(e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void createGame() throws ResponseException, IOException, DataAccessException {
        try {
            System.out.print("New game Name: ");
            String gameName = scanner.nextLine();
            serverFacade.createGame(gameName);
            System.out.print("successfully create game " + gameName);
        } catch (ResponseException e) {
            System.out.print(e.getMessage());
            help();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void listGames() throws ResponseException, IOException, DataAccessException {
        int gameIndex = 1;
        try {
            System.out.println("Games: ");
            ArrayList<ListGameInformation> gamelist = serverFacade.listGames();
            for (int i = 0; i < gamelist.size(); i++) {
                System.out.println(gameIndex + i + ". " + gamelist.get(i).gameName());
                int gameID = gamelist.get(i).gameID();
                System.out.println("Game ID: " + gameID);
                String blackUsername = gamelist.get(i).blackUsername();
                String whiteUsername = gamelist.get(i).whiteUsername();
                if (blackUsername != null) {
                    System.out.println("black player: " + blackUsername);
                }
                if (whiteUsername != null) {
                    System.out.println("white player: " + whiteUsername);
                }
                System.out.println("\n");
            }
            help();
        } catch(ResponseException | IOException e){
            System.out.println(e.getMessage());
            help();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            help();
        }
    }
    public void joinGame() throws ResponseException, IOException, DataAccessException {
        ArrayList<ListGameInformation> gamelist = serverFacade.listGames();
        System.out.println("enter the number of the game you want to play: ");
        int gameIndex = scanner.nextInt();
        int gameID = gamelist.get(gameIndex - 1).gameID();
        System.out.println("player color: ");
        String playerColor = scanner.next();
        if (playerColor == null) {
            observeGame();
        } else {
            try {
                serverFacade.joinGame(gameID, playerColor);
                System.out.println("successfully joined");
                //websocket connection
                if (playerColor.equalsIgnoreCase("White")){
                    webSocketFacade.joinPlayer(serverFacade.getAuthToken(),gameID, ChessGame.TeamColor.WHITE);}
                else{
                    webSocketFacade.joinPlayer(serverFacade.getAuthToken(),gameID, ChessGame.TeamColor.BLACK);
                }
                GamePlay gamePlay = new GamePlay(serverFacade,playerColor,gameID);
                GamePlay.playerColor = playerColor;
                gamePlay.run();

            } catch (ResponseException e) {
                System.out.println(e.getMessage());
                help();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void observeGame() throws ResponseException, IOException, DataAccessException {
        ArrayList<ListGameInformation> gameList = serverFacade.listGames();
        System.out.println("enter the number of the game you want to observe: ");
        int gameIndex = scanner.nextInt();
        int gameID = gameList.get(gameIndex - 1).gameID();
        webSocketFacade.joinObserver( serverFacade.getAuthToken(), gameID);
        GamePlay gamePlay = new GamePlay(serverFacade, null, gameID);
        gamePlay.help();

    }
    public void quit(){
        // exit the program
        System.exit(0);
        System.out.println("Exit");
    }

}


