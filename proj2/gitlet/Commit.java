package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import static gitlet.Utils.*;
/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private HashMap<String, String> files;
    private Commit parent;

    /* TODO: fill in the rest of this class. */
    Commit(){
        this.message = "";
        this.parent = null;
    }
    Commit(String msg, Commit parent){
        this.message = msg;
        this.parent = parent;
    }

    public static Commit fromFile(File file){
        return readObject(file, Commit.class);
    }

    public void commit(StageArea stage){
        // iter over all files in stage area
        // if a file in stage area not exists in parent commit, add to current commit
        // if a file in stage area exists in parent commit, check if it is changed
        // if it is changed, add it tot current commit and save a copy of the file
        // if not changed, add it tot current commit
        // finally, save current commit as HEAD, overwrite the older HEAD
    }

    public boolean contain(File file){
        return true;
    }
}
