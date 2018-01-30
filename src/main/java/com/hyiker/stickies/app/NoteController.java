package com.hyiker.stickies.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
    private final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width,
            SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private String save_path = "/Users/sidhch/JavaData/data.json";
    private static final String introduce = "双击锁定/解锁\n双击长按" + ((float) (MyTextArea.close_delay / 1000)) + "秒关闭\n可拖动左上角或者右下角改变大小";

    public static void main(String[] args) {
        instance.init();
    }

    private NoteController() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::end));
    }

    private void init() {
        JSONArray data;
        noteList = new ArrayList<>();
        try {
            data = read();
        } catch (IOException e) {
            data = null;
        }

        createNotes(data);
        setUpMenu();
        setUndecorated(true);
        setVisible(true);
    }


    private void createNote(int x, int y, int w, int h, String text) {
        createNote(null, x, y, w, h, text, true);
    }

    private void createNote(String id, int x, int y, int w, int h, String text, Boolean visible) {
        noteList.add(new Note(id, x, y, w, h, text, visible));
    }

    private void createNotes(JSONArray data) {
        if (data == null) {
            createNote(20, 80);
            return;
        }
        for (Object o : data) {
            JSONObject d = (JSONObject) o;
            JSONArray a = (JSONArray) d.get("bounds");
            Boolean visible = (Boolean) d.get("visible");
            if (visible == null) {
                visible = true;
            }
            createNote((String) d.get("id"), (int) a.get(0), (int) a.get(1), (int) a.get(2), (int) a.get(3), (String) d.get("text"), visible);
        }

    }

    private void createNote(int x, int y) {
        createNote(x, y, 0, 0, introduce);
    }

    private boolean save() throws IOException {
        JSONArray data = new JSONArray();
        if (noteList != null) {
            for (Note n : noteList) {
                JSONObject note = new JSONObject();
                note.put("id", n.getId());


                Point position = n.getLocation();
                Dimension size = n.getSize();
                JSONArray bounds = new JSONArray();
                bounds.add(0, (int) position.getX());
                bounds.add(1, (int) position.getY());
                bounds.add(2, (int) size.getWidth());
                bounds.add(3, (int) size.getHeight());

                note.put("bounds", bounds);
                note.put("text", n.getTa().getText());
                note.put("date", System.currentTimeMillis());
                note.put("visible", n.isVisible());
                data.add(note);
            }
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(save_path));

        bos.write(data.toString().getBytes());
        bos.close();
        return true;
    }

    private JSONArray read() throws IOException {
        byte[] b;
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(save_path));
        b = new byte[bis.available()];
        bis.read(b);
        String data_str = new String(b);

        return JSON.parseArray(data_str);
    }

    public static NoteController getInstance() {
        return instance;
    }


    public static String getUUID() {
        return UUID.randomUUID().toString();
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

    public boolean end() {
        try {
            return save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setVisibleForAll(boolean visibleForAll) {
        for (Note n : noteList) {
            if (n != null) {
                n.setVisible(visibleForAll);
            }
        }
    }
}
