package ui;

import java.util.Scanner;

public class PostLogin
{
    private serverfacade server;
    private  Scanner scanner;
    private String serverUrl;

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


}
