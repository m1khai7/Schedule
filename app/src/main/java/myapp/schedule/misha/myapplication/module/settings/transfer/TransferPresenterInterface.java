package myapp.schedule.misha.myapplication.module.settings.transfer;

import android.net.Uri;

public interface TransferPresenterInterface {

	void onClickShareFile();

	void onClickReceiveFile();

	void onClickSaveFile();

	void parseFile(Uri uri);

	void onInfoMenuClick();

	void saveFileByUri(Uri uri);
}
