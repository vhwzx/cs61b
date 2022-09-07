package gitlet;

import java.util.Arrays;
import java.util.List;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                Repository.add(List.of(Arrays.copyOfRange(args, 1, args.length)));
                break;
            case "status":
                Repository.status();
                break;
            case "rm":
                break;
            default:
                throw new GitletException("no such a command");

        }
    }
}
