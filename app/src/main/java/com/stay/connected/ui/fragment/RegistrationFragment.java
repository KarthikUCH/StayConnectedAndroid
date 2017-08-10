package com.stay.connected.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stay.connected.R;
import com.stay.connected.util.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentRegistrationListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {


    @BindView(R.id.input_lay_name)
    TextInputLayout inputLayName;

    @BindView(R.id.edt_name)
    EditText edtName;

    @BindView(R.id.input_lay_email)
    TextInputLayout inputLayEmail;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.input_lay_mobile)
    TextInputLayout inputLayMobile;

    @BindView(R.id.edt_mobile)
    EditText edtMobile;

    @BindView(R.id.input_lay_password)
    TextInputLayout inputLayPassword;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    private OnFragmentRegistrationListener mListener;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment RegistrationFragment.
     */
    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_register)
    public void onRegisterClick() {
        boolean validationSuccess = true;
        inputLayName.setError(null);
        inputLayEmail.setError(null);
        inputLayMobile.setError(null);
        inputLayPassword.setError(null);

        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            inputLayName.setError(getResources().getString(R.string.text_invalid_user_name));
            edtName.requestFocus();
            validationSuccess = false;
        }
        if (!AppUtil.verifyUserEmail(email)) {
            inputLayEmail.setError(getResources().getString(R.string.text_invalid_user_email));
            if (validationSuccess) {
                edtEmail.requestFocus();
                validationSuccess = false;
            }
        }
        if (mobile.length() < 8) {
            inputLayMobile.setError(getResources().getString(R.string.text_invalid_user_number));
            if (validationSuccess) {
                edtMobile.requestFocus();
                validationSuccess = false;
            }
        }
        if (!AppUtil.verifyUserPassword(password)) {
            inputLayPassword.setError(getResources().getString(R.string.text_invalid_user_password));
            if (validationSuccess) {
                edtPassword.requestFocus();
                validationSuccess = false;
            }
        }

        if (!validationSuccess) {
            return;
        }

        if (mListener != null) {
            mListener.registerUser(name, email, mobile, password);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentRegistrationListener) {
            mListener = (OnFragmentRegistrationListener) context;
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
    public interface OnFragmentRegistrationListener {
        // TODO: Update argument type and name
        void registerUser(String name, String email, String mobile, String password);
    }
}
