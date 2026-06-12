package client;


import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl extends ReplParent{

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
            if(client.getBreakLoop()){
                client.setBreakLoop(false);
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
}
