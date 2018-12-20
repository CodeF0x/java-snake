package com.codef0x.snake;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Iterator;

public class ScoreSaver {
    File file;
    String filepath;

    public ScoreSaver() {
        this.filepath = "highscore-snake.txt";
        this.file = new File(this.filepath);
    }

    public void saveHighscore(String highscore) throws IOException {
        File file = this.file;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        writer.write(highscore);
        writer.close();
    }

    public String loadHighscore() throws IOException {
        String output = "";
        if (fileExistent()) {
            BufferedReader reader = new BufferedReader(new FileReader(this.filepath));
            output = reader.readLine();
            reader.close();
            return output;
        } else {
            output = "None yet";
            return output;
        }
    }

    public boolean fileExistent() throws IOException {
        if (this.file.exists() && !this.file.isDirectory()) {
            return true;
        }
        return false;
    }
}
