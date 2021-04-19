package lexer_parser.lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to build an array of Tokens from an input file
 * @see Token
 */
public class Lexer {

    // Integer
    public static final String INTTOKEN = "INT";
    // Identifier
    public static final String IDTOKEN = "ID";

    // Assignment (=)
    public static final String ASSMTTOKEN = "ASSMT";

    // Operations
    public static final String PLUSTOKEN = "PLUS";
    public static final String SUBTOKEN = "SUB";
    public static final String MULTTOKEN = "MULT";
    public static final String DIVTOKEN = "DIV";

    public static final String UNKNTOKEN = "UNKNOWN";
    public static final String EOFTOKEN = "EOF";

    public static final String NEWLINETOKEN = "NEWLINE";


    private String buffer; // Input Stream

    // keep track of where you are in the buffer stream
    private int idx = 0;

    /**
     * Reading the file for lexical analysis
     * Tokenize each work of each line and categorize
     * them based on pre-determined token types
     *
     * @param fileName the file we open
     */
    public Lexer(String fileName) {
        getInput(fileName);
    }

    /**
     * Reads given file into the data member buffer
     *
     * @param fileName name of file to parse
     */
    private void getInput(String fileName) {
        try {
            System.out.println("fileName = " + fileName);
            Path filePath = Paths.get(fileName);
            byte[] allBytes = Files.readAllBytes(filePath);
            buffer = new String(allBytes);
        } catch (IOException e) {
            System.out.println("You did not enter a valid file name in the run arguments.");
            System.out.println("Please enter a string to be parsed:");
            Scanner scanner = new Scanner(System.in);
            buffer = scanner.nextLine();
        }
    }

    /**
     * Method that returns all the token in the file
     *
     * @return ArrayList of parser.Token
     */
    public ArrayList<Token> getAllTokens() {

        ArrayList<Token> tokens = new ArrayList<>();
        while (idx < buffer.length()) {
            Token token = getNextToken();
            if (token != null) {
                tokens.add(token);
            }
        }

        // Condition to add EOF token.
        // EOF should be added to last for each add
//        if (idx == buffer.length()) {
        tokens.add(new Token(EOFTOKEN, "EOF"));
//        }
        return tokens;
    }


    /**
     * Method that checks for the token type
     * and returns a token type and token value
     * @return Token Represents an individual token
     */
    private Token getNextToken() {
        Token token = null;
        char[] bufferArr = buffer.toCharArray();

        while (idx < bufferArr.length) {

            char ch = bufferArr[idx];

            // If starts with letter, it is an identifier
            if (Character.isLetter(ch)) {
                token = getIdentifier();
                break;
            }
            else if (Character.isDigit(ch)) {
                token = getInteger();
                break;
            }
            else if (ch == '=') {
                token = new Token(ASSMTTOKEN, "=");
                idx++;
                break;
            }
            else if (ch == '+') {
                token = new Token(PLUSTOKEN, "+");
                idx++;
                break;
            }
            else if (ch == '-') {
                token = new Token(SUBTOKEN, "-");
                idx++;
                break;
            }
            else if (ch == '/') {
                token = new Token(DIVTOKEN, "/");
                idx++;
                break;
            }
            else if (ch == '*') {
                token = new Token(MULTTOKEN, "*");
                idx++;
                break;
            }
            else if (ch == '\n') {
                token = new Token(NEWLINETOKEN, "\n\\n");
                idx++;
                break;
            }
            else if (Character.isWhitespace(ch)) {
                idx++;
                break;
            }
            else {
                token = new Token(UNKNTOKEN, Character.toString(ch));
                idx++;
                break;
            }
        }
        return token;
    }


    /**
     * Method that identifies integer token type, extracts it,
     * and returns it to the getNextToken()
     *
     * @return parser.Token type- integer
     */
    private Token getInteger() {
        StringBuilder builder = new StringBuilder();

        while (idx < buffer.length()) {

            char ch = buffer.charAt(idx);

            if (Character.isDigit(ch)) {
                builder.append(ch);
                idx++;
            } else {
                // builder.length() > 0 && ch == ' '
                break;
            }
        }

        return new Token(INTTOKEN, builder.toString().trim());
    }


    /**
     * Method that identifies an identifier token type,
     * extracts it and returns it.
     *
     * @return Token type - Identifier
     */
    private Token getIdentifier() {

        StringBuilder builder = new StringBuilder();

        while (idx < buffer.length()) {

            char ch = buffer.charAt(idx);

            if (builder.length() == 0 && (Character.isLetter(ch) || ch == ' ')) {
                builder.append(ch);
                idx++;
            }
            else if ((builder.length() > 0) && (Character.isLetter(ch) || Character.isDigit(ch))) {
                builder.append(ch);
                idx++;
            }
            else {
                // (builder.length() > 0) && ch == ' '
                break;
            }
        }
        return new Token(IDTOKEN, builder.toString().trim());
    }

    public String getBuffer() {
        return buffer;
    }
}