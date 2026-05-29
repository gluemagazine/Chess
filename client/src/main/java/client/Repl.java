package client;


import java.util.Scanner;

import ui.EscapeSequences.*;

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

            System.out.print("[" + client.getClientState() + "] >>> " );
            String input = scanner.nextLine();

            if(input.equals("quit")){
                client.setHasQuit(true);
                break;
            }

            if(input.equals("help")){
                System.out.println(help);
                continue;
            }

            client.processInput(input);
        }
    }
}
