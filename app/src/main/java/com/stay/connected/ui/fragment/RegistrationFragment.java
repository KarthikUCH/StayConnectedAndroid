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

import com.lamudi.phonefield.PhoneInputLayout;
import com.stay.connected.R;
import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.application.StayConnected;
import com.stay.connected.ui.Presenter.IRegistrationPresenter;
import com.stay.connected.ui.view.IRegistrationView;
import com.stay.connected.util.AppUtil;
import com.stay.connected.widget.PhoneNumberLayout;

import javax.inject.Inject;

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
public class RegistrationFragment extends Fragment implements IRegistrationView {

    @Inject
    IRegistrationPresenter mRegistrationPresenter;

    @BindView(R.id.input_lay_name)
    TextInputLayout inputLayName;

    @BindView(R.id.edt_name)
    EditText edtName;

    @BindView(R.id.input_lay_email)
    TextInputLayout inputLayEmail;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.input_lay_mobile)
    PhoneNumberLayout phoneEditText;

    /*@BindView(R.id.edt_mobile)
    EditText edtMobile;*/

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
        injectComponent(((StayConnected) getActivity().getApplication()).getComponent());

        mRegistrationPresenter.attachView(this);
    }

    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, view);
        return view;
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
    public void onStart() {
        super.onStart();
        phoneEditText.setDefaultCountry("SG");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRegistrationPresenter.detachView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.btn_register)
    public void onRegisterClick() {
        boolean validationSuccess = true;
        inputLayName.setError(null);
        inputLayEmail.setError(null);
        phoneEditText.setError(null);
        inputLayPassword.setError(null);

        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String mobile = phoneEditText.getPhoneNumber();
        String password = edtPassword.getText().toString().trim();
        mRegistrationPresenter.registerUser(name, email, phoneEditText.isValid(), mobile, password);
    }

    @Override
    public void setInvalidName(boolean requestFocus) {
        inputLayName.setError(getResources().getString(R.string.text_invalid_user_name));
        if (requestFocus) {
            edtName.requestFocus();
        }
    }

    @Override
    public void setInvalidEmail(boolean requestFocus) {
        phoneEditText.setError(getResources().getString(R.string.text_invalid_user_number));
        if (requestFocus) {
            phoneEditText.requestFocus();
        }
    }

    @Override
    public void setInvalidNumber(boolean requestFocus) {
        inputLayPassword.setError(getResources().getString(R.string.text_invalid_user_password));
        if (requestFocus) {
            inputLayPassword.requestFocus();
        }
    }

    @Override
    public void setInvalidPassword(boolean requestFocus) {
        inputLayPassword.setError(getResources().getString(R.string.text_invalid_user_password));
        if (requestFocus) {
            inputLayPassword.requestFocus();
        }
    }

    @Override
    public void registeringUser() {
        if (mListener != null) {
            mListener.registeringUser();
        }
    }

    @Override
    public void registrationResponse(Integer responseCode) {
        if (mListener != null) {
            mListener.registrationResponse(responseCode);
        }
    }

    @Override
    public void showRegistrationError(String errorMsg) {
        if (mListener != null) {
            mListener.showRegistrationError(errorMsg);
        }
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
        void registeringUser();

        void registrationResponse(Integer responseCode);

        void showRegistrationError(String errorMsg);
    }
}
