package ui;

import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.*;

import java.util.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;


public class GoConsolor {
    private final serverfacade server = new serverfacade(8080);
    private boolean userAuthorized = false;
    static String userAuthToken;
    private boolean running = true;
    private boolean LoggedOutMenu = true;

    private boolean inGame = false; // 确定有没有在game里
    private boolean LoggedInMenu = true;

    public void run() {
        while (this.running) {
            if (this.LoggedOutMenu) {
                printLoggedOutMenu();
                this.LoggedOutMenu = false;
            }
            else if (this.LoggedInMenu) {
                printLoggedInMenu();
                this.LoggedInMenu = false;
            }

            ArrayList<String> Input = new ArrayList<>();
            try
            {
                Input = (ArrayList<String>) promptUserForInput();
            }
            catch (IOException ex)
            {
                System.out.print("An error occurred. Please try again");
            }

            parseCommands(Input);
        }
    }

    private void printLoggedOutMenu() {
        String printString = String.format("""
                
                %s OPTIONS:
                    register <username> <password> <email> - creates  account.
                    login <username> <password> - Login your account.
                    quit - Quit Game.
                    help - Show all available actions. 
                
                """, getUserAuthStatusAsString(this.userAuthorized, this.inGame));
        System.out.print(printString);
    }

    private void printLoggedInMenu() {
        String printString = String.format("""
            
            %s OPTIONS:
                list - Lists all games
                create <name> Create a game
                join <ID> <WHITE|BLACK> - Joins a game.
                observe <ID> - Observe a game.
                logout - Logout your account.
                quit - Quit Game.
                help - Show all available actions.
            
            """, getUserAuthStatusAsString(this.userAuthorized, this.inGame));
        System.out.print(printString);
    }

    private void GameUIMenu()
    {
        String printString = String.format("""
                %s OPTIONS:
                    help - Show all available actions in Game.
                    Redraw Chess Board - Redraw the board by the request.
                    Leave - Leave the Game.
                    Make Move - Tell me where you want to go.
                    Resign - Surrender the game.
                    Highlight Legal Moves - Tell me what potential moves I can go.
                
                """, getUserAuthStatusAsString(this.userAuthorized, this.inGame));
            System.out.print(printString);
    }
    private Collection<String> promptUserForInput() throws IOException
    {
        System.out.print(getPrompt());
        return getUserInput();
    }

    private String getPrompt()
    {
        return String.format("[%s] >>> ", getUserAuthStatusAsString(this.userAuthorized, this.inGame));
    }

    private String getUserAuthStatusAsString(boolean userAuthorized, boolean inGame)
    {
        if (userAuthorized)
        {
            return "LOGGED_IN";
        }
        else if (!userAuthorized)
        {
            return "LOGGED_OUT";
        }
        else if (userAuthorized && inGame)
        {
            return "IN GAME";
        }
        return "Not found";
    }

