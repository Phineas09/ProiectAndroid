package ro.mta.proiect.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import ro.mta.proiect.MainActivity;
import ro.mta.proiect.R;
import ro.mta.proiect.database.Chapter;
import ro.mta.proiect.database.ChapterDB;

public class ChapterFragment extends Fragment implements View.OnTouchListener, ViewTreeObserver.OnScrollChangedListener {

    private int chapter;
    private Chapter currentChapter;
    private ScrollView scrollView;
    private boolean alreadyRead;

    public ChapterFragment(String chapter) throws Exception {
        this.chapter = Integer.parseInt(chapter);
        currentChapter =  ChapterDB.getInstance(getContext()).getChapterDao().getChapterByKey(this.chapter);
        if(currentChapter == null)
            throw new Exception("Chapter not yet available!");
        alreadyRead = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chapter, container, false);
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = getActivity().getWindow().findViewById(R.id.toolbar);
        toolbar.setTitle("Chapter " + chapter);

        scrollView = getView().findViewById(R.id.fragment_chapter_scroll_view);
        scrollView.setOnTouchListener(this);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);


        if(currentChapter != null) {

            /**
             * Get additional information from database
             */
            ((TextView) getView().findViewById(R.id.fragment_chapter_title)).setText(currentChapter.getChapterName());
            ((ImageView) getView().findViewById(R.id.fragment_chapter_image)).setImageResource(getImageId(getContext(), currentChapter.getChapterImage()));
            ((TextView) getView().findViewById(R.id.fragment_chapter_content)).setText(currentChapter.getChapterContent());
        }
    }

    public void onScrollChanged(){
        if(!alreadyRead) {

            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int bottomDetector = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());
            if (bottomDetector == 0) {
                /**
                 * If the user has reached the end of the chapter
                 */
                MainActivity.getCurrentUserDetails().addReadChapter(currentChapter.getChapterName());
                alreadyRead = true;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
