package myapp.schedule.misha.myapplication.module.calls;

import static myapp.schedule.misha.myapplication.data.preferences.Preferences.DARK_THEME;
import static myapp.schedule.misha.myapplication.data.preferences.Preferences.LIGHT_THEME;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import myapp.schedule.misha.myapplication.R;
import myapp.schedule.misha.myapplication.common.core.BaseMainFragment;
import myapp.schedule.misha.myapplication.common.core.BasePresenter;
import myapp.schedule.misha.myapplication.data.preferences.Preferences;
import myapp.schedule.misha.myapplication.entity.Calls;
import myapp.schedule.misha.myapplication.util.DataUtil;


public class CallsFragment extends BaseMainFragment implements CallsFragmentView {


    private CallsFragmentAdapter callsAdapter;
    private CallsPresenter presenter;

    @Override
    public void onResume() {
        super.onResume();
        hideToolbarIcon();
        getContext().setCurrentTitle(getString(R.string.title_calls));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CallsPresenter(getContext());
        callsAdapter = new CallsFragmentAdapter(presenter);
        DataUtil.hintKeyboard(getContext());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        RecyclerView rvCalls = view.findViewById(R.id.rv_groups);
        rvCalls.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCalls.setAdapter(callsAdapter);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
        if (Preferences.getInstance().getSelectedTheme().equals(DARK_THEME)) {
            menu.findItem(R.id.btn_info).setIcon(R.drawable.ic_info_white);
        }
        if (Preferences.getInstance().getSelectedTheme().equals(LIGHT_THEME)) {
            menu.findItem(R.id.btn_info).setIcon(R.drawable.ic_info_black);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.btn_info) {
            showSnack(R.string.calls_info_snack);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }


    public void updateView(ArrayList<Calls> callsList) {
        callsAdapter.setCallsList(callsList);
        callsAdapter.notifyDataSetChanged();
    }
}
