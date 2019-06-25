package pl.wat.tim.mobile.view;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

public interface FragmentProvider {

    <T extends Fragment> void showFragment(T fragment,@IdRes int containerViewId);
}
