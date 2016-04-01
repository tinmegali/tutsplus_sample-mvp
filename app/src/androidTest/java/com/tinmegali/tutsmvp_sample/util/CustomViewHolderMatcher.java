package com.tinmegali.tutsmvp_sample.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tinmegali.tutsmvp_sample.main.activity.view.recycler.NotesViewHolder;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.any;

/**
 * ---------------------------------------------------
 * Created by Tin Megali on 01/04/16.
 * Project: tuts+mvp_sample
 * ---------------------------------------------------
 * <a href="http://www.tinmegali.com">tinmegali.com</a>
 * <a href="http://www.github.com/tinmegali>github</a>
 * ---------------------------------------------------
 */
public class CustomViewHolderMatcher extends TypeSafeMatcher<RecyclerView.ViewHolder> {
    private Matcher<View> itemMatcher = any(View.class);

    public CustomViewHolderMatcher() { }

    public CustomViewHolderMatcher(Matcher<View> itemMatcher) {
        this.itemMatcher = itemMatcher;
    }

    @Override
    public boolean matchesSafely(RecyclerView.ViewHolder viewHolder) {
        return NotesViewHolder.class.isAssignableFrom(viewHolder.getClass())
                && itemMatcher.matches(viewHolder.itemView);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is assignable from CustomViewHolder");
    }
}
