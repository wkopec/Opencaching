package com.example.opencaching.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Wojtek on 27.07.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Item> items;

    public SectionsPagerAdapter(FragmentManager fm, ArrayList<Item> items) {
        super(fm);
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).getTitle();
    }

    public static class Item {
        private Fragment fragment;
        private String title;

        public Item(String title, Fragment fragment) {
            this.fragment = fragment;
            this.title = title;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public String getTitle() {
            return title;
        }
    }
}
