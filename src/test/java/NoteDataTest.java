import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyiker.stickies.app.io.DataReader;
import com.hyiker.stickies.app.model.DataStorage;
import com.hyiker.stickies.app.model.NoteData;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 由sidhch于2018/2/1创建
 */

public class NoteDataTest {
    private static Logger logger = LoggerFactory.getLogger(NoteDataTest.class);

    /**
     * {
     * "notes":[{...},{...}]
     * "last_open":""
     * }
     */
    @Test
    public void restore() {
        DataStorage ds = new DataStorage();
        NoteData noteData = new NoteData();
        noteData.setId("123123asd");
        Rectangle ra = new Rectangle(1, 2, 3, 4);
        noteData.setBounds(ra);
        ds.addSingleData(noteData);
        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(ds);
            logger.info(json);
            DataStorage ds1 = om.readValue(json, DataStorage.class);
            logger.info(ds1.getNoteData().get(0).getBounds().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void read() {
        DataReader dr = new DataReader();
        try {
            System.out.println(dr.readNotesData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
