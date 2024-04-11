package ui;

import request.LoginRequest;
import request.RegisterRequest;

import java.io.IOException;
import java.util.Scanner;

public class PreLogin
{
    private Scanner scanner;
    private ServerFacade serverFacade;
    public PreLogin(Scanner scanner, ServerFacade server)
    {
        this.scanner = new Scanner(System.in);
        this.serverFacade = server;
    }

    public void run()
    {
        System.out.println("Welcome to Chess Game!");
        System.out.println("""
                Register <USERNAME> <PASSWORD> <EMAIL> - Create Account.
                Login <USERNAME> <PASSWORD> - LOGIN TO Play.
                Quit - Leave The Game.
                Help - All Available Actions.
                """);
        while(true)
        {
            System.out.print("Start a game >>> ");
            String cd = scanner.nextLine();
            switch (cd)
            {
                case "Help" -> help();
                case "Register" -> register();
                case "Login" -> login();
                case "Quit" -> quit();
                default -> run();
            }
        }
    }
    public void help()
    {
        System.out.println("Choose one of the options: ");
        run();
    }

    public void quit()
    {
        System.exit(0);
        System.out.println("Exit");
    }

    public void login()
    {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        // call login API
        try
        {
            serverFacade.login(new LoginRequest(username, password));
            System.out.println(username + "is successfully logged in. \n");
            PostLogin postLogin = new PostLogin(scanner, serverFacade);
            postLogin.run();
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void register()
    {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();
        try
        {
            serverFacade.register(new RegisterRequest(username, password, email));
        } catch (ResponseException e) {
            System.out.println("Registration Error.");
        }
    }
}
