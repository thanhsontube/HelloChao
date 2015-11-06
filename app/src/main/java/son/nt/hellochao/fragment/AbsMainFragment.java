package son.nt.hellochao.fragment;

import son.nt.hellochao.base.AFragment;

/**
 * Created by Sonnt on 11/6/15.
 */
public abstract class AbsMainFragment<T> extends AFragment {
    abstract void updateData (T data);
}
