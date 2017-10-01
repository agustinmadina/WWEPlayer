package com.wwe.madina.wweplayer;

import android.support.design.widget.TextInputLayout;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.wwe.madina.wweplayer.activities.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Madina on 1/10/2017.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    private TextInputLayout usernameTextInput;
    private TextInputLayout passwordTextInput;
    private LoginActivity loginActivity;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule  = new  ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        loginActivity = loginActivityRule.getActivity();
        usernameEditText = (EditText) loginActivity.findViewById(R.id.input_username);
        passwordEditText = (EditText) loginActivity.findViewById(R.id.input_password);
        usernameTextInput = (TextInputLayout) loginActivity.findViewById(R.id.text_input_username);
        passwordTextInput = (TextInputLayout) loginActivity.findViewById(R.id.text_input_password);
    }

    @Test
    public void itShouldSetErrorInBothFieldsIfNotCompletedAndReturnFalse() throws Exception {
        loginActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertFalse(loginActivity.bothRequiredFieldsAreCompleted());
                assertTrue(usernameTextInput.getError() != null);
                assertTrue(passwordTextInput.getError() != null);
            }
        });
    }

    @Test
    public void itShouldSetErrorOnlyInUsernameAndReturnFalse() throws Exception {
        loginActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                passwordEditText.setText("dummy password");

                assertFalse(loginActivity.bothRequiredFieldsAreCompleted());
                assertTrue(usernameTextInput.getError() != null);
                assertFalse(passwordTextInput.getError() != null);
            }
        });
    }

    @Test
    public void itShouldSetErrorOnlyInPasswordInBothFieldsIfNotCompletedAndReturnFalse() throws Exception {
        loginActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                usernameEditText.setText("dummy username");

                assertFalse(loginActivity.bothRequiredFieldsAreCompleted());
                assertFalse(usernameTextInput.getError() != null);
                assertTrue(passwordTextInput.getError() != null);
            }
        });
    }

    @Test
    public void itShouldCleanBothErrorsAndReturnTrue() throws Exception {
        loginActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                usernameEditText.setText("dummy username");
                passwordEditText.setText("dummy password");

                assertTrue(loginActivity.bothRequiredFieldsAreCompleted());
                assertFalse(usernameTextInput.getError() != null);
                assertFalse(passwordTextInput.getError() != null);
            }
        });
    }
}
