package myapp.schedule.misha.myapplication.module.schedule.edit.page;

import android.app.Dialog;

import java.util.ArrayList;

import myapp.schedule.misha.myapplication.entity.Lesson;

public interface EditSchedulePagePresenterInterface {

    void onSubjectClick(int position);

    void onAudienceClick(int position);

    void onEducatorClick(int position);

    void onTypelessonClick(int position);

    void onCopyUpClick(int position);

    void onCopyDownClick(int position);

    void onClearLessonClick(int position);

    void onCopyLessonOtherDay(int position);

    void onClearDayClick();

    void setWeek(int position);

    void onButtonClicked(int id);

    ArrayList<Lesson> getLessonList();

}