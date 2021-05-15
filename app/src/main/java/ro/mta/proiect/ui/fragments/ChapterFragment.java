package ro.mta.proiect.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import ro.mta.proiect.R;
import ro.mta.proiect.database.Chapter;
import ro.mta.proiect.database.ChapterDB;

public class ChapterFragment extends Fragment {

    private int chapter;
    private Chapter currentChapter;

    public ChapterFragment(String chapter) throws Exception {
        this.chapter = Integer.parseInt(chapter);
        currentChapter =  ChapterDB.getInstance(getContext()).getChapterDao().getChapterByKey(this.chapter);
        if(currentChapter == null)
            throw new Exception("Chapter not yet available!");
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

        if(currentChapter != null) {

            /**
             * Get additional information from database
             */
            ((TextView) getView().findViewById(R.id.fragment_chapter_title)).setText(currentChapter.getChapterName());
            ((ImageView) getView().findViewById(R.id.fragment_chapter_image)).setImageResource(getImageId(getContext(), currentChapter.getChapterImage()));
            ((TextView) getView().findViewById(R.id.fragment_chapter_content)).setText(currentChapter.getChapterContent());
        }
    }
}
