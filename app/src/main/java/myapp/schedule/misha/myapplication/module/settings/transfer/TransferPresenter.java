package myapp.schedule.misha.myapplication.module.settings.transfer;

import static myapp.schedule.misha.myapplication.data.database.dao.LessonDao.ID;
import static myapp.schedule.misha.myapplication.data.database.dao.LessonDao.ID_AUDIENCE;
import static myapp.schedule.misha.myapplication.data.database.dao.LessonDao.ID_EDUCATOR;
import static myapp.schedule.misha.myapplication.data.database.dao.LessonDao.ID_SUBJECT;
import static myapp.schedule.misha.myapplication.data.database.dao.LessonDao.ID_TYPE_LESSON;
import static myapp.schedule.misha.myapplication.data.database.dao.LessonDao.NUMBER_DAY;
import static myapp.schedule.misha.myapplication.data.database.dao.LessonDao.NUMBER_LESSON;
import static myapp.schedule.misha.myapplication.data.database.dao.LessonDao.NUMBER_WEEK;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import myapp.schedule.misha.myapplication.NewFileProvider;
import myapp.schedule.misha.myapplication.R;
import myapp.schedule.misha.myapplication.ScheduleApp;
import myapp.schedule.misha.myapplication.common.core.BaseMainPresenter;
import myapp.schedule.misha.myapplication.data.database.AppContentProvider;
import myapp.schedule.misha.myapplication.data.database.DatabaseHelper;
import myapp.schedule.misha.myapplication.data.database.dao.AudienceDao;
import myapp.schedule.misha.myapplication.data.database.dao.CallDao;
import myapp.schedule.misha.myapplication.data.database.dao.EducatorDao;
import myapp.schedule.misha.myapplication.data.database.dao.LessonDao;
import myapp.schedule.misha.myapplication.data.database.dao.SubjectDao;
import myapp.schedule.misha.myapplication.data.database.dao.TypelessonDao;
import myapp.schedule.misha.myapplication.entity.CollectSchedule;
import myapp.schedule.misha.myapplication.entity.Lesson;
import myapp.schedule.misha.myapplication.entity.ParceSchedule;

public class TransferPresenter extends BaseMainPresenter<TransferFragmentView> implements TransferPresenterInterface {

	private final Context context;

	private static final String FILE_NAME = "schedule.json";

	private final ContentResolver contentResolver;

	public TransferPresenter(Context context) {
		this.context = context;
		contentResolver = context.getContentResolver();
	}

	@Override
	public void init() {
	}

	@Override
	public void onClickShareFile() {
		File scheduleJson = getScheduleFile();
		String pathName = getClassPath(NewFileProvider.class);
		getView().shareFile(NewFileProvider.getUriForFile(context, pathName, scheduleJson));
	}

	public static String getClassPath(Class<?> value) {
		if (value == null) {
			return null;
		}
		char[] temp = value.getName().toCharArray();
		Package path = value.getPackage();
		for (int i = path == null ? 0 : path.getName().length() + 1; i < temp.length; i++) {
			if (temp[i] == '.') {
				temp[i] = '$';
			}
		}
		return new String(temp);
	}

	@Override
	public void onClickReceiveFile() {
		getView().selectFile();
	}

	@Override
	public void onClickSaveFile() {
		getView().saveFile(FILE_NAME);
	}

