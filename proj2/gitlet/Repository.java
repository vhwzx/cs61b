package gitlet;

import edu.princeton.cs.algs4.ST;

import java.io.File;
import java.util.*;
import java.nio.charset.StandardCharsets;

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
    public static final File COMMIT_DIR = join(GITLET_DIR, "commits");
    public static final File File_DIR = join(GITLET_DIR, "files");

    public static String HEAD;
    public static String BRANCH;
    public static HashMap<String, String> BRANCHES;
    public static StageArea STAGE_AREA;

    public Repository(){
        if(GITLET_DIR.exists()){
            HEAD = readHEAD();
            BRANCH = readBRANCH();
            BRANCHES = readBRANCHES();
            STAGE_AREA = readStageArea();
        }
    }

    /* TODO: fill in the rest of this class. */
    public void init(){
        if(GITLET_DIR.exists())
            exit_message("A Gitlet version-control system already exists in the current directory.");
        else {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            File_DIR.mkdir();
            BRANCH = "master";
            writeBRANCH(BRANCH);

            STAGE_AREA = new StageArea();
            BRANCHES = new HashMap<>();
            commit("initial commit", new ArrayList<>());
        }
    }

    public void add(String path){
        add(new File(path));
    }
    public void add(File file){
        // deal with dir input
        if(file.isDirectory()){
            if(!file.exists())
                exit_message("File does not exist.");
            List<String> plainfiles = plainFilenamesIn(file);
            if(plainfiles != null){
                for(String f: plainfiles){
                    add(new File(f));
                }
            }
        }
        else{
            Commit c = readCommit(HEAD);
            //deal with plain file input
            String filepath = file.getPath();
            // delete file, if a file is in (STAGE_AREA or cur commit) and deleted in work dir, when add it, remove it from STAGE_AREA
            if(!file.exists() && (STAGE_AREA.contain(filepath) || c.contain(filepath))){
                //deleted
                STAGE_AREA.remove(file.getName());
                STAGE_AREA.delete(filepath);
            }
            // try add a not exists file, throw error
            else if(!file.exists())
                exit_message("File does not exist.");
            // add file
            else {
                Blob blob = new Blob(file);

                // if a file is not changed, no need to add it to STAGE AREA
                if(c.contain(filepath) && c.getsha1(filepath).equals(blob.getid())){
                    // remove if it is already in STAGE_AREA
                    STAGE_AREA.remove(filepath);
                    STAGE_AREA.deleted.remove(filepath);
                }
                else {
                    STAGE_AREA.put(filepath, blob.getid());
                    writeBlob(blob);
                }
            }
            // save change to STAGE_AREA
            writeStageArea(STAGE_AREA);
        }
    }

    public void commit(String msg){
        if(STAGE_AREA.isEmpty()){
            exit_message("No changes added to the commit.");
        }
        if(msg.length() == 0){
            exit_message("Please enter a commit message.");
        }

        List<String> parents = new ArrayList<>();
        parents.add(HEAD);
        commit(msg, parents);
    }
    public void commit(String msg, List<String> parents){
        Commit c = new Commit(msg, BRANCH, parents, STAGE_AREA);
        String commitID = c.getid();
        writeCommit(c, commitID);

        HEAD = commitID;
        writeHEAD(HEAD);

        BRANCHES.put(BRANCH, commitID);
        writeBRANCHES(BRANCHES);

        //clear STAGE_AREA
        STAGE_AREA = new StageArea();
        writeStageArea(STAGE_AREA);
    }

    public void rm(String path){
        rm(new File(path));
    }
    public void rm(File file){
        String filepath = file.getPath();
        Commit c = readCommit(HEAD);
        if(!STAGE_AREA.contain(filepath) && !c.contain(filepath))
            exit_message("No reason to remove the file.");
        if(STAGE_AREA.contain(filepath)){
            STAGE_AREA.remove(filepath);
        }
        if(c.contain(filepath)){
            restrictedDelete(file);
            STAGE_AREA.delete(filepath);
        }
        writeStageArea(STAGE_AREA);
    }

    public void log(){
        display_log(HEAD);
    }

    public void display_log(String commitID){
        Commit c = readCommit(commitID);
        c.display();
        if(c.hasParent())
            display_log(c.first_parent());
    }

    public void global_log(){
        String[] commits = COMMIT_DIR.list();
        if(commits != null)
            for(String commitID: commits){
                Commit c = readCommit(commitID);
                c.display();
            }
    }

    public void find(String msg){
        boolean find_at_least_one = false;
        for(String commitID: plainFilenamesIn(COMMIT_DIR)){
            Commit c = readCommit(commitID);
            if(c.contain_msg(msg)){
                System.out.println(commitID);
                find_at_least_one = true;
            }

        }
        if(!find_at_least_one)
            exit_message("Found no commit with that message.");
    }

    public void status(){
        System.out.println("=== Branches ===");
        System.out.println("*"+BRANCH);
        for(String branch_name : BRANCHES.keySet()){
            if(!BRANCH.equals(branch_name))
                System.out.println(branch_name);
        }
        System.out.println();


        List<String> filepaths_list = plainFilenamesIn(CWD);
        HashSet<String> cwd_filepaths = null;
        if(filepaths_list == null)
            return;
        else
            cwd_filepaths = new HashSet<>(filepaths_list);

        HashSet<String> modified = new HashSet<>();
        HashSet<String> deleted = new HashSet<>();
        HashSet<String> untracked = new HashSet<>();
        HashSet<String> staged = new HashSet<>();

        Commit c = readCommit(HEAD);

        for(String filepath : cwd_filepaths){
            Blob blob = new Blob(filepath);
            if(c.contain(filepath) && !c.getsha1(filepath).equals(blob.getid()) && !STAGE_AREA.contain(filepath)){
                modified.add(filepath);
                continue;
            }
            if(STAGE_AREA.contain(filepath) && !STAGE_AREA.get(filepath).equals(blob.getid())){
                modified.add(filepath);
                continue;
            }
            if(STAGE_AREA.contain(filepath) && STAGE_AREA.get(filepath).equals(blob.getid())){
                staged.add(filepath);
                continue;
            }

            if(!c.contain(filepath) && !STAGE_AREA.contain(filepath))
                untracked.add(filepath);
        }

        for(String filepath : c.files.keySet()){
            if(!cwd_filepaths.contains(filepath) && !STAGE_AREA.deleted.contains(filepath))
                deleted.add(filepath);
        }
        for(String filepath : STAGE_AREA.files.keySet()){
            if(!cwd_filepaths.contains(filepath))
                deleted.add(filepath);
        }

        System.out.println("=== Staged Files ===");
        for(String filepath: staged){
            System.out.println(filepath);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for(String filepath: STAGE_AREA.deleted){
            System.out.println(filepath);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        for(String filepath: modified){
            System.out.println(filepath+"(modified)");
        }
        for(String filepath : deleted)
            System.out.println(filepath+"(deleted)");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for(String filepath: untracked){
            System.out.println(filepath);
        }
        System.out.println();

    }

    public void checkout(String name, int type){
        // file
        if(type == 0)
            checkout(HEAD, name);
        // branch
        else
            switch_branch(name);
    }

    //check out a file
    public void checkout(String commitID, String filename){
        if(!join(COMMIT_DIR, commitID).exists())
            exit_message("No commit with that id exists.");
        Commit c = readCommit(commitID);
        if(!c.contain(filename))
            exit_message("File does not exist in that commit.");
        Blob cur_file = new Blob(filename);
        Blob commited_file = readBlob(c.getsha1(filename));
        if(!cur_file.getid().equals(commited_file.getid())){
            restrictedDelete(cur_file);
            restoreFile(commited_file);
            STAGE_AREA.remove(filename);
            writeStageArea(STAGE_AREA);
        }
    }

    public void list_branches(){
        System.out.println("*"+BRANCH);
        for(String branch_name : BRANCHES.keySet()){
            if(!BRANCH.equals(branch_name))
                System.out.println(branch_name);
        }
        System.out.println();
    }
    public void branch(String branch_name){
        if(BRANCHES.containsKey(branch_name))
            exit_message("A branch with that name already exists.");
        BRANCHES.put(branch_name, HEAD);
        writeBRANCHES(BRANCHES);
    }

    public void check_before_switch(String branch_name){
        if(!BRANCHES.containsKey(branch_name))
            exit_message("No such branch exists.");
        if(BRANCH.equals(branch_name))
            exit_message("No need to checkout the current branch.");

        check_untracked_file(BRANCHES.get(branch_name));
    }

    // list files that are in neither STAGE_AREA nor HEAD commit
    public HashSet<String> list_untracked_file(){
        Commit c = readCommit(HEAD);
        HashSet<String> untracked = new HashSet<>();
        List<String> files = plainFilenamesIn(CWD);
        if(files != null){
            for(String f: files){
                Blob blob = new Blob(f);
                if(!STAGE_AREA.contain(f) && !c.contain(f))
                    untracked.add(f);
            }
        }
        return untracked;
    }

    // if an untracked file will be changed by commitID, throw error
    public void check_untracked_file(String commitID){
        Commit c = readCommit(commitID);
        HashSet<String> untracked = list_untracked_file();
        for(String f: untracked){
            Blob blob = new Blob(f);
            if(c.contain(f) && !c.getsha1(f).equals(blob.getid()))
                exit_message("There is an untracked file in the way; delete it, or add and commit it first.");
        }
    }
    public void switch_branch(String branch_name) {
        check_before_switch(branch_name);

        Commit old = readCommit(HEAD);
        HEAD = BRANCHES.get(branch_name);
        BRANCH = branch_name;
        writeHEAD(HEAD);
        writeBRANCH(BRANCH);
        Commit cur = readCommit(HEAD);

        // clear CWD
        for (String filepath : old.files.keySet()) {
            restrictedDelete(filepath);
        }

        for (String filepath : cur.files.keySet()) {
            Blob file = readBlob(cur.getsha1(filepath));
            restoreFile(filepath, file);
        }
    }

    public void rm_branch(String branch_name){
        if(!BRANCHES.containsKey(branch_name))
            exit_message("A branch with that name does not exist.");
        if(branch_name.equals(BRANCH))
            exit_message("Cannot remove the current branch.");
        BRANCHES.remove(branch_name);
        writeBRANCHES(BRANCHES);
    }

    public void reset(String commitID){
        if(!join(COMMIT_DIR, commitID).exists())
            exit_message("No commit with that id exists.");
        check_untracked_file(commitID);
        BRANCHES.put(BRANCH, commitID);
        writeBRANCHES(BRANCHES);

        Commit cur = readCommit(HEAD);
        Commit c = readCommit(commitID);
        Set<String> tracked = combine(cur.files.keySet(), STAGE_AREA.files.keySet(), STAGE_AREA.deleted);

        // remove all tracked files not in given commit
        for(String filepath: tracked){
            if(!c.contain(filepath)){
                restrictedDelete(filepath);
            }
        }

        // reset all file in given commit
        for(String filepath : c.files.keySet()){
            File cur_file = new File(filepath);
            if(cur_file.exists()){
                Blob cur_blob = new Blob(filepath);
                if(!c.getsha1(filepath).equals(cur_blob.getid())){
                    restrictedDelete(cur_blob);
                    restoreFile(c.getsha1(filepath));
                }
            }
            else {
                restoreFile(c.getsha1(filepath));
            }
        }

        // update STAGE AREA
        STAGE_AREA.clear();
        writeStageArea(STAGE_AREA);
        HEAD = commitID;
        writeHEAD(HEAD);

    }

    public HashSet<String> list_parent_commits(String commitID){
        HashSet<String> vis = new HashSet<>();
        Deque<String> q = new LinkedList<>();
        vis.add(commitID);
        q.addLast(commitID);
        while (!q.isEmpty()){
            String cur = q.removeFirst();
            Commit c = readCommit(cur);
            if(c.hasParent())
                for(String p : c.getParents()){
                    if(!vis.contains(p)){
                        vis.add(p);
                        q.addLast(p);
                    }
                }
        }
        return vis;
    }
    public String find_split_point(String cur_branch_id, String other_branch_id){
        HashSet<String> parents = list_parent_commits(cur_branch_id);
        if(parents.contains((other_branch_id)))
            return other_branch_id;
        HashSet<String> vis = new HashSet<>();
        Deque<String> q = new LinkedList<>();
        q.addLast(other_branch_id);
        vis.add(other_branch_id);
        while(!q.isEmpty()){
            String cur = q.removeFirst();
            Commit c = readCommit(cur);
            if(c.hasParent())
                for(String p : c.getParents()){
                    if(parents.contains(p))
                        return p;
                    if(!vis.contains(p)){
                        vis.add(p);
                        q.addLast(p);
                    }
                }
        }
        throw error("No split point found");
    }

    public boolean isChanged(Commit prev, Commit later, String file){
        return !isSameFile(prev, later, file);
    }

    public boolean isSameFile(Commit x, Commit y, String file){
        if(x.contain(file) != y.contain(file))
            return false;
        if(!x.contain(file) && !y.contain(file))
            return true;
        return x.getsha1(file).equals(y.getsha1(file));
    }
    public int merge_state(Commit split, Commit cur, Commit other, String file){
        if(isChanged(split, cur, file) && isChanged(split, other, file)){
            // modified in same way
            if(isSameFile(cur, other, file))
                return 0;
            // modified in different way, merge conflict
            else {
                return 1;
            }
        } else if(!isChanged(split, cur, file) && !isChanged(split, other, file)){
            // file is not changed, nothing to do
            return 2;
        }
        // file is change only in one branch, follow the changed branch
        else if (isChanged(split, cur, file)) {
            // follow cur branch
            return 3;
        }
        // follow other branch
        return 4;
    }

    public int change_type(Commit split, Commit y, String file){
        // modify
        if(split.contain(file) && y.contain(file))
            return 0;
        // remove
        if(split.contain(file) && !y.contain(file))
            return 1;
        // add
        if(!split.contain(file) && y.contain(file))
            return 2;
        throw error("no such state");
    }

    public void modify(String oldfile, Blob newfile){
        restrictedDelete(oldfile);
        restoreFile(oldfile, newfile);
    }

    public void deal_with_file_change(Commit split, Commit y, String f){
        Blob newfile;
        switch (change_type(split, y, f)){
            case 0:
                newfile = readBlob(y.getsha1(f));
                modify(f, newfile);
                STAGE_AREA.put(f, newfile.getid());
                break;
            case 1:
                restrictedDelete(f);
                //STAGE_AREA.delete(f);
                break;
            case 2:
                newfile = readBlob(y.getsha1(f));
                restoreFile(f, newfile);
                STAGE_AREA.put(f, newfile.getid());
                break;
            default:
                throw error("no such change type");
        }
    }

    public void deal_with_conflict(Commit cur, Commit other, String f){
        StringBuilder conflictfile = new StringBuilder();
        conflictfile.append("<<<<<<< HEAD\n");
        if(cur.contain(f)){
            Blob curfile = readBlob(cur.getsha1(f));
            conflictfile.append(readContentsAsString(curfile));
        }
        conflictfile.append("=======\n");
        if(other.contain(f)){
            Blob otherfile = readBlob(other.getsha1(f));
            conflictfile.append(readContentsAsString(otherfile));
        }
        conflictfile.append(">>>>>>>\n");
        String file = conflictfile.toString();
        Blob conflictblob = new Blob(f, file.getBytes(StandardCharsets.UTF_8));
        writeBlob(conflictblob);
        restoreFile(f, conflictblob);
        STAGE_AREA.put(f, conflictblob.getid());
    }
    public void merge(String other_branch_name){
        if(!STAGE_AREA.isEmpty())
            exit_message("You have uncommitted changes.");
        if(!BRANCHES.containsKey(other_branch_name))
            exit_message("A branch with that name does not exist.");
        if(BRANCH.equals(other_branch_name))
            exit_message("Cannot merge a branch with itself.");

        // check for untracked file
        check_untracked_file(BRANCHES.get(other_branch_name));

        String other_branch_id = BRANCHES.get(other_branch_name);
        String SPLIT = find_split_point(HEAD, other_branch_id);

        if(SPLIT.equals(other_branch_id)){
            exit_message("Given branch is an ancestor of the current branch");
        }

        if(SPLIT.equals(HEAD)){
            switch_branch(other_branch_name);
            exit_message("Current branch fast-forwarded.");
        }


        Commit split = readCommit(SPLIT);
        Commit cur = readCommit(HEAD);
        Commit other = readCommit(other_branch_id);
        Set<String> files = combine(split.files.keySet(), cur.files.keySet(), other.files.keySet());

        boolean conflict = false;
        for(String f: files){
            int state = merge_state(split, cur, other, f);
            switch (state){
                case 0:
                    // changed in same way
                    deal_with_file_change(split, cur, f);
                    break;
                case 1:
                    // changed in diff way, merge conflict
                    deal_with_conflict(cur, other, f);
                    conflict = true;
                    break;
                case 2:
                    // not changed, nothing to do
                    break;
                case 3:
                    // only changed in cur branch
                    deal_with_file_change(split, cur, f);
                    break;
                case 4:
                    // only changed in other branch
                    deal_with_file_change(split, other, f);
                    break;
                default:
                    throw error("no such a state");
            }
        }
        // make a commit after merge
        List<String> parents = new ArrayList<>();
        parents.add(HEAD);
        parents.add(BRANCHES.get(other_branch_name));
        commit(String.format("Merged %s into %s.", other_branch_name, BRANCH), parents);
        if(conflict)
            System.out.println("Encountered a merge conflict.");
    }

}
