package happybaby.pics.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import happybaby.pics.R;


public abstract class BaseFragment extends Fragment implements IActivityHelper {
    private View containerView;
    private boolean isFirst = true;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (containerView == null) {
            if (IGetContentView() != null) {
                containerView = IGetContentView();
            } else if (IGetContentViewResId() != 0) {
                containerView = inflater.inflate(IGetContentViewResId(), container, false);
            }
        }
        ViewGroup parent = (ViewGroup) containerView.getParent();
        if (parent != null) {
            parent.removeView(containerView);
        }
        if (isFirst) {
            View view = containerView.findViewById(R.id.toolbar);
            View title = containerView.findViewById(R.id.toolbarTitle);
            if (view != null) {
                toolbar = (Toolbar) view;
            }
            if (title != null) {
                toolbarTitle = (TextView) title;
            }
            IFindViews(containerView);
            IInitData();
            IRequest();
            isFirst = false;
        }
        return containerView;
    }

    @Override
    public Context IGetContext() {
        return getActivity();
    }

    @Override
    public Activity IGetActivity() {
        return getActivity();
    }

    @Override
    public abstract int IGetContentViewResId();

    @Override
    public View IGetContentView() {
        return null;
    }

    @Override
    public void IFindViews() {

    }

    @Override
    public abstract void IFindViews(View view);

    @Override
    public abstract void IInitData();

    @Override
    public void IRequest() {

    }

    public View getContainerView() {
        return containerView;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public TextView getToolbarTitle() {
        return toolbarTitle;
    }
}