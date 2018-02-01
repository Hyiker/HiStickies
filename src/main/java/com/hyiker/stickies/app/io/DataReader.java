package com.hyiker.stickies.app.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyiker.stickies.app.Note;
import com.hyiker.stickies.app.model.DataStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 由sidhch于2018/2/1创建
 */
public class DataReader {
    private static final String SAVE_PATH = "/Users/sidhch/JavaData/data.json";
    private static final File SAVE_FILE = new File(SAVE_PATH);
    private final ObjectMapper om = new ObjectMapper();

    public DataReader() {
    }

    public void saveNotesData(List<Note> noteList) throws IOException {
        DataStorage ds = new DataStorage();
        for (Note note : noteList) {
            ds.addSingleData(note.getNoteData());
        }
        byte[] b = om.writeValueAsBytes(ds);
        FileOutputStream fos = new FileOutputStream(SAVE_FILE);
        fos.write(b);
        fos.close();
    }

    public DataStorage readNotesData() throws IOException {
        if (!SAVE_FILE.exists()) {
            return null;
        }
        return om.readValue(SAVE_FILE, DataStorage.class);
    }
}
