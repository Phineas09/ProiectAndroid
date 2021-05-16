package ro.mta.proiect.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ro.mta.proiect.R;

public class AdminFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public AdminFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = getActivity().getWindow().findViewById(R.id.toolbar);
        toolbar.setTitle("Admin Panel");


        tabLayout = getView().findViewById(R.id.fragment_admin_tab_layout);
        viewPager = getView().findViewById(R.id.fragment_admin_view_pager);
        AdminViewPageAdapter adminViewPageAdapter = new AdminViewPageAdapter(getChildFragmentManager());
        adminViewPageAdapter.AddFragment(new AdminGraphFragment(), "Graphs");
        adminViewPageAdapter.AddFragment(new AdminUsersFragment(), "Users");
        viewPager.setAdapter(adminViewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }



}
