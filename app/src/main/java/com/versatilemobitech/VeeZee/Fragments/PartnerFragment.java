package com.versatilemobitech.VeeZee.Fragments;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.versatilemobitech.VeeZee.Activities.MainActivity;
import com.versatilemobitech.VeeZee.Adapters.PartnerTabAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.PartnerCategories;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartnerFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener, IParseListener {
    ViewPager pager_partners;
    TabLayout tabLayout;
    private View view;
    ImageView img_user_menu;
    ImageView img_credits;
    TextView toolBarTitle, txt_share, txt_credits, text_view_v;
    Toolbar toolbar;
    PartnerTabAdapter adapter;
    SearchView searchView = null;
    ImageView search;
    String TAG = "";

    private SearchView.OnQueryTextListener queryTextListener;
    ArrayList<PartnerCategories> partnerCategoriesArrayList = new ArrayList<>();

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public PartnerFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_partner, container, false);
        initComponents();
        Toolbar toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolBar);
        toolbar.setVisibility(View.GONE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    private void initComponents() {
        setReferences();
        setClickListeners();
        TAG = MainActivity.ClickToNavigate;
        callServiceForPartnerCategories();


    }

    @Override
    public void onStart() {
        super.onStart();
          //  EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {

     //   EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void callServiceForPartnerCategories() {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.PARTENR_CATEGORY_URL, new JSONObject(), new HashMap<String, String>(), this, Constants.PARTENR_CATEGORY_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }


    }

    private void setClickListeners() {
        search.setOnClickListener(this);
    }

    private void setTabs() {


        for (int i = 0; i < partnerCategoriesArrayList.size(); i++) {
            PartnerCategories partnerCategories = partnerCategoriesArrayList.get(i);

            tabLayout.addTab(tabLayout.newTab().setText(partnerCategories.getName()));
        }
        for (int k = 0; k < partnerCategoriesArrayList.size(); k++) {
            PartnerCategories partnerCategories = partnerCategoriesArrayList.get(k);

            mFragmentList.add(new ClubsFragment().newInstance(Integer.parseInt(partnerCategories.getCategory_id()),k,TAG));
        }

        setAdapter();
        tabLayout.addOnTabSelectedListener(PartnerFragment.this);
        tabLayout.setupWithViewPager(pager_partners);
    }

    private void setAdapter() {
        adapter = new PartnerTabAdapter(PartnerFragment.this, getChildFragmentManager(), tabLayout.getTabCount(), partnerCategoriesArrayList, mFragmentList);
        pager_partners.setAdapter(adapter);

        pager_partners.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    private void setReferences() {
        pager_partners = view.findViewById(R.id.pager_partners);
        tabLayout = view.findViewById(R.id.tabLayout);
        img_user_menu = ((MainActivity) getActivity()).findViewById(R.id.img_user_menu);
        img_credits = ((MainActivity) getActivity()).findViewById(R.id.img_credits);
        toolBarTitle = ((MainActivity) getActivity()).findViewById(R.id.toolBarTitle);
        txt_share = ((MainActivity) getActivity()).findViewById(R.id.txt_share);
        txt_credits = ((MainActivity) getActivity()).findViewById(R.id.txt_credits);
        text_view_v = ((MainActivity) getActivity()).findViewById(R.id.text_view_credits_v);
        searchView = ((MainActivity) getActivity()).findViewById(R.id.search_view);

        search = view.findViewById(R.id.search);

        txt_credits.setVisibility(View.GONE);
        img_credits.setVisibility(View.GONE);
        img_user_menu.setVisibility(View.GONE);
        txt_share.setVisibility(View.GONE);
        text_view_v.setVisibility(View.GONE);


        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("Partners");
        toolBarTitle.setTextColor(getResources().getColor(R.color.black));
        toolBarTitle.setGravity(Gravity.LEFT);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pager_partners.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_credits:
                //searchView.setVisibility(View.VISIBLE);
                break;
            case R.id.search :
                EventBus.getDefault().post(String.valueOf(pager_partners.getCurrentItem()));
                break;

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ErrorResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.something_went_wrong), null);
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        switch (requestCode) {
            case Constants.PARTENR_CATEGORY_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        partnerCategoriesArrayList.clear();
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                                String category_id = jsonObject1.optString("category_id");
                                String name = jsonObject1.optString("name");

                                PartnerCategories partnerCategories = new PartnerCategories(category_id, name);
                                partnerCategoriesArrayList.add(partnerCategories);

                            }
                            setTabs();
                        }

                    }else if(status==401) {
                        PopUtils.alertDialog(getActivity(), message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}
