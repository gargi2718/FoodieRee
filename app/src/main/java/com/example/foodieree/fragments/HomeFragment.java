package com.example.foodieree.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodieree.R;
import com.example.foodieree.adapters.ReelAdapter;
import com.example.foodieree.models.Reel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPagerReels;
    private ReelAdapter reelAdapter;
    private final List<Reel> reelsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPagerReels = view.findViewById(R.id.viewPagerReels);
        setupReels();
        return view;
    }

    private void setupReels() {
        reelAdapter = new ReelAdapter(requireContext(), reelsList, getLifecycle());
        viewPagerReels.setAdapter(reelAdapter);
        viewPagerReels.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        loadDummyReels();

        viewPagerReels.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                reelAdapter.setCurrentPlayingPosition(position);
            }
        });

        // Start first video
        viewPagerReels.post(() -> reelAdapter.setCurrentPlayingPosition(0));
    }

    private void loadDummyReels() {
        reelsList.add(new Reel("1", "https://www.youtube.com/shorts/Pq0uPdL-jR4", "Sizzling Brownie", "Brownie Cafe", "1.0 km"));
        reelsList.add(new Reel("2", "https://www.youtube.com/watch?v=c2AVUQVmenw", "Delicious Pizza", "Pizza Hut", "1.5 km"));
        reelsList.add(new Reel("3", "https://www.youtube.com/watch?v=w2C2JMYio30", "Wood Fired Pizza", "Brick Oven", "1.2 km"));

        reelAdapter.notifyDataSetChanged();
    }
}



/*
    private void loadDummyReels() {
        reelsList.add(new Reel(
                "1",
                "https://www.youtube.com/watch?v=c2AVUQVmenw",
                "Delicious Pizza",
                "Pizza Hut",
                "1.5 km"
        ));
        reelsList.add(new Reel(
                "2",
                "https://www.youtube.com/watch?v=w2C2JMYio30",
                "Wood Fired Pizza",
                "Brick Oven",
                "1.2 km"
        ));
        reelsList.add(new Reel(
                "3",
                "https://www.youtube.com/shorts/Pq0uPdL-jR4",
                "Brownies Like No Other",
                "Mongolina",
                "1.9 km"
        ));

package com.example.foodieree.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.foodieree.R;
import com.example.foodieree.adapters.SimpleReelAdapter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ViewPager2 viewPager;
    private SimpleReelAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        setupViewPager();

        return view;
    }

    private void setupViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("Food Item 1");
        titles.add("Food Item 2");
        titles.add("Food Item 3");

        adapter = new SimpleReelAdapter(titles);
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
    }
}

/*
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ViewPager2 reelsViewPager;
    private ReelAdapter reelAdapter;
    private List<Reel> reelsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            initializeViews(view);
            return view;
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: ", e);
            return null;
        }
    }

    private void initializeViews(View view) {
        try {
            reelsViewPager = view.findViewById(R.id.viewPagerReels);
            setupReelsViewPager();
            loadDummyReels();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: ", e);
        }
    }

    private void setupReelsViewPager() {
        try {
            reelsList = new ArrayList<>();
            reelAdapter = new ReelAdapter(requireContext(), reelsList);
            reelsViewPager.setAdapter(reelAdapter);
            reelsViewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up ViewPager: ", e);
        }
    }

    private void loadDummyReels() {
        try {
            reelsList.add(new Reel(
                    "1",
                    "https://www.youtube.com/watch?v=c2AVUQVmenw",
                    "Delicious Pizza",
                    "Pizza Hut",
                    "1.5 km"
            ));
            reelsList.add(new Reel(
                    "1",
                    "https://www.youtube.com/watch?v=w2C2JMYio30",
                    "Wood Fired Pizza",
                    "Brick Oven",
                    "1.2 km"
            ));
            reelsList.add(new Reel(
                    "1",
                    "https://www.youtube.com/shorts/Pq0uPdL-jR4",
                    "Brownies Like No Ever",
                    "Mongolina",
                    "1.9 km"
            ));
            // Add more dummy data
            reelAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "Error loading dummy data: ", e);
        }
    }


    @Override
    public void onDestroyView() {
        try {
            if (reelAdapter != null) {
                reelAdapter.releaseAllPlayers();
            }
            super.onDestroyView();
        } catch (Exception e) {
            Log.e(TAG, "Error in onDestroyView: ", e);
            super.onDestroyView();
        }
    }


}
*/