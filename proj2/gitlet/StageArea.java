package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;
import static gitlet.Utils.readContents;

public class StageArea implements Serializable {
    public HashMap<String, String> files;
    public HashSet<String> deleted;

    public StageArea(){
        files = new HashMap<>();
        deleted = new HashSet<>();
    }
    public boolean contain(String filepath){
        return files.containsKey(filepath);
    }
    public void put(String filepath, String h){
        //need to save, add new hash ref count, reduce old hash ref count
        if(files.containsKey(filepath) && files.get(filepath).equals(h)){
            files.put(filepath, h);
        }

        //need to save, add new hash ref count
        if(!files.containsKey(filepath)){
            files.put(filepath, h);
        }
        // in case the file is staged for remove before
        deleted.remove(filepath);
    }

    public boolean isEmpty(){
        return files.isEmpty()&&deleted.isEmpty();
    }

    public void remove(String filepath){
        files.remove(filepath);
    }

    public void delete(String filepath){
        deleted.add(filepath);
    }

    public String get(String filepath){
        return files.get(filepath);
    }

    public void clear(){
        files.clear();
        deleted.clear();
    }




}



