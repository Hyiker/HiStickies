package com.hyiker.stickies.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hyiker.stickies.app.model.basic.BasicModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 由sidhch于2018/2/1创建
 */
public class DataStorage extends BasicModel {
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

}
