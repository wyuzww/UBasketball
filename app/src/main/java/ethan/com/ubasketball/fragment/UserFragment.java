package ethan.com.ubasketball.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ethan.com.ubasketball.LoginByPasswordActivity;
import ethan.com.ubasketball.MainActivity;
import ethan.com.ubasketball.R;

public class UserFragment extends Fragment implements View.OnClickListener {
    private TextView userTologin;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userTologin = (TextView) getActivity().findViewById(R.id.user_id);
        userTologin.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_id:
                startActivity(new Intent(getActivity(), LoginByPasswordActivity.class));
                break;
        }
    }
}
