package com.versatilemobitech.VeeZee.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.versatilemobitech.VeeZee.Adapters.FaqAdapter;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FaqReferralFragment extends Fragment {
    private View view;
    RecyclerView rv_faq;
    FaqAdapter mFaqAdapter;

    public FaqReferralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_faq_referral, container, false);
        init();
        return view;
    }
    private void init() {

        rv_faq = view.findViewById(R.id.rv_faq);
        setAdapter();

    }

    private void setAdapter() {
        rv_faq.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        ArrayList<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        mFaqAdapter = new FaqAdapter(getActivity(),list);
        rv_faq.setAdapter(mFaqAdapter);

    }
}
