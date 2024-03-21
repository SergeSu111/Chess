package ui;

import model.GameData;
import request.CreateGameRequest;
import request.ListGameRequest;
import result.ListGameInformation;
import result.ListGameResult;

import java.util.*;

public class PostLogin
{
    private serverfacade server;
    private  Scanner scanner;
    private String serverUrl;
    private String userAuthToken;

    public PostLogin(Scanner scanner, serverfacade server)
    {
        this.server = server;
        this.scanner = new Scanner(System.in);
    }

    public void run() throws ResponseException
    {
        System.out.println("Welcome to the Chess Game.");
        System.out.println("""
                create game
                list games
                join games
                observe games
                logout
                quit
                help
                """);
        while(true)
        {
            System.out.print("play game.");
            String command = scanner.nextLine();
            switch (command)
            {
                case "help" -> help();
                break;
                case "logout" -> logout();
                break;
                case "create game" -> createGame();
                break;
                case "list games" -> listGames();
                break;
                case "join game or observe game" -> joinGame();
                break;
                case "quit" -> quit();
                break;
                default -> run();
            }
        }
    }

    public void help() throws ResponseException
    {
        System.out.println("Choose one of the actions to start:");
        run();
    }

    public void logout() throws ResponseException
    {
        try
        {
            server.logout(this.userAuthToken);
            System.out.println("Successfully logged out.");
            Prelogin preLogin = new Prelogin(scanner, server);
            preLogin.run();
        }
        catch (ResponseException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void createGame() throws ResponseException
    {
        try
        {
            System.out.println("Please tell me your new game's name: ");
            String gameName = scanner.nextLine();
            server.createGames(new CreateGameRequest(gameName));
            System.out.println("You successfully create a game" + gameName);
        }
        catch (ResponseException e)
        {
            System.out.println(e.getMessage());
            help();
        }
    }

    public void listGames () throws ResponseException
    {
        try
        {
            System.out.println("Here are all games: " );
            ListGameResult gameList = server.listGames(new ListGameRequest());
            System.out.print("\nCURRENT GAMES:\n");
            System.out.print("    Game Name | Game ID | White Player Username | Black Player Username\n");
            for (ListGameInformation gameInfo : gameList.games())
            {
                System.out.print("    " + gameInfo.gameName() + " | " + gameInfo.whiteUsername() + " | " + gameInfo.blackUsername() + "\n");
            }
            System.out.print("\n");
        }
        catch (ResponseException e)
        {
            System.out.println(e.getMessage());
            help();
        }
    }

    public void joinGame() throws ResponseException
    {
        ListGameResult gameList = server.listGames(new ListGameRequest());
        System.out.println("You can join game or observe game by not entering a player color.");
        System.out.println("Enter the number of the game ID that you want to play or observe: ");
        int gameID =
    }






}
