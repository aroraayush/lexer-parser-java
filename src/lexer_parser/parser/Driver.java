package lexer_parser.parser;

import lexer_parser.lexer.Lexer;

public class Driver {

    public static void main(String[] args) {
        String[] fileArray = new String[]{
                "input/test.txt",
                "input/testExpectingId2.txt",
                "input/testExpectingAssignOp.txt",
                "input/testExpectingIdOrInt2.txt",
                "input/testMultiplePlus.txt",
                "input/testWhiteSpace.txt",
                "input/testIdentifierNotDefined.txt",
                "input/testExtraCredit.txt"};

        for (String file : fileArray) {
//            System.out.println(file);
//            Lexer lexer = new Lexer(file);
//            System.out.println(lexer.getBuffer());

            Parser parser = new Parser(file);
            parser.parseProgram();
            System.out.println(parser);
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        }

        String fileName = "testExpectingAssignOp.txt";
        Parser parse = new Parser(fileName);

    }
}
