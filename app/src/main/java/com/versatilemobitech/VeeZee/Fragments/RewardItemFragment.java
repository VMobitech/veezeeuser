package com.versatilemobitech.VeeZee.Fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Activities.DetailsActivity;
import com.versatilemobitech.VeeZee.Model.Rewards;
import com.versatilemobitech.VeeZee.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RewardItemFragment extends Fragment implements View.OnClickListener {


    View view;
    ImageView image_view_main, image_view_sub, image_view_food, image_view_shop,
            image_view_movie, image_view_run, image_view_travel, image_view_infinity;
    TextView text_view_redeem, text_view_details, text_view_title, text_view_header_title, text_view_description, text_view_log_title;

    Rewards rewards;


    public RewardItemFragment() {
        // Required empty public constructor
    }

    public static RewardItemFragment newInstance(Rewards rewards) {
        RewardItemFragment rewardItemFragment = new RewardItemFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("rewards", rewards);


        rewardItemFragment.setArguments(bundle);

        return rewardItemFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.item_rewards, container, false);

        initView(view);


        return view;
    }

    private void initView(View view) {
        image_view_main = view.findViewById(R.id.image_view_main);
        image_view_sub = view.findViewById(R.id.image_view_sub);
        image_view_food = view.findViewById(R.id.image_view_food);
        image_view_shop = view.findViewById(R.id.image_view_shop);
        image_view_movie = view.findViewById(R.id.image_view_movie);
        image_view_run = view.findViewById(R.id.image_view_run);
        image_view_travel = view.findViewById(R.id.image_view_travel);
        image_view_infinity = view.findViewById(R.id.image_view_infinity);

        text_view_redeem = view.findViewById(R.id.text_view_redeem);
        text_view_details = view.findViewById(R.id.text_view_details);
        text_view_title = view.findViewById(R.id.text_view_title);
        text_view_header_title = view.findViewById(R.id.text_view_header_title);
        text_view_description = view.findViewById(R.id.text_view_title);
        text_view_log_title = view.findViewById(R.id.text_view_log_title);

        text_view_redeem.setOnClickListener(this);
        text_view_details.setOnClickListener(this);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        rewards = bundle.getParcelable("rewards");
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        rewards = bundle.getParcelable("rewards");
        if (rewards != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                text_view_description.setText(Html.fromHtml(rewards.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                text_view_description.setText(Html.fromHtml(rewards.getDescription()));
            }
            Picasso.get().load(rewards.getImage()).placeholder(R.drawable.valet_history_placeholder).into(image_view_main);
            Picasso.get().load(rewards.getLogo()).placeholder(R.drawable.rewards_brand_placeholder).into(image_view_sub);

            text_view_title.setText(rewards.getDescription());
            text_view_header_title.setText(rewards.getTitle());
            text_view_log_title.setText(rewards.getBrand());

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_redeem:
                BottomSheetFragment addPhotoBottomDialogFragment = BottomSheetFragment.newInstance(rewards);
                addPhotoBottomDialogFragment.show(getChildFragmentManager(),
                        "add_photo_dialog_fragment");
                break;
            case R.id.text_view_details:
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("rewards", rewards);
                startActivity(intent);
                break;
        }
    }
}
