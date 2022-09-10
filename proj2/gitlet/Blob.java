package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Repository.CWD;
import static gitlet.Utils.*;
import java.io.Serializable;

public class Blob implements Serializable {

    private String hash;
    private byte[] content;
    private String path;
    private int refer_count;

    public Blob(String path){
        this.path = path;
        content = readContents(join(CWD, path));
        hash = sha1(content);
        refer_count = 1;

    }

    public Blob(String path, byte[] content){
        this.path = path;
        this.content = content;
        hash = sha1(this.content);
        refer_count = 1;

    }

    public Blob(File file){
        path = file.getPath();
        content = readContents(file);
        hash = sha1(content);
        refer_count = 1;
    }
    public String getid(){
        return hash;
    }

    public byte[] getContent(){
        return content;
    }

    public String getPath(){
        return path;
    }
}
