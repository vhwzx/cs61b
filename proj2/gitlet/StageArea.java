package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;
import static gitlet.Utils.readContents;

public class StageArea implements Serializable {
    HashMap<String, String> files;
    List<String> staged;
    List<String> removed;
    List<String> modified;
    List<String> untracked;

    void StageArea(){files = null;}

    public static StageArea fromFile(File STAGE_FILE){
        return readObject(STAGE_FILE, StageArea.class);
    }

    boolean isEmpty(){
        return true;
    }

    void add(File file){
        byte[] contents = readContents(file);
        String hash = sha1(contents);
        files.put(file.toString(), hash);
        writeContents(join(Repository.STAGE_DIR, hash), contents);
    }

    public void remove(File file){
        String hash = files.get(file.toString());
        File tmp = join(Repository.STAGE_DIR, hash);
        tmp.delete();
        files.remove(file.toString());
    }

    boolean contain(File file){ return files.containsKey(file.toString());}
    boolean contain(String file){ return files.containsKey(file);}

    String get(String key){
        return files.get(key);
    }

    void clear(){
        staged.clear();
        removed.clear();
        modified.clear();
        untracked.clear();
    }
    void status(){
        clear();
        List<String> curfiles = plainFilenamesIn(Repository.CWD);
        // list all plain files in CWD
        for(String file : curfiles) {
            String h1 = sha1(readContents(file));
            // if a file exists in stage area, check it
            if(this.contain(file)) {
                // if file is removed(hash is null), add to removed
                String h2 = this.get(file);
                if(h2 == null)
                    removed.add(file);
                // if not changed, add to staged
                else if (h2.equals(h1)) {
                    staged.add(file);
                } else {
                    // if changed, add to Modifications Not Staged For Commit(modified)
                    modified.add(file+ "(modified)");
                }
            }
            else {
                // if a file not exists in stage area, add to Untracked Files
                untracked.add(file);
            }
        }

        Set<String> curfiles_set = new HashSet<>(curfiles);
        // iter over all files in stage area
        for(String file : files.keySet()) {
            // if a file not exists in CWD, add to modified(deleted)
            if (!curfiles_set.contains(file))
                modified.add(file + "(deleted)");
        }
    }

}



