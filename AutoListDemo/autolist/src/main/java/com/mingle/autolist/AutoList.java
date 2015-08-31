package com.mingle.autolist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;

import com.mingle.utils.BusProvider;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;


public class AutoList<T extends Object> extends ArrayList<T> implements DataObserver{

    public static final String  Tag="AutoList";


    private BaseAdapter mAdapter;
    private RecyclerView.Adapter mRvAdapter;
    private ActionHandler mActionHandler;


    private void register() {

        Log.e(Tag,"register  Bus");
        BusProvider.getInstance().register(this);
    }

    private void unRegister() {
        Log.e(Tag,"unRegister Bus");
        BusProvider.getInstance().unregister(this);
    }

    public void setup(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().add(new DataFragment(),
                this.toString()).commitAllowingStateLoss();
    }

    public void setup(FragmentActivity activity) {

        setup(activity.getSupportFragmentManager());
    }


    public void setup(Fragment fragment) {

        if(fragment instanceof  DataObserverEnable){
            ((DataObserverEnable) fragment).addDataObserver(this);

        }else{
            Log.e(Tag," your Fragment must implement DataObserverEnable");
        }
    }


    @Subscribe
    public void action(T o) {


        if (o instanceof AutoData) {
            if (TextUtils.isEmpty(((AutoData) o).getIdentifies())) {
                return;
            }
            AutoData autoData = (AutoData) o;

            boolean handled = false;
            if (mActionHandler != null) {
                handled = mActionHandler.handleAction(o);
            }

            if (!handled) {
                switch (autoData.action) {
                    case Add:
                        addT(autoData);
                        break;
                    case Delete:
                        deleteT(autoData);
                        break;
                    case Update:
                        updateT(autoData);
                        break;
                    case Custom:
                        break;
                }
            }

            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
            if (mRvAdapter != null) {
                mRvAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRegister() {
        register();
    }

    @Override
    public void onUnRegister() {
        unRegister();

    }




    public interface ActionHandler<T> {
        boolean handleAction(T a);

    }

    public void setActionHandler(ActionHandler<T> actionHandler) {

        mActionHandler = actionHandler;
    }


    private void updateT(AutoData o) {


        int index = indexOf(o);
        if (index != -1) {
            this.remove(index);
            this.add(index, (T) o);
        }


    }

    private void deleteT(AutoData o) {

        int index = indexOf(o);
        if (index != -1) {
            this.remove(index);
        }

    }

    private void addT(AutoData o) {

        if (!contains(o)) {
            this.add(0, (T) o);
        }

    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
    }

    public void setRVAdapter(RecyclerView.Adapter rvAdapter) {
        mRvAdapter = rvAdapter;
    }


    @SuppressLint("ValidFragment")
    class DataFragment extends Fragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            register();

        }

        @Override
        public void onDestroy() {
            unRegister();
            super.onDestroy();
        }

    }
}