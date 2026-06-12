package client;


import java.util.Scanner;

import static ui.EscapeSequences.*;

public class InGameRepl extends ReplParent{

    private final String help;
    private final InGameClient client;


    public InGameRepl(String prompt, InGameClient client){
        this.help = prompt;
        this.client = client;
        run();
    }

    private void run(){
        Scanner scanner = new Scanner(System.in);
        while (!client.getHasLeft()) {
            System.out.print(RESET_TEXT_COLOR);

            System.out.print("[IN_GAME] >>> ");
            String input = scanner.nextLine();

            if (input.equals("help")) {
                printAlternating(help);
                continue;
            }

            client.processInput(input);
        }
    }
}
