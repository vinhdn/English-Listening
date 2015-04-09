package me.vinhdo.effortless.english.Adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import me.vinhdo.effortless.english.Fragments.ContentLessionFragment;
import me.vinhdo.effortless.english.Fragments.ListLessionFragment;
import me.vinhdo.effortless.english.Services.PlayerService;

/**
 * Created by Vinh on 1/9/15.
 */
public class PlayerViewAdapter extends FragmentPagerAdapter {

    PlayerService service;

    public PlayerViewAdapter(FragmentManager fm,PlayerService service) {
        super(fm);
        this.service = service;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ContentLessionFragment.create(service);
            case 1:
                return ListLessionFragment.create(service);
            default:
                return ListLessionFragment.create(service);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
