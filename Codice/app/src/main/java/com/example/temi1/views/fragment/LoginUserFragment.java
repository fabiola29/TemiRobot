package com.example.temi1.views.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.example.temi1.R;
import com.example.temi1.db.UserTemi.TemiUserRoomDatabase;
import com.example.temi1.model.TemiUser;
import com.example.temi1.temi.TemiSpeaker;
import com.example.temi1.viewmodel.GlobalViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnConstraintBeWithStatusChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import java.util.List;
import java.util.regex.Pattern;

public class LoginUserFragment extends Fragment implements
        Robot.NlpListener,
        OnRobotReadyListener,
        Robot.ConversationViewAttachesListener,
        Robot.WakeupWordListener,
        Robot.ActivityStreamPublishListener,
        Robot.TtsListener,
        OnBeWithMeStatusChangedListener,
        OnGoToLocationStatusChangedListener,
        OnLocationsUpdatedListener,
        OnConstraintBeWithStatusChangedListener,
        OnDetectionStateChangedListener,
        Robot.AsrListener  {

    private ImageButton bt_back;
    private NavController navController;
    private FrameLayout parentLayout;
    private GlobalViewModel viewModel;
    private TemiSpeaker temiSpeaker;
    private ImageButton bt_help;
    private Robot robot;
    public static final String ACTION_HOME_WELCOME = "home.welcome", ACTION_HOME_DANCE = "home.dance", ACTION_HOME_SLEEP = "home.sleep";
    public static final String HOME_BASE_LOCATION = "homebase";
    private Button saveActivityBtn,forgotPasswordBtn;
    private TextInputLayout emailUser, passwordUser;
    private TemiUserRoomDatabase temiUserRoomDatabase;


    /**
     * Setting up all the event listeners
     */
    @Override
    public void onStart() {
        super.onStart();

        // Add robot event listeners
        robot.addOnRobotReadyListener(this);
        robot.addNlpListener(this);
        robot.addOnBeWithMeStatusChangedListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addWakeupWordListener(this);
        robot.addTtsListener(this);
        robot.addOnLocationsUpdatedListener(this);
        robot.addOnConstraintBeWithStatusChangedListener(this);
        robot.addOnDetectionStateChangedListener(this);
        robot.addAsrListener(this);
    }


    /**
     * Removing the event listeners upon leaving the app.
     */
    @Override
    public void onStop() {
        super.onStop();

        // Remove robot event listeners
        robot.removeOnRobotReadyListener(this);
        robot.removeNlpListener(this);
        robot.removeOnBeWithMeStatusChangedListener(this);
        robot.removeOnGoToLocationStatusChangedListener(this);
        robot.removeWakeupWordListener(this);
        robot.removeTtsListener(this);
        robot.removeOnLocationsUpdateListener(this);
        robot.removeAsrListener(this);
        robot.stopMovement();
    }

    public LoginUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        temiSpeaker = new TemiSpeaker(viewModel.getTemiRobot());

    }


    /**
     * Designed to close the keyboard when not in use or when you decide not to use it
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_user, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Initialize the Room Database instance
        temiUserRoomDatabase = TemiUserRoomDatabase.getDatabase(getActivity());

        // get an instance of the robot
        robot = Robot.getInstance();

        parentLayout = view.findViewById(R.id.fragment_loginUserFrameLayout);
        parentLayout.setOnTouchListener((v, motionEvent) -> {
            hideKeyboard(v);
            return true;
        });

        emailUser = view.findViewById(R.id.fragment_loginUser_text_input_layout);

        passwordUser = view.findViewById(R.id.fragment_loginUser_text_input_layout_passwordActivity);

        bt_back =  view.findViewById(R.id.btn_back);
        bt_back.setOnClickListener(v -> button_back());

        saveActivityBtn = view.findViewById(R.id.fragment_loginUser_sava);
        saveActivityBtn.setOnClickListener(v -> {
            loginUserButton();
        });

        bt_help  = view.findViewById(R.id.btn_help);
        bt_help.setOnClickListener(v -> button_help());

        forgotPasswordBtn = view.findViewById(R.id.btn_forgot_password);
        forgotPasswordBtn.setOnClickListener(v -> {
            showForgotPasswordDialog();
        });

    }


    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private boolean isValidPassword(String password) {
        /*

        ^ : start of string
             (?=[A-Z0-9]*[a-z]) : Look ahead to ensure at least one lowercase character
             (?=[a-zA-Z]*[0-9]) : look ahead to ensure at least one digit
             (?=[a-z0-9]*[A-Z]) : look ahead to ensure at least one uppercase character
                [a-zA-Z0-9]{8,} : Captures eight p plus alphanumeric characters
                              $ : matches the end of the string
        */
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "^(?=[A-Z0-9]*[a-z])(?=[a-zA-Z]*[0-9])(?=[a-z0-9]*[A-Z])[a-zA-Z0-9]{8,}$");

        return PASSWORD_PATTERN.matcher(password).matches();
    }


    private void showForgotPasswordDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);

        final TextInputLayout emailTextInputLayout = mView.findViewById(R.id.dialog_forgot_password_email_layout);
        final TextInputLayout newPasswordTextInputLayout = mView.findViewById(R.id.dialog_forgot_password_newPassword_layout);
        final TextInputLayout confermPasswordTextInputLayout = mView.findViewById(R.id.dialog_forgot_password_confermPassword_layout);

        Button submitBtn = mView.findViewById(R.id.dialog_forgot_password_submitBtn);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        // Hides the keyboard when you tap mView
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
                return false;
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailTextInputLayout.getEditText().getText().toString().trim();
                final String newPassword = newPasswordTextInputLayout.getEditText().getText().toString().trim();
                final String confermPassword = confermPasswordTextInputLayout.getEditText().getText().toString().trim();

                if (email.isEmpty() && newPassword.isEmpty() && confermPassword.isEmpty() ) {
                        robot.speak(TtsRequest.create("Please fill any empty", true));
                }else if(email.isEmpty()) {
                   robot.speak(TtsRequest.create("Enter your email", true));
                }else if(newPassword.isEmpty()) {
                   robot.speak(TtsRequest.create("Enter new password", true));
                }else if(confermPassword.isEmpty()) {
                   robot.speak(TtsRequest.create("Enter the password conferm", true));
                }else if (!isValidEmail(email)) {
                   robot.speak(TtsRequest.create("Incorrect email format", true));
                }else if (!isValidPassword(newPassword)){
                   robot.speak(TtsRequest.create("The password must have at least 8 characters, one uppercase, one lowercase character and at least one number", true));
                } else {
                    // Perform database initialization in an AsyncTask
                    new InitializeDatabaseAsyncTask(email, newPassword, confermPassword, dialog).execute();
                }
            }
        });
    }


    // AsyncTask for database initialization
    private class InitializeDatabaseAsyncTask extends AsyncTask<Void, Void, TemiUserRoomDatabase> {
        private final String email;
        private final String newPassword;
        private final String confermPassword;
        private final AlertDialog dialog;

        public InitializeDatabaseAsyncTask(String email, String newPassword, String confermPassword, AlertDialog dialog) {
            this.email = email;
            this.newPassword = newPassword;
            this.confermPassword = confermPassword;
            this.dialog = dialog;
        }

        @Override
        protected TemiUserRoomDatabase doInBackground(Void... voids) {
            return Room.databaseBuilder(getActivity().getApplicationContext(),
                    TemiUserRoomDatabase.class, "TemiUserDatabase").build();
        }

        @Override
        protected void onPostExecute(TemiUserRoomDatabase database) {
            temiUserRoomDatabase = database;
            new VerifyEmailAsyncTask(email, newPassword, confermPassword, dialog).execute();
        }
    }


    private void loginUserButton(){
        String email = emailUser.getEditText().getText().toString().trim();
        String password = passwordUser.getEditText().getText().toString().trim();

        new LoginUserAsyncTask().execute(email, password);

    }


    // AsyncTask for email verification
    private class VerifyEmailAsyncTask extends AsyncTask<Void, Void, TemiUser> {
        private final String email;
        private final String newPassword;
        private final String confermPassword;
        private final AlertDialog dialog;

        public VerifyEmailAsyncTask(String email, String newPassword, String confermPassword, AlertDialog dialog) {
            this.email = email;
            this.newPassword = newPassword;
            this.confermPassword = confermPassword;
            this.dialog = dialog;
        }

        @Override
        protected TemiUser doInBackground(Void... voids) {
            Log.d("DEBUG", "Email inserita dall'utente: " + email);

            // Check the email in background
            TemiUser user = temiUserRoomDatabase.userDao().getUserByEmail(email);

            if (user != null) {
                Log.d("DEBUG", "Email trovata nel database: " + user.getEmail());
            } else {
                Log.d("DEBUG", "Email non trovata nel database");
            }

            return user;

        }

        @Override
        protected void onPostExecute(TemiUser user) {
            if (user != null) {
                if (newPassword.equals(confermPassword)) {
                    new UpdateUserAsyncTask(user, dialog, newPassword).execute();
                } else {
                    robot.speak(TtsRequest.create("Passwords do not match", true));
                }
            } else {
                robot.speak(TtsRequest.create("Email not found", true));
            }
        }
    }


    // AsyncTask for user update
    private class UpdateUserAsyncTask extends AsyncTask<Void, Void, Void> {
        private final TemiUser user;
        private final AlertDialog dialog;
        private final String newPassword;
        public UpdateUserAsyncTask(TemiUser user, AlertDialog dialog, String newPassword) {
            this.user = user;
            this.dialog = dialog;
            this.newPassword = newPassword;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            // Perform user update in background
            user.setPassword(newPassword);
            temiUserRoomDatabase.userDao().updateUser(user);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            robot.speak(TtsRequest.create("Password updated successfully", true));
            dialog.dismiss();
        }
    }


    // AsyncTask to perform login operations in the database
    private class LoginUserAsyncTask extends AsyncTask<String, Void, TemiUser> {
        private String password;

        @Override
        protected TemiUser doInBackground(String... params) {
            String email = params[0];
            password = params[1];

            TemiUserRoomDatabase temiUserRoomDatabase = TemiUserRoomDatabase.getDatabase(getActivity());

            // Check l'email in the database
            TemiUser user = temiUserRoomDatabase.userDao().getUserByEmail(email);

            if (user != null) {
                // If the user exists, compare the password
                if (!user.getPassword().equals(password)) {
                    // Password mismatch, return null
                    return null;
                }
            }

            // Return user (may be null if user does not exist or password does not match)
            return user;
        }

        @Override
        protected void onPostExecute(TemiUser user) {
            if (user != null) {
                // The user was found in the database and the password matches
                // You can proceed to the next screen or perform necessary actions
                viewModel.setLoggedInUserId(user.getUserId());
                Log.d("UserID", "User ID logged in: " + viewModel.getLoggedInUserId());
                navController.navigate(R.id.action_LoginUserFragment_to_homeFragment);
            } else {
                // The user was not found in the database or the password does not match
                // We get the values entered by the user
                String email = emailUser.getEditText().getText().toString().trim();
                String password = passwordUser.getEditText().getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    robot.speak(TtsRequest.create("Please fill any empty", true));
                }else if (email.isEmpty()) {
                    robot.speak(TtsRequest.create("Please enter email", true));
                }else if ( password.isEmpty() ) {
                    robot.speak(TtsRequest.create("Please enter password", true));
                }else if (!isValidEmail(email)){
                    robot.speak(TtsRequest.create("Incorrect email format", true));
                }else if (!isValidPassword(password)) {
                    robot.speak(TtsRequest.create("The password must have at least 8 characters, one uppercase, one lowercase character and at least one number", true));
                }
                robot.speak(TtsRequest.create("Login failed. Incorrect email or password", true));
            }
        }
    }


    private void button_back(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_LoginUserFragment_to_RegisterOrViewActivityfragment);
    }


    private void button_help() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),  R.style.AlertDialogStyle);
        builder.setCancelable(true);
        builder.setTitle("Help");
        builder.setMessage(R.string.helperLoginUserFragment);
        builder.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    @Override
    public void onPublish(ActivityStreamPublishMessage message) {
        //After the activity stream finished publishing (photo or otherwise).
        //Do what you want based on the message returned.
        robot.speak(TtsRequest.create("Uploaded.", false));
    }


    @Override
    public void onAsrResult(@NonNull String asrResult) {
        Log.d("onAsrResult", "asrResult = " + asrResult);
    }


    @Override
    public void onConversationAttaches(boolean isAttached) {
        if (isAttached) {
            //Do something as soon as the conversation is displayed.
        }
    }


    /**
     * When adding the Nlp Listener to your project you need to implement this method
     * which will listen for specific intents and allow you to respond accordingly.
     * <p>
     * See AndroidManifest.xml for reference on adding each intent.
     */
    @SuppressLint("Range")
    @Override
    public void onNlpCompleted(NlpResult nlpResult) {
        //do something with nlp result. Base the action specified in the AndroidManifest.xml
        Toast.makeText(getActivity(), nlpResult.action, Toast.LENGTH_SHORT).show();

        switch (nlpResult.action) {
            case ACTION_HOME_WELCOME:
                robot.tiltAngle(23, 5.3F);
                break;

            case ACTION_HOME_DANCE:
                long t = System.currentTimeMillis();
                long end = t + 5000;
                while (System.currentTimeMillis() < end) {
                    robot.skidJoy(0F, 1F);
                }
                break;

            case ACTION_HOME_SLEEP:
                robot.goTo(HOME_BASE_LOCATION);
                break;
        }
    }


    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        // Do whatever you like upon the status changing. after the robot finishes speaking
    }


    @Override
    public void onWakeupWord(String wakeupWord, int direction) {
        // Do anything on wakeup. Follow, go to location, or even try creating dance moves.
    }


    @Override
    public void onBeWithMeStatusChanged(String status) {
        //  When status changes to "lock" the robot recognizes the user and begin to follow.
        switch (status) {

            case "abort":
                // do something i.e. speak
                robot.speak(TtsRequest.create("Abort", false));
                break;

            case "calculating":
                robot.speak(TtsRequest.create("Calculating", false));
                break;

            case "lock":
                robot.speak(TtsRequest.create("Lock", false));
                break;

            case "search":
                robot.speak(TtsRequest.create("search", false));
                break;

            case "start":
                robot.speak(TtsRequest.create("Start", false));
                break;

            case "track":
                robot.speak(TtsRequest.create("Track", false));
                break;
        }
    }


    @Override
    public void onConstraintBeWithStatusChanged(boolean isConstraint) {
        Log.d("onConstraintBeWith", "status = " + isConstraint);
    }


    @Override
    public void onDetectionStateChanged(int state) {
        Log.d("onDetectionStateChanged", "state = " + state);
        if (state == DETECTED) {
            robot.constraintBeWith();
        } else {
            robot.stopMovement();
        }
    }


    @Override
    public void onGoToLocationStatusChanged(String location, String status, int descriptionId, String description) {
        Log.d("GoToStatusChanged", "descriptionId=" + descriptionId + ", description=" + description);
        switch (status) {
            case "start":
                robot.speak(TtsRequest.create("Starting", false));
                break;

            case "calculating":
                robot.speak(TtsRequest.create("Calculating", false));
                break;

            case "going":
                robot.speak(TtsRequest.create("Going", false));
                break;

            case "complete":
                robot.speak(TtsRequest.create("Completed", false));
                break;

            case "abort":
                robot.speak(TtsRequest.create("Cancelled", false));
                break;
        }
    }


    @Override
    public void onLocationsUpdated(List<String> locations) {
        //Saving or deleting a location will update the list.
        Toast.makeText(getActivity(), "Locations updated :\n" + locations, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getActivity().getPackageManager().getActivityInfo(getActivity().getComponentName(), PackageManager.GET_META_DATA);
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}