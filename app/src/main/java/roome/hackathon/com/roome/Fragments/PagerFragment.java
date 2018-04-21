package roome.hackathon.com.roome.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import roome.hackathon.com.roome.Models.Medium;
import roome.hackathon.com.roome.R;


public class PagerFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private SimpleDraweeView img;
    private View pagerFragment;
    private FragmentTransaction mFragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        Bundle args = getArguments();
        final ArrayList<Medium> list = args.getParcelableArrayList("arrSlider");
        final int position = args.getInt(ARG_OBJECT);
        img = (SimpleDraweeView) view.findViewById(R.id.imge);
        Uri uri = Uri.parse(list.get(position).getUrl());
        img.setImageURI(uri);
        pagerFragment = view.findViewById(R.id.pagerFragment);
        pagerFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;

    }
}