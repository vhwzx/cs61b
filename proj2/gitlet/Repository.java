package gitlet;

import java.io.File;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File FILE_DIR = join(GITLET_DIR, "files");

    public static final File STAGE_DIR = join(GITLET_DIR, "stage-files");
    public static final File STAGE_FILE = join(GITLET_DIR, "stage");

    public static final File COMMIT_FILE = join(GITLET_DIR, "commit");

    public static void init(){
        if(exists()){
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.out.println();
        }
        else {
            System.out.println("gitlet repositary init");
            GITLET_DIR.mkdir();
            FILE_DIR.mkdir();
            STAGE_DIR.mkdir();
            StageArea stage = new StageArea();
            saveStage(stage);
            Commit c = new Commit("initial commit", null);
            writeObject(COMMIT_FILE, c);
        }
    }
    public static boolean exists(){
        return GITLET_DIR.exists();
    }

    public static void add(List<String> args){
        if(args == null)
            return ;
        for(String file : args){
            if(validFile(file)){
                if(file.equals(".")){
                    add(plainFilenamesIn(CWD));
                }
                else {
                    add(new File(file));
                }
            }
        }
    }

    private static StageArea getStage(){
        return StageArea.fromFile(STAGE_FILE);
    }

    private static void saveStage(StageArea stage){
        writeObject(STAGE_FILE, stage);
    }

    private static boolean validFile(String file){
        if(file.equals("."))
            return true;
        return validFile(new File(file));
    }
    private static boolean validFile(File file){
        if(!isSubdir(file, CWD)){throw new GitletException("file must be inside gitlet repository");}
        if(!fileExists(file)){throw  new GitletException("File does not exist");}
        return true;
    }
    private static void add(File file){
        if(file.isFile()){
            StageArea stage = getStage();
            stage.add(file);
            saveStage(stage);
        }
    }

    public static void status(){
        StageArea stage = getStage();
        stage.status();
    }

    private static Commit getHead(){
        return Commit.fromFile(COMMIT_FILE);
    }
    public static void commit(String msg){
        Commit head = Commit.fromFile(COMMIT_FILE);
        Commit newc = new Commit(msg, head);
        StageArea stage = StageArea.fromFile(STAGE_FILE);
        newc.commit(stage);
    }

    public static void rm(File file){
        StageArea stage = getStage();
        Commit head = getHead();
        if(stage.contain(file))
            stage.remove(file);
        if(head.contain(file))
            head.remove(file);

    }


    /* TODO: fill in the rest of this class. */
}
