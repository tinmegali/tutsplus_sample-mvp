package com.tinmegali.tutsmvp_sample;

import android.content.Context;

import com.tinmegali.tutsmvp_sample.data.DAO;
import com.tinmegali.tutsmvp_sample.models.Note;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 18/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class DBTest {

    private DAO dao;

    @Before
    public void setup() {
        Context context = RuntimeEnvironment.application;
        dao = new DAO(context);
    }

    private Note getNote(String text) {
        Note note = new Note();
        note.setText(text);
        note.setDate("some date");
        return note;
    }

    @Test
    public void insertNote() {
        String noteText = "noteText";
        long id = dao.insertNote(getNote(noteText));
        assertTrue(id > 0);

        Note note = dao.getNote((int)id);
        assertNotNull(note);
        assertEquals(note.getText(), noteText);
    }

    @Test
    public void noteList() {
        ArrayList<String> noteTexts = new ArrayList<>();
        noteTexts.add( "note1" );
        noteTexts.add( "note2" );
        noteTexts.add("note3");

        for( int i=0; i<noteTexts.size(); i++){
            Note note = new Note(noteTexts.get(i), "some date");
            dao.insertNote(note);
        }

        ArrayList<Note> notes = dao.getAllNotes();
        assertNotNull( notes );
        assertEquals(notes.size(), noteTexts.size());
    }

    @Test
    public void deleteNote() {
        Note note = getNote("deleteNote");
        long id = dao.insertNote(note);
        assertTrue(id > 0);

        Note noteInserted = dao.getNote((int)id);
        assertNotNull(noteInserted);
        assertEquals(noteInserted.getText(), note.getText());

        long delResult = dao.deleteNote( noteInserted );
        assertEquals(1, delResult);
    }
}
