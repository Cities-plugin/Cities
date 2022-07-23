package nl.partytitan.cities.tasks;

import nl.partytitan.cities.utils.FileUtils;

import java.io.File;

public class FlatFileSaveTask implements Runnable {

    private final String contents;
    private final File file;

    public FlatFileSaveTask(String contents, File file){

        this.contents = contents;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            FileUtils.stringToFile(contents, file);
        } catch (NullPointerException e){
        }
    }
}

