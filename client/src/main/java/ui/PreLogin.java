package ui;


import dataAccess.DataAccessException;

import java.io.IOException;
import java.security.Provider;
import java.util.Scanner;
public class PreLogin {
    private Scanner scanner;
    private ServerFacade serverFacade;
    public PreLogin(Scanner scanner, ServerFacade server){
        this.scanner = new Scanner(System.in);
        this.serverFacade = server;
    }
    public void run(){
        System.out.println("Welcome to the chess game.");
        System.out.println("""
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> < PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """);
        while(true) {
            System.out.print("start a game >>> ");
            String command = scanner.nextLine();

            switch (command) {
                case "help" -> help();
                case "register" -> register();
                case "login" -> login();
                case " quit" -> quit();
                default -> run();
            }
        }
    }
    public void help(){
        // display text informing user actions
        System.out.println("Choose on of the options to start:");
        run();
    }
    public void quit(){
        // exit the program
        System.exit(0);
        System.out.println("Exit");
    }
    public void login(){
        //prompt the user to input log in information
        System.out.println("username: ");
        String username = scanner.nextLine();
        System.out.println("password: ");
        String password = scanner.nextLine();
        // call server login API
        try{
            serverFacade.login(username,password);
            System.out.println(username + " is successfully logged in" + "\n");
            PostLogin postLogin = new PostLogin(scanner,serverFacade);
            postLogin.run();
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        // successful log in --> transition to PostLogin UI

    }
    public void register(){
        System.out.print("username: ");
        String username = scanner.nextLine();
        System.out.print("password: ");
        String password = scanner.nextLine();
        System.out.print("email: ");
        String email = scanner.nextLine();
        try{
            serverFacade.register(username, password, email);
            System.out.println(username + " is successfully registered in" + "\n");
            PostLogin postLogin = new PostLogin(scanner, serverFacade);
            postLogin.run();
        } catch (ResponseException e) {
            System.out.println("Registration Error");
        } catch (IOException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}