package gitlet;
import gitlet.Repository;

import static gitlet.Utils.*;
/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if(args.length == 0){
            exit_message("Please enter a command.");
        }
        String firstArg = args[0];
        Repository repo = new Repository();
        if(!"init".equals(firstArg) && !repo.GITLET_DIR.exists()){
            exit_message("Not in an initialized Gitlet directory.");
        }
        switch(firstArg) {
            case "init":
                repo.init();
                break;
            case "add":
                repo.add(args[1]);
                break;
            case "commit":
                repo.commit(args[1]);
                break;
            case "rm":
                repo.rm(args[1]);
                break;
            case "log":
                repo.log();
                break;
            case "global_log":
            case "global-log":
                repo.global_log();
                break;
            case "find":
                repo.find(args[1]);
                break;
            case "status":
                repo.status();
                break;
            case "branch":
                if(args.length == 1)
                    repo.list_branches();
                else if(args.length == 2)
                    repo.branch(args[1]);
                break;
            case "rm_branch":
            case "rm-branch":
                repo.rm_branch(args[1]);
                break;
            case "reset":
                repo.reset(args[1]);
                break;
            case "merge":
                repo.merge(args[1]);
                break;
            case "switch":
                repo.switch_branch(args[1]);
                break;
            case "checkout":
                if(args[1].equals("--")){
                    check_parms(args, 3);
                    repo.checkout(args[2], 0);
                }
                else if (args.length == 2) {
                    repo.checkout(args[1], 1);
                }
                else {
                    if(args[2].equals("--"))
                        repo.checkout(shortID(args[1]), args[3]);
                    else{
                        System.out.println("Incorrect operands.");
                    }

                }
                break;
            default:
                exit_message("No command with that name exists.");
                break;
        }
    }
}
