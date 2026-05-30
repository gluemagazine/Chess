package client;


import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {

    private final String help;
    private final Client client;


    public Repl(String prompt, Client client){
        this.help = prompt;
        this.client = client;
        run();
    }

    private void run(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            if(client.getHasQuit()){
                break;
            }
            System.out.print(RESET_TEXT_COLOR);


            System.out.print("[" + client.getClientState() + "] >>> " );
            String input = scanner.nextLine();

            if(input.equals("quit")){
                client.setHasQuit(true);
                break;
            }

            if(input.equals("help")){
                printAlternating(help);
                continue;
            }

            client.processInput(input);
        }
    }

    private void printAlternating(String toPrint){
        String[] split = toPrint.split("\n");
        for(var line : split){
            var pieces = line.split( "-");
            System.out.println(pieces[0] + SET_TEXT_COLOR_LIGHT_GREY + "-" + pieces[1] + RESET_TEXT_COLOR);
        }
    }
}
