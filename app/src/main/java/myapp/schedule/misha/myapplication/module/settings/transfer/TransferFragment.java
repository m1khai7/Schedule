package myapp.schedule.misha.myapplication.module.settings.transfer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import myapp.schedule.misha.myapplication.R;
import myapp.schedule.misha.myapplication.common.core.BaseMainFragment;
import myapp.schedule.misha.myapplication.common.core.BasePresenter;
import myapp.schedule.misha.myapplication.data.preferences.Preferences;

import static myapp.schedule.misha.myapplication.data.preferences.Preferences.DARK_THEME;
import static myapp.schedule.misha.myapplication.data.preferences.Preferences.LIGHT_THEME;

public class TransferFragment extends BaseMainFragment implements TransferFragmentView {

	private final ActivityResultLauncher<String> getFileLauncher =
			registerForActivityResult(
					new ActivityResultContracts.GetContent(),
					this::fileSelect
			);

	private final ActivityResultLauncher<Intent> saveFileLauncher =
			registerForActivityResult(
					new ActivityResultContracts.StartActivityForResult(),
					this::handleResultSavedFile
			);

	private TransferPresenter presenter;

	public static TransferFragment newInstance() {
		return new TransferFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		presenter = new TransferPresenter(getContext());
	}

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_transfer_data, container, false);
		TextView shareTv = view.findViewById(R.id.shareTv);
		TextView receiveTv = view.findViewById(R.id.receiveTv);
		TextView saveTv = view.findViewById(R.id.saveTv);
		if (Preferences.getInstance().getSelectedTheme().equals(DARK_THEME)) {
			shareTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unarchive_white, 0, 0, 0);
			receiveTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_archive_white, 0, 0, 0);
			saveTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save_white, 0, 0, 0);
		} else if (Preferences.getInstance().getSelectedTheme().equals(LIGHT_THEME)) {
			shareTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unarchive_black, 0, 0, 0);
			receiveTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_archive_black, 0, 0, 0);
			saveTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save_black, 0, 0, 0);
		}

		shareTv.setOnClickListener(v -> presenter.onClickShareFile());
		receiveTv.setOnClickListener(v -> presenter.onClickReceiveFile());
		saveTv.setOnClickListener(v -> presenter.onClickSaveFile());

		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		showIcon();
		getContext().setCurrentTitle(R.string.title_transfer_data);
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
			presenter.onInfoMenuClick();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void selectFile() {
		getFileLauncher.launch("*/*");
	}

	@Override
	public void saveFile(String fileName) {
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("application/json");
		intent.putExtra(Intent.EXTRA_TITLE, fileName);
		saveFileLauncher.launch(intent);
	}

	@Override
	public void shareFile(Uri uri) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("application/json");
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		startActivity(Intent.createChooser(intent, getString(R.string.title_share_data)));
	}

	@NonNull
	@Override
	protected BasePresenter getPresenter() {
		return presenter;
	}

	private void fileSelect(Uri uri) {
		if (uri != null) {
			presenter.parseFile(uri);
		}
	}

	public void handleResultSavedFile(ActivityResult activityResult) {
		if (activityResult.getResultCode() == Activity.RESULT_CANCELED) return;
		if (activityResult.getResultCode() == Activity.RESULT_OK) {
			if (activityResult.getData() != null) {
				Uri uri = activityResult.getData().getData();
				presenter.saveFileByUri(uri);
			} else {
				showSnack(R.string.error_save_file);
			}
		} else {
			showSnack(R.string.error_save_file);
		}
	}
}