    private static Collection<String> getUserInput() throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return new ArrayList<>(List.of(reader.readLine().split(" ")));
    }

    private void parseCommands(ArrayList<String> userInput) {
        String unrecognizedCommandString = "Cannot figure out the command. Please typing help to list available commands.\n";
        if (!userInput.isEmpty())
        {
            ArrayList<String> validCommands = new ArrayList<>(Arrays.asList("register", "login", "list", "create",
                    "join", "observe", "logout", "quit", "help"));
            String firstCommand = userInput.getFirst().toLowerCase();
            if (firstCommand.isEmpty())
            {
                return;
            }
            else if (!validCommands.contains(firstCommand))
            {
                System.out.print(unrecognizedCommandString);
                return;
            }
            ArrayList<String> userArguments;
            userArguments = userInput;
            userArguments.removeFirst();
            ArrayList<String> validate;
            // Unauthorized and authorized options
            try {
                System.out.println("---------------------------------------------------------------------------------------------");
                System.out.println(firstCommand);
                boolean invalidInput = true;
                if (!userAuthorized)
                {
                    switch (firstCommand) {
                        case "register" -> {
                            System.out.print("Am I in command ");
                            validate = new ArrayList<>(Arrays.asList("str", "str", "str"));
                            register(userArguments);

                        }
                        case "login" -> {
                            System.out.print("I am login");
//                            validate = new ArrayList<>(Arrays.asList("str", "str"));

                            login(userArguments);

                        }
                    }
                }
                else
                {
                    switch (firstCommand) {
                        case "list" -> {
                            if (userArguments.isEmpty())
                            {
                                list();
                            }
                            else
                            {
                                invalidInput = false;
                            }
                        }
                        case "create" -> {
                            validate = new ArrayList<>(List.of("str"));
                            create(userArguments);

                        }
                        case "join" -> {
                            validate = new ArrayList<>(Arrays.asList("int", "str"));
                            if (isValidInput(userArguments, validate))
                            {
                                join(userArguments);
                            }
                            else
                            {
                                invalidInput = false;
                            }
                        }
                        case "observe" -> {
                            validate = new ArrayList<>(List.of("int"));
                            if (isValidInput(userArguments, validate))
                            {
                                observe(userArguments);
                            }
                            else
                            {
                                invalidInput = false;
                            }
                        }
                        case "logout" -> {
                            if (userArguments.isEmpty())
                            {
                                logout();
                            }
                            else
                            {
                                invalidInput = false;
                            }
                        }
                    }
                }
                // Always-available options
                switch (firstCommand) {
                    case "quit" ->
                    {
                        if (userArguments.isEmpty())
                        {
                            quit();
                        }
                        else
                        {
                            invalidInput = false;
                        }
                    }
                    case "help" ->
                    {
                        if (userArguments.isEmpty())
                        {
                            help();
                        }
                        else
                        {
                            invalidInput = false;
                        }
                    }
                }
//                if (invalidInput)
//                {
//                    System.out.print("Invalid command input. Go back to help.\n");
//                }
            }
            catch (ResponseException ex)
            {
                System.out.print("An error occurred while communicating with the server: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.print(unrecognizedCommandString);
        }
    }

    private boolean isValidInput(ArrayList<String> userInput, ArrayList<String> validTypes) {
        if (userInput.size() != validTypes.size())
        {
            return false;
        }
        boolean isValidInput = true;
        for (int i = 0; i < validTypes.size(); i++)
        {
            String input = userInput.get(i);
            String type = validTypes.get(i);
            switch(type) {
                case "str" -> isValidInput = isValidInput && !isNumeric(input) && !input.isEmpty();
                case "int" -> isValidInput = isValidInput && isNumeric(input);
            }
        }
        return isValidInput;
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.valueOf(str);
            return true;
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
    }

    private void register(ArrayList<String> userArgs) throws ResponseException {
        System.out.print("Am I in register?");
        RegisterResult rResponse = server.register( new RegisterRequest(userArgs.get(0), userArgs.get(1), userArgs.get(2)));
        System.out.print("Registered user " + rResponse.username() + ".\n");
        login(new ArrayList<>(Arrays.asList(userArgs.get(0), userArgs.get(1))));
    }

    private void login(ArrayList<String> userArgs) throws ResponseException {
        LoginResult lResponse = server.login(
                new LoginRequest(userArgs.get(0), userArgs.get(1)));
        setAuthorization(lResponse.authToken());
        System.out.print("Logged in user " + lResponse.username() + ".\n");
        //printLoggedInMenu();
    }

    private void list() throws ResponseException {
        ListGameResult response = server.listGames(this.userAuthToken);
        System.out.print("\nCURRENT GAMES:\n");
        System.out.print("    Game Name | Game ID | White Player Username | Black Player Username\n");
        for(ListGameInformation gameInfo: response.games()) {
            System.out.print("    " + gameInfo.gameName() + " | " +
                    gameInfo.gameID() + " | " +
                    gameInfo.whiteUsername() + " | " +
                    gameInfo.blackUsername() + "\n");
        }
        System.out.print("\n");
    }

    private void create(ArrayList<String> userArgs) throws ResponseException {
        CreateGameResult response = server.createGame(new CreateGameRequest(userArgs.getFirst()), this.userAuthToken);
        System.out.print("New game \"" + userArgs.getFirst() + "\" created with ID: " + response.gameID() + "\n");
    }

    private void join(ArrayList<String> userArgs) throws ResponseException {
        String color;
        if (userArgs.get(1) != null)
        {
            color = userArgs.get(1).toUpperCase();
        }
        else
        {
            color = null;
        }
        int gameID = Integer.parseInt(userArgs.get(0));
        server.joinGame(new JoinGameRequest(color, gameID), this.userAuthToken);
        // Default board printing for phase 5
        //     Actual implementation will be done via websockets in phase 6
        System.out.print("GameID: " + gameID + "\n");
        board.drawWholeBoard(board.getDefaultBoard());
    }

    private void observe(ArrayList<String> userArgs) throws ResponseException {
        join(new ArrayList<>(Arrays.asList(userArgs.getFirst(), null)));
    }

    private void logout() throws ResponseException {
        server.logout(this.userAuthToken);
        setAuthorization(null);
        System.out.print("Logged out.\n");
    }

    private void quit() throws ResponseException
    {
        if (this.userAuthorized)
        {
            logout();
        }
        System.out.print("Quitting...\n\n");
        this.running = false;
    }

    private void help() {
        if (!this.userAuthorized)
        {
            printLoggedOutMenu();
        }
        else
        {
            printLoggedInMenu();
        }
    }

    private void setAuthorization(String authToken)
    {
        this.userAuthToken = authToken;
        this.userAuthorized = (authToken != null);
        if (this.userAuthorized) { this.LoggedInMenu = true;
        } else { this.LoggedOutMenu = true; }
    }
}