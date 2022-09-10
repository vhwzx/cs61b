package gitlet;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    // private final HashMap<String, String> stringStringHashMap;
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String author;
    private String date;
    private String message;
    private String branch_name;
    private List<String> parents;

    private String commitID;
    public final HashMap<String, String> files;


    /* TODO: fill in the rest of this class. */
    public Commit(String message, String branch_name, List<String> parents, StageArea STAEG_AREA){
        this.author = "";
        this.date = getDate();
        this.message = message;
        this.branch_name = branch_name;
        this.parents = parents;
        this.commitID = sha1(this.date, this.message);

        // merge parents
        if(parents.size() > 1){
            this.files = STAEG_AREA.files;
//            for(String parent : parents){
//                Commit c = readCommit(parent);
//                for(String filepath : c.files.keySet()){
//                    // file is in both parent, check for merge conflict
//                    if(this.files.containsKey(filepath)){
//                        // check sha1 hash same
//                        if(!this.files.get(filepath).equals(c.files.get(filepath)))
//                            throw error("merge conflict");
//                    }
//                    else {
//                        this.files.put(filepath, c.files.get(filepath));
//                    }
//
//                }
//            }
        }
        // normal commit
        else if (parents.size() == 1){
            Commit c = readCommit(parents.get(0));
            this.files = c.files;
            // add files in stage area
            for(String filepath : STAEG_AREA.files.keySet()){
                this.files.put(filepath, STAEG_AREA.files.get(filepath));
            }
            //remov deleted file from parnet commit
            for(String filepath : STAEG_AREA.deleted){
                this.files.remove(filepath);
            }
        }
        // init repo, parents.size() == 0
        else
            this.files = new HashMap<>();



    }
    public boolean contain(String file){
        return files.containsKey(file);
    }

    public String getsha1(String file){
        return files.get(file);
    }

    private String getDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public void display(){
        System.out.println("===");
        System.out.println("commit "+commitID);
        System.out.println("Date: "+date);
        System.out.println(message);
        System.out.println();
    }
    public boolean hasParent(){
        return parents.size() > 0;
    }
    public String first_parent(){
        return parents.get(0);
    }

    public List<String> getParents(){return parents;}

    public boolean contain_msg(String msg){
        if(message.equals(msg))
            return true;
        return false;
    }

    public String getid(){
        return commitID;
    }


}
