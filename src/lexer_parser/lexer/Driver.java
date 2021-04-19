package lexer_parser.lexer;

import java.util.ArrayList;

public class Driver {

    public static void main(String[] args) {

        String fileName = args.length == 0 ? "testMultiplePlus.txt" : args[0];
        Lexer lexer = new Lexer(fileName);
        System.out.println(lexer.getBuffer());

        ArrayList<Token> tokens = lexer.getAllTokens();

        // Printing out the token types and values
        for (int i = 0; i < tokens.size(); i++) {
            System.out.print(tokens.get(i) + " | ");
        }
    }
}
