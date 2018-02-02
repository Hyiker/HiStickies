package com.hyiker.stickies.app;

import com.hyiker.stickies.app.io.DataReader;
import com.hyiker.stickies.app.model.DataStorage;
import com.hyiker.stickies.app.model.NoteData;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * 由sidhch于2018/1/27创建
 */
public class NoteController extends MyFrame {
    private List<Note> noteList;
    private static NoteController instance = new NoteController();
    private DataReader reader;
    private final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width,
            SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private String save_path = "/Users/sidhch/JavaData/data.json";

    public static void main(String[] args) {
        instance.init();
    }

    /*<-- Controller concerned functions start -->*/
    private NoteController() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("on shutdown()");
            shutdown();
        }));
    }

    private void init() {
        DataStorage data;
        noteList = new ArrayList<>();
        reader = new DataReader();
        try {
            data = reader.readNotesData();
        } catch (IOException e) {
            data = null;
        }

        createNotes(data);
        setUpMenu();
        setUndecorated(true);
        setVisible(true);
    }

    public static NoteController getInstance() {
        return instance;
    }

    public void shutdown() {
        try {
            reader.saveNotesData(instance.getNoteList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*<-- Controller concerned functions end -->*/


    /*<-- Note/Notes concerned functions start -->*/

    private void createNote(NoteData nd) {
        noteList.add(new Note(nd));
    }

    private void createNote(int x, int y) {
        noteList.add(new Note(x, y));
    }

    public void createDefaultNote() {
        noteList.add(new Note());
    }

    private void createNotes(DataStorage data) {

        List<NoteData> noteData = null;
        if (data != null) {
            noteData = data.getNoteData();
        }
        if (noteData == null) {
            createDefaultNote();
            return;
        }
        for (NoteData nd : noteData) {
            createNote(nd);
        }

    }

    public int destroyNote(String id) {
        Note note = null;
        for (int i = 0; i < noteList.size(); i++) {
            if (Objects.equals(id, noteList.get(i).getId())) {
                note = noteList.remove(i);
                break;
            }
        }
        if (note == null) {
            return 0;
        }

        note.fadeOut(500);

        return 1;
    }

    public void instantiateRandomNote() {
        Random rand = new Random();
        int x, y;
        x = rand.nextInt(SCREEN_WIDTH - Note.DEFAULT_WIDTH);
        y = rand.nextInt((SCREEN_HEIGHT - Note.DEFAULT_HEIGHT) / 2);
        createNote(x, y);
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setVisibleForAll(boolean visibleForAll) {
        for (Note n : noteList) {
            if (n != null) {
                n.setVisible(visibleForAll);
            }
        }
    }


    /*<-- Note/Notes concerned functions end -->*/


    /*<-- util functions start -->*/

    /*<-- util functions end -->*/


}
