package com.hyiker.stickies.app.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyiker.stickies.app.Note;
import com.hyiker.stickies.app.model.DataStorage;
import com.hyiker.stickies.app.model.MenuInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * 由sidhch于2018/2/1创建
 */
public class JSONDataHandler {
    private static final Logger logger = LoggerFactory.getLogger(JSONDataHandler.class);
    // private static final String SAVE_PATH = "/Users/sidhch/JavaData/data.json";
    private final File MENU_SETTINGS = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("menu.json")).getPath());
    private URL SAVE_PATH = this.getClass().getClassLoader().getResource("save.json");
    private final File SAVE_FILE = new File(Objects.requireNonNull(SAVE_PATH).getPath());
    private final ObjectMapper om = new ObjectMapper();

    public JSONDataHandler() {
        //logger.info("SAVE_PATH is '{}'", SAVE_PATH.getPath());
        //logger.info("Is file exists '{}'", SAVE_FILE.exists());
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


    public List<MenuInfo> readMenuInfo() throws IOException {
        if (!MENU_SETTINGS.exists()) {
            return null;
        }
        return om.readValue(MENU_SETTINGS, om.getTypeFactory().constructParametricType(List.class, MenuInfo.class));
    }
}