	@Override
	public void parseFile(Uri uri) {
		InputStream inputStream = null;
		ParceSchedule jsonModelSchedule = null;
		try {
			inputStream = contentResolver.openInputStream(uri);
			if (inputStream != null) {
				int size = inputStream.available();
				byte[] buffer = new byte[size];
				inputStream.read(buffer);
				inputStream.close();
				String myJsonData = new String(buffer, StandardCharsets.UTF_8);
				Gson gson = new Gson();
				jsonModelSchedule = gson.fromJson(myJsonData, ParceSchedule.class);
			} else {
				getView().showSnack(R.string.error_load_schedule);
			}
		} catch (Exception e) {
			Log.e("error", e.toString());
			getView().showSnack(ScheduleApp.getStr(R.string.error_load_schedule));
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				} else {
					Log.e("error", "outputStream is null");
					getView().showSnack(ScheduleApp.getStr(R.string.error_load_schedule));
				}
			} catch (IOException exception) {
				Log.e("error", exception.toString());
				getView().showSnack(ScheduleApp.getStr(R.string.error_load_schedule));
			}
		}
		if (jsonModelSchedule != null) {
			updateDatabase(jsonModelSchedule);
		}
	}

	@Override
	public void onInfoMenuClick() {
		getView().showSnack(ScheduleApp.getStr(R.string.transfer_info_snackbar),
				ScheduleApp.getStr(android.R.string.ok), null);
	}

	@Override
	public void saveFileByUri(Uri uri) {
		OutputStream outputStream = null;
		try {
			outputStream = contentResolver.openOutputStream(uri);
			if (outputStream != null) {
				outputStream.write(getScheduleJsonByteArray());
			}
		} catch (Exception e) {
			Log.e("error", e.toString());
			getView().showSnack(ScheduleApp.getStr(R.string.error_parse_share));
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
					getView().showSnack(R.string.success_save_file);
				} else {
					Log.e("error", "outputStream is null");
					getView().showSnack(ScheduleApp.getStr(R.string.error_parse_share));
				}
			} catch (IOException exception) {
				Log.e("error", exception.toString());
				getView().showSnack(ScheduleApp.getStr(R.string.error_parse_share));
			}
		}
	}

	private File getScheduleFile() {
		final File directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
		final File file = new File(directory, FILE_NAME);
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			outputStream.write(getScheduleJsonByteArray());
		} catch (Exception e) {
			Log.e("error", e.toString());
			getView().showSnack(ScheduleApp.getStr(R.string.error_parse_share));
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				} else {
					Log.e("error", "outputStream is null");
					getView().showSnack(ScheduleApp.getStr(R.string.error_parse_share));
				}
			} catch (IOException exception) {
				Log.e("error", exception.toString());
				getView().showSnack(ScheduleApp.getStr(R.string.error_parse_share));
			}
		}
		return file;
	}

	private void updateDatabase(ParceSchedule modelSchedule) {
		CallDao.getInstance().deleteAll();
		CallDao.getInstance().insertAll(modelSchedule.getCalls());
		SubjectDao.getInstance().deleteAll();
		SubjectDao.getInstance().insertAll(modelSchedule.getSubjects());
		AudienceDao.getInstance().deleteAll();
		AudienceDao.getInstance().insertAll(modelSchedule.getAudiences());
		EducatorDao.getInstance().deleteAll();
		EducatorDao.getInstance().insertAll(modelSchedule.getEducators());
		TypelessonDao.getInstance().deleteAll();
		TypelessonDao.getInstance().insertAll(modelSchedule.getTypelessons());
		LessonDao.getInstance().deleteAll();

		SQLiteDatabase database;
		try (DatabaseHelper databaseHelper = new DatabaseHelper(context)) {
			database = databaseHelper.getWritableDatabase();
			database.beginTransaction();
			for (int i = 0; i < 612; i++) {
				Lesson lesson = modelSchedule.getLessons().get(i);
				ContentValues set = new ContentValues();
				set.put(ID, i + 1);
				set.put(NUMBER_WEEK, lesson.getNumber_week());
				set.put(NUMBER_DAY, lesson.getNumber_day());
				set.put(NUMBER_LESSON, lesson.getNumber_lesson());
				set.put(ID_SUBJECT, lesson.getId_subject());
				set.put(ID_AUDIENCE, lesson.getId_audience());
				set.put(ID_EDUCATOR, lesson.getId_educator());
				set.put(ID_TYPE_LESSON, lesson.getId_typelesson());
				database.insert(AppContentProvider.LESSONS_TABLE, null, set);
			}
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception e) {
			getView().showSnack(R.string.error_load_schedule);
		} finally {
			getView().showSnack(R.string.success_load_schedule);
			getView().navigateToMain();
		}
	}

	private byte[] getScheduleJsonByteArray() {
		final Gson gsonBuilder = new GsonBuilder().create();
		return gsonBuilder.toJson(new CollectSchedule()).getBytes();
	}
}