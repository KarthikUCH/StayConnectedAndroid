package com.stay.connected.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.stay.connected.R;
import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.ui.fragment.RegistrationFragment;
import com.stay.connected.ui.fragment.SignInFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends InjectableActivity implements RegistrationFragment.OnFragmentInteractionListener, SignInFragment.OnFragmentInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_sing_in)
    TextView tvSignIn;

    @BindView(R.id.tv_register)
    TextView tvRegister;

    private SignInFragment mSignInFragment;
    private RegistrationFragment mRegistrationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSignInFragment = SignInFragment.newInstance();
        mRegistrationFragment = RegistrationFragment.newInstance();

        showSignInFragment();
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }


    @OnClick(R.id.tv_sing_in)
    public void showSignInFragment() {

        tvSignIn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_accent));
        tvRegister.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_grey));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mSignInFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @OnClick(R.id.tv_register)
    public void showRegistrationFragment() {

        tvSignIn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_grey));
        tvRegister.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_accent));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mRegistrationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
