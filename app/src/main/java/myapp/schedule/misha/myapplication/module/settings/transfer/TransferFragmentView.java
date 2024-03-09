package myapp.schedule.misha.myapplication.module.settings.transfer;

import android.net.Uri;

import myapp.schedule.misha.myapplication.common.core.BaseView;

public interface TransferFragmentView extends BaseView {

	void shareFile(Uri uri);

	void selectFile();

	void saveFile(String fileName);
}
