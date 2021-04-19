package lexer_parser.parser;

import lexer_parser.lexer.Token;
import lexer_parser.lexer.Lexer;

import java.util.ArrayList;

/**
 * This class determines if a given code file is syntactically correct and 
 * report either “valid program” or a particular error message
 * if the program is invalid.
 */
public class Parser {

    // Data member to keep track of where you are in the token list
    private int index = 0;

    // Data member to keep count of the lines to produce error messages for.
    private int line_ = 1; 
    
    private final IdTable idTable;
    private boolean flagOperator;
    private ArrayList<Token> tokenList;
    private Lexer lexer;

    /**
     * Constructor that creates a lexer and places the 
     * results of getAllTokens() into a data member- tokenList
     */
    public Parser(String fileName) {
        
        this.idTable = new IdTable();
        this.flagOperator = false;
        this.lexer = new Lexer(fileName);
        this.tokenList = lexer.getAllTokens();
    }
    
    /**
     * This method drives the process and parses an entire program
     * It calls parseAssignment() within a loop.
     */
    public void parseProgram() {
        
        while (!nextToken().getType().equals(Lexer.EOFTOKEN)) {
            
            putToken();
            
            if (!parseAssignment()) {
                System.out.println("Invalid Program");
                return;
            }
            
            // Parsing the program. Parse Assignment is true till now.
            
            // Program Parsed completely. Parse Assignment is true.
            // Valid Program.
            if (index >= tokenList.size()) {
                System.out.println("Valid Program 1");
                return;
            }
        }
        System.out.println("Valid Program 2");
    }


    /**
     * This method only parses a single statement.
     *
     * @return boolean true or false depending on the conditional statements below
     */
    public boolean parseAssignment() {
        
        Token token = nextToken();
        System.out.println("token = " + token);
        
        while (token.getType().equals(Lexer.NEWLINETOKEN)) {
            token = nextToken();
        }
        
        putToken();  // idx--
        
        if (token.getType().equals(Lexer.EOFTOKEN)) {
            // Reached end of file. All Ok.
            return true;
        }

        String id = parseId();

        // NOT IDENTIFIER
        if (id.equals("NOT_IDENTIFIER")) {
            if (flagOperator)
                System.out.println("Error:" + " " + "Expecting Identifier or an Operator in line" + " " + line_);
            else
                System.out.println("Error:" + " " + "Expecting Identifier in line" + " " + line_);
            return false;
        }

        // IDENTIFIER
        if (parseAssignOp() && parseExpression()) {
            idTable.add(id);
            return true;
        }

        System.out.println("Error:" + " " + "Expecting Assignment Operator in line" + " " + line_);
        return false;
    }


    /**
     * This method parses a single assignment operator.
     *
     * @return either boolean true or false depending on the conditional statements below
     */
    public boolean parseAssignOp() {
        if (!(tokenList.get(index).getType().equals(Lexer.ASSMTTOKEN))) {
            index++;
            return false;
        }
        index++;
        return true;
    }


    /**
     * This method parses a single identifier.
     *
     * @return value of the ID parser.Token type
     */
    public String parseId() {

        Token token = nextToken();

        if (!(token.getType().equals(Lexer.IDTOKEN))) {
            return "NOT_IDENTIFIER";
        }

        flagOperator = false;
        return token.getValue();
    }


    /**
     * This method parses the expression, i.e, the right-hand side of an assignment operator
     *
     * @return either boolean true or false depending on the conditional statements below
     */
    public boolean parseExpression() {
        Token t = nextToken();

        if ((t.getType().equals(Lexer.IDTOKEN)) || (t.getType().equals(Lexer.INTTOKEN)) || (t.getType().equals(Lexer.NEWLINETOKEN))) {
            if ((t.getType().equals(Lexer.IDTOKEN)) && (idTable.getAddress(t.getValue()) == -1)) {
                System.out.println("Error:" + " " + "Identifier" + " " + t.getValue() + " not defined in " + "line" + " " + line_);
                return false;
            } else if (t.getType().equals(Lexer.IDTOKEN) && (idTable.getAddress(t.getValue()) != -1)) {
                t = nextToken();

            }

            if (t.getType().equals(Lexer.NEWLINETOKEN)) {
                line_++;
                t = nextToken();
            }

            if (t.getType().equals(Lexer.INTTOKEN)) {
                t = nextToken();
            }

            if (checkOperators(t)) {
                while (checkOperators(t)) {
                    t = nextToken();
                    if ((t.getType().equals(Lexer.IDTOKEN)) && (idTable.getAddress(t.getValue()) == -1)) {
                        System.out.println("Error:" + " " + "Identifier" + " " + t.getValue() + " not defined in " + "line" + " " + line_);
                        return false;
                    } else if ((t.getType().equals(Lexer.IDTOKEN)) || (t.getType().equals(Lexer.INTTOKEN))) {
                        t = nextToken();
                        if (t.getType().equals(Lexer.NEWLINETOKEN)) {
                            line_++;
                        }
                    } else {
                        System.out.println("Error:" + " " + "Expecting Identifier or Integer in line" + " " + line_);
                        return false;
                    }
                }
                flagOperator = true;
                return true;
            } else {
                if (t.getType().equals(Lexer.NEWLINETOKEN)) {
                    line_++;
                    t = nextToken();
                    putToken();
                } else {
                    putToken();
                    flagOperator = true;
                    return true;
                }
            }
        } else {
            System.out.println("Error:" + " " + "Expecting Identifier or Integer in line" + " " + line_);
            return false;
        }
        return true;
    }


    /**
     * This method checks for multiple operators Tokens- PLUSTOKEN, SUBTOKEN, MULTTOKEN, DIVTOKEN
     *
     * @param
     * @return boolean true or false depending on the condition
     */
    public boolean checkOperators(Token t) {
        if (t.getType().equals(Lexer.PLUSTOKEN) || t.getType().equals(Lexer.SUBTOKEN) || t.getType().equals(Lexer.MULTTOKEN) || t.getType().equals(Lexer.DIVTOKEN)) {
            return true;
        }
        return false;
    }


    /**
     * This method gets the next token in the list and increments the index
     */
    public Token nextToken() {

        Token nextT = tokenList.get(index);
        index++;
        return nextT;
    }


    /**
     * This method decrements the index
     */
    public void putToken() {
        if (index != 0) {
            index--;
        }
    }


    /**
     * This method prints out the token list and id table.
     *
     * @return
     */
    @Override
    public String toString() {

        String str = "Parser {" +
                "Token List=" + "(";

        for (int i = 0; i < tokenList.size(); i++) {
            if (!(tokenList.get(i).getType().equals(Lexer.NEWLINETOKEN))) {
                str += tokenList.get(i) + "," + " ";

            }
        }
        if (str.endsWith(", ")) {
            str = str.substring(0, str.length() - 2);
        }
        str += ")" + ", ID Table=";
        str += idTable.toString() + "}";
        return str;
    }
    
} // Class ends here