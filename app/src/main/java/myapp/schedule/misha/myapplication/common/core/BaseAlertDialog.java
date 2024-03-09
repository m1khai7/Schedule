package myapp.schedule.misha.myapplication.common.core;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import myapp.schedule.misha.myapplication.R;
import myapp.schedule.misha.myapplication.ScheduleApp;
import myapp.schedule.misha.myapplication.common.core.snack.SnackBehavior;
import myapp.schedule.misha.myapplication.common.core.snack.SnackBehaviorInterface;
import myapp.schedule.misha.myapplication.module.schedule.exploreDays.MainFragment;

public abstract class BaseAlertDialog extends BaseDialog implements BaseDialogView {

	private SnackBehaviorInterface snackBehavior;

	@Override
	public void showError(@StringRes int message) {
		ScheduleApp.showToast(message);
	}

	@Override
	public void showError(String message) {
		ScheduleApp.showToast(message);
	}

	@Override
	public void showGlobalError(@StringRes int message) {
		// do nothing
	}


	@Override
	public void showGlobalError(String message) {
		//do nothing
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		snackBehavior = new SnackBehavior(getView());
		getPresenter().setRoot(getContext());
		getPresenter().setView(this);
	}

	@Override
	public void replaceFragment(Fragment fragment) {
		replaceFragment(fragment, true);
	}

	@Override
	public void replaceFragment(Fragment fragment, boolean saveBackStack) {
		replaceFragment(fragment, true, saveBackStack);
	}

	@Override
	public void replaceFragment(Fragment fragment, boolean animate, boolean saveBackStack) {
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		if (getChildFragmentManager().getBackStackEntryCount() >= 1 && animate) {
			transaction.setCustomAnimations(R.anim.tr_child_up, R.anim.tr_exit_left,
					R.anim.tr_parent_back, R.anim.tr_child_back);
		}
		String name = null;
		if (saveBackStack) {
			name = fragment.getClass().getName();
		}
		transaction.replace(R.id.fragment_container, fragment, name)
				.addToBackStack(name)
				.commitAllowingStateLoss();
	}

	@Override
	public void navigateToMain() {
		Fragment newFragment = MainFragment.newInstance();
		FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void showSnack(String message) {
		snackBehavior.showSnack(message);
	}

	@Override
	public void showSnack(String message, String button, View.OnClickListener callback) {
		snackBehavior.showSnack(message, button, callback);
	}

	@Override
	public void showSnack(@StringRes int message) {
		snackBehavior.showSnack(message);
	}


	@Override
	public void closeDialog() {
		dismiss();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getPresenter().setRoot(null);
		getPresenter().getCompositeDisposable().dispose();
	}

	@Override
	public void onStart() {
		super.onStart();
		getPresenter().onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		getPresenter().onStop();

	}

	@NonNull
	protected abstract BasePresenter getPresenter();
}
