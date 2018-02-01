package com.hyiker.stickies.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 由sidhch于2018/2/1创建
 */
public class DataStorage {
    @JsonProperty("notes")
    private List<NoteData> noteData;

    public List<NoteData> getNoteData() {
        return noteData;
    }

    public void setNoteData(List<NoteData> notes) {
        this.noteData = notes;
    }

    public void addSingleData(NoteData nd) {
        if (noteData == null) {
            noteData = new ArrayList<>();
        }
        noteData.add(nd);
    }

    @Override
    public String toString() {
        String str = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            str = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
