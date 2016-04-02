package com.tinmegali.tutsmvp_sample;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.tinmegali.tutsmvp_sample.main.activity.MVP_Main;
import com.tinmegali.tutsmvp_sample.main.activity.model.MainModel;
import com.tinmegali.tutsmvp_sample.main.activity.presenter.MainPresenter;
import com.tinmegali.tutsmvp_sample.main.activity.view.MainActivity;
import com.tinmegali.tutsmvp_sample.models.Note;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;

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
public class MainPresenterTest {

    private MainPresenter mPresenter;
    private MainModel mockModel;
    private MVP_Main.RequiredViewOps mockView;
    private Context mockContext;

    @Before
    public void setup() {
        mockView = Mockito.mock(MVP_Main.RequiredViewOps.class, RETURNS_DEEP_STUBS);
        mockContext = Mockito.mock(Context.class, RETURNS_DEEP_STUBS);
        when(mockView.getActivityContext()).thenReturn(mockContext);

        mockModel = Mockito.mock( MainModel.class, RETURNS_DEEP_STUBS );
        mPresenter = new MainPresenter( mockView );
        mPresenter.setModel(mockModel);

        when(mockModel.loadData()).thenReturn(true);
        reset(mockView);
    }

    @Test
    public void testClickNewNote() {
        String noteText = "NewNote";
        int arrayPos = 10;
        when(mockModel.insertNote(any(Note.class))).thenReturn(arrayPos);

        mPresenter.clickNewNote(noteText);
        verify(mockModel).insertNote(any(Note.class));
        verify(mockView).notifyItemInserted( eq(arrayPos+1) );
        verify(mockView).notifyItemRangeChanged(eq(arrayPos), anyInt());
        verify(mockView, never()).showToast(any(Toast.class));
    }

    @Test
    public void testClickNewNoteError() {
        MainPresenter spyPresenter = spy(mPresenter);
        when(spyPresenter.getActivityContext()).thenReturn(mockContext);
        when(mockContext.getString(R.string.toast_error))
                .thenReturn(RuntimeEnvironment.application.getString(R.string.toast_error));

        String noteText = "NewNote";
        when(mockModel.insertNote(any(Note.class))).thenReturn(-1);
        when(mockModel.insertNote(any(Note.class))).thenReturn(-1);
        mPresenter.clickNewNote(noteText);
        verify(mockView).showToast(any(Toast.class));
    }

    @Test
    public void testDeleteNote(){
        MainPresenter spyPresenter = spy(mPresenter);
        when(mockModel.deleteNote(any(Note.class), anyInt())).thenReturn(true);
        doReturn(mock(Toast.class)).when(spyPresenter).makeToast(anyString());

        int adapterPos = 0;
        int layoutPos = 1;
        spyPresenter.deleteNote(new Note(), adapterPos, layoutPos);
        verify(mockView).showProgress();
        verify(mockModel).deleteNote(any(Note.class), eq(adapterPos));
        verify(mockView).hideProgress();
        verify(mockView).notifyItemRemoved(eq(layoutPos));
        verify(mockView).showToast(any(Toast.class));
    }

    @Test
    public void testDeleteNoteError(){
        MainPresenter spyPresenter = spy(mPresenter);
        when(mockModel.deleteNote(any(Note.class), anyInt())).thenReturn(false);
        when(spyPresenter.getActivityContext()).thenReturn(mockContext);
        when(mockContext.getString(R.string.toast_delete_error))
                .thenReturn(RuntimeEnvironment.application.getString(R.string.toast_delete_error));

        int adapterPos = 0;
        int layoutPos = 1;
        mPresenter.deleteNote(new Note(), adapterPos, layoutPos);
        verify(mockView).showProgress();
        verify(mockModel).deleteNote(any(Note.class), eq(adapterPos));
        verify(mockView).hideProgress();
        verify(mockView).showToast(any(Toast.class));

    }


}
