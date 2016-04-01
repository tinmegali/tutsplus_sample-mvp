package com.tinmegali.tutsmvp_sample.main.activity.view;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.EditText;

import com.tinmegali.tutsmvp_sample.R;
import com.tinmegali.tutsmvp_sample.main.activity.MVP_Main;
import com.tinmegali.tutsmvp_sample.util.ChildClickAction;
import com.tinmegali.tutsmvp_sample.util.CustomViewHolderMatcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 30/03/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> testRule =
            new ActivityTestRule<>(MainActivity.class);

    private MVP_Main.ProvidedPresenterOps presenterOps;


    @Before
    public void registerIdleRes() {
        presenterOps = testRule.getActivity().mPresenter;
    }


    @Test
    public void testBlankItem() {
        EditText editText = (EditText) testRule.getActivity().findViewById(R.id.edit_note);
        assertNotNull(editText);
        assertTrue(editText.getText().toString().isEmpty());
        onView(withId(R.id.fab)).perform(click());
        // Verifying Toast msg
        onView(withText(R.string.toast_empty_note))
                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    private String addNewItem(int noteQtd) {
        final String noteText = "New Note [" + Integer.toString(noteQtd) + "]";

        // Adding Item
        onView(withId(R.id.edit_note)).perform(typeText(noteText));
        onView(withId(R.id.fab)).perform(click());
        return noteText;
    }

    private void checkItem(String noteText, int notesQtd) {
        // Verify item
        onView(withId(R.id.list_notes))
                .check(matches(hasDescendant(withText(noteText))));
        assertTrue(presenterOps.getNotesCount() == notesQtd + 1);
        assertTrue(testRule.getActivity().mListAdapter.getItemCount() == notesQtd + 1);
    }

    private void clickDeleteItem(String noteText) {
        // click btn Delete
        onView((withId(R.id.list_notes)))
                .perform(actionOnHolderItem(
                        new CustomViewHolderMatcher(hasDescendant(withText(startsWith(noteText)))),
                        ChildClickAction.clickOnChild(R.id.btn_delete)
                ));
    }

    private void deleteItem(String noteText) {
        // click btn Delete
        clickDeleteItem(noteText);
        // Check if AlertBox is displayed
        final String alertMsg =
                String.format(testRule.getActivity().getString(R.string.alert_del_msg), noteText);
        onView(withText(alertMsg)).check(matches(isDisplayed()));
        // confirming delete
        onView(withId(android.R.id.button1)).perform(click());

        // Verifying Toast msg
        onView(withText(R.string.toast_delete_success))
                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testingNewItem() {
        assertNotNull(presenterOps);
        final int notesQtd = presenterOps.getNotesCount();
        String noteText = addNewItem(notesQtd);

        // Verify item
        checkItem(noteText, notesQtd);

        // click btn Delete
        clickDeleteItem(noteText);

        // Check if AlertBox is displayed
        final String alertMsg =
                String.format(testRule.getActivity().getString(R.string.alert_del_msg), noteText);
        onView(withText(alertMsg)).check(matches(isDisplayed()));
        // cancel
        onView(withId(android.R.id.button2)).perform(click());

        deleteItem(noteText);

    }


}