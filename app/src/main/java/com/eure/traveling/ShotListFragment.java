package com.eure.traveling;

import com.eure.traveling.entity.Shot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;


public class ShotListFragment extends ListFragment implements AbsListView.OnScrollListener {

    public static final String TAG = ShotListFragment.class.getSimpleName();

    /**
     * ページングのMAX値
     */
    private final static int MAX_COUNT = 50;
    /**
     * 現在のページ数
     */
    private int mCount = 1;

    private String mTypeName;

    private ShotListAdapter mAdapter;
    private ListView mListView;
    private View mFooter;

    private Realm mRealm;

    public ShotListFragment() {
    }

    public static ShotListFragment newInstance(String typeName) {
        ShotListFragment fragment = new ShotListFragment();
        Bundle args = new Bundle();
        args.putString("typeName", typeName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), DribbbleService.class);
        intent.putExtra("typeName", getType());
        intent.putExtra("page", mCount);
        getActivity().startService(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView = getListView();
        mListView.addFooterView(getFooter());
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRealm = Realm.getDefaultInstance();
        RealmResults<Shot> shots = mRealm.where(Shot.class)
                                .equalTo("type", getType())
                                .findAll();
        addListData(shots);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((RealmBaseAdapter<?>)getListAdapter()).updateRealmResults(null);
        mRealm.close();
        mRealm = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i(TAG, "onListItemClick:");
        Log.i(TAG, "position = " + position);
        Log.i(TAG, "id = " + id);
        super.onListItemClick(l, v, position, id);
        DetailDialogFragment detailDialogFragment = DetailDialogFragment.newInstance(mAdapter.getItem(position).getImage().getNormal());
        detailDialogFragment.show(getFragmentManager(), TAG);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // コンテンツが追加される前に呼ばれることもある
        if (mAdapter == null) {
            return;
        }

        // 最後尾までスクロールした場合
        if (totalItemCount == firstVisibleItem + visibleItemCount) {
            additionalReading();
        }
    }


    private String getType() {
        if (mTypeName == null) {
            mTypeName = getArguments().getString("typeName");
        }
        return mTypeName;
    }

    private ListView getMyListView() {
        if (mListView == null) {
            mListView = getListView();
        }
        return mListView;
    }

    private View getFooter() {
        if (mFooter == null) {
            mFooter = getLayoutInflater(getArguments()).inflate(R.layout.listview_footer, null);
        }
        return mFooter;
    }

    private void visibleFooter() {
        getMyListView().addFooterView(getFooter());
    }

    private void invisibleFooter() {
        getMyListView().removeFooterView(getFooter());
    }

    private void addListData(RealmResults<Shot> shots) {
        if (mAdapter == null) {
            mAdapter = new ShotListAdapter(getActivity(), shots, true);
            setListAdapter(mAdapter);
        } else {
            mAdapter.add(shots);
        }
    }

    private void additionalReading() {
        if (mCount >= MAX_COUNT) {
            invisibleFooter();
            return;
        }
        mCount++;
        Intent intent = new Intent(getActivity(), DribbbleService.class);
        intent.putExtra("typeName", getType());
        intent.putExtra("page", mCount);
        getActivity().startService(intent);
    }

}
