import chess.*;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var serverUrl = "http://localhost:8080"; // 服务器url
        if (args.length == 1)
        {
            serverUrl = args[0]; // 如果命令行有其他的serverurl 那么就覆盖默认的
        }

        new Repl(serverUrl).run(); //将server url给Repl类来运行一个交互式命令行界面，和传进去的服务器进行交互.
        //System.out.println("♕ 240 Chess Client: " + piece);
    }
}