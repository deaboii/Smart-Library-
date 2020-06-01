package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter
{


    public TabsAccessorAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i)
    {
        switch (i)
        {
            case 0:
                IssuedFragment issuedFragment =new IssuedFragment();
                return issuedFragment;
            case 1:
                RefrenceFragment refrenceFragment =new RefrenceFragment();
                return refrenceFragment;
            default:
                return null;
        }


    }

    @Override
    public int getCount()
    {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Issued";

            case 1:
                return "Refrence";

            default:
                return null;
        }
    }
}
