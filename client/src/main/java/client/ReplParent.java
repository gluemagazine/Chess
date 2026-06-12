package client;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public class ReplParent {
    protected void printAlternating(String toPrint){
        String[] split = toPrint.split("\n");
        for(var line : split){
            var pieces = line.split( "-");
            System.out.println(pieces[0] + SET_TEXT_COLOR_LIGHT_GREY + "-" + pieces[1] + RESET_TEXT_COLOR);
        }
    }
}
