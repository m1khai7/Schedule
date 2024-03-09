package myapp.schedule.misha.myapplication.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import myapp.schedule.misha.myapplication.R;
import myapp.schedule.misha.myapplication.common.core.BaseActivity;
import myapp.schedule.misha.myapplication.data.database.DatabaseHelper;
import myapp.schedule.misha.myapplication.data.database.dao.CallDao;
import myapp.schedule.misha.myapplication.module.calls.CallsFragment;
import myapp.schedule.misha.myapplication.module.editData.EditDataFragment;
import myapp.schedule.misha.myapplication.module.exploreList.ScheduleListFragment;
import myapp.schedule.misha.myapplication.module.schedule.exploreDays.MainFragment;
import myapp.schedule.misha.myapplication.module.settings.SettingsContainer;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
		bottomNavigationView.setOnNavigationItemSelectedListener(this);
		bottomNavigationView.setSelectedItemId(R.id.scheduleDays);

		try (DatabaseHelper databaseHelper = new DatabaseHelper(this)) {
			databaseHelper.getWritableDatabase();
			CallDao.getInstance().initTable();
		} catch (Exception exception) {
			showSnack(R.string.error_load_database);
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		int id = menuItem.getItemId();
		if (id == R.id.calls)
			replaceFragment(new CallsFragment());
		else if (id == R.id.editData)
			replaceFragment(new EditDataFragment());
		else if (id == R.id.scheduleDays)
			replaceFragment(new MainFragment());
		else if (id == R.id.scheduleList)
			replaceFragment(new ScheduleListFragment());
		else if (id == R.id.settingsMenu)
			replaceFragment(new SettingsContainer());
		return true;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

}