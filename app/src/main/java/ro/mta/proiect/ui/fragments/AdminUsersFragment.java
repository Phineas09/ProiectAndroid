package ro.mta.proiect.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ro.mta.proiect.R;
import ro.mta.proiect.tables.UserDetails;
import ro.mta.proiect.tables.Users;

public class AdminUsersFragment extends Fragment {

    SearchView searchView;
    ListView listView;
    ArrayAdapter<Users> arrayAdapter;
    List<Users> usersList;

    public AdminUsersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_users, container, false);
    }

    private String getUserId(Users selectedUser) {
        for (Map.Entry<String, Users> stringUserDetailsEntry : Users.getAllUsers().entrySet()) {
            Map.Entry pair = (Map.Entry)stringUserDetailsEntry;
            Users currentUser = (Users)pair.getValue();
            if(currentUser.getEmail().equals(selectedUser.getEmail())) {
                return (String)pair.getKey();
            }
        }
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = getView().findViewById(R.id.fragment_admin_users_search_view);
        listView = getView().findViewById(R.id.fragment_admin_users_list);

        /**
         * Get an array of all users
         */

        usersList = new ArrayList<>();

        for (Map.Entry<String, Users> stringUserDetailsEntry : Users.getAllUsers().entrySet()) {
            Map.Entry pair = (Map.Entry)stringUserDetailsEntry;
            usersList.add(((Users)pair.getValue()));
        }

        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, usersList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * Change frame
                 *
                 *
                 */
                Users selectedUser = (Users)parent.getItemAtPosition(position);
                /*Find userId
                 */
                String selectedUserId = getUserId(selectedUser);
                UserDetails.getUserDetailsById(selectedUserId);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.main_fragment_container,
                                new ProfileFragment(selectedUser, selectedUserId, UserDetails.getUserDetailsById(selectedUserId), true))
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AdminUsersFragment.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AdminUsersFragment.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });



    }

}
