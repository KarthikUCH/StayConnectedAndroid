package com.stay.connected.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stay.connected.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentSignInListener} interface
 * to handle interaction events.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    @BindView(R.id.input_lay_email)
    TextInputLayout inputLayEmail;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.input_lay_password)
    TextInputLayout inputLayPassword;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    private OnFragmentSignInListener mListener;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment SignInFragment.
     */
    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInClick() {

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.length() == 0) {
            edtEmail.requestFocus();
            return;
        } else if (edtPassword.length() == 0) {
            edtPassword.requestFocus();
            return;
        }

        if (mListener != null) {
            mListener.signInUser(email, password);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSignInListener) {
            mListener = (OnFragmentSignInListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentRegistrationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentSignInListener {
        void signInUser(String email, String password);
    }
}
