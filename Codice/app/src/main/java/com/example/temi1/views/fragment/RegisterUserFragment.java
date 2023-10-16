package com.example.temi1.views.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class RegisterUserFragment extends Fragment implements
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
    private Button saveActivityBtn;
    private TextInputLayout nameUser, emailUser, passwordUser;
    private ImageView addImageFace;
    int CAMERA_PICTURE = 1;
    private Bitmap bitmap;
    private final Executor executor = Executors.newSingleThreadExecutor();
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


    public RegisterUserFragment() {
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
        return inflater.inflate(R.layout.fragment_register_user, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // get an instance of the robot
        robot = Robot.getInstance();

        // Initialize the Room Database instance
        temiUserRoomDatabase = temiUserRoomDatabase.getDatabase(getActivity());

        parentLayout = view.findViewById(R.id.fragment_registerUserFrameLayout);
        parentLayout.setOnTouchListener((v, motionEvent) -> {
            hideKeyboard(v);
            return true;
        });

        nameUser = view.findViewById(R.id.fragment_insert_activity_text_input_layout);

        emailUser = view.findViewById(R.id.fragment_insert_activity_text_input_layout_emailActivity);

        passwordUser = view.findViewById(R.id.fragment_insert_activity_text_input_layout_passwordActivity);

        addImageFace = view.findViewById(R.id.addImageFace);
        addImageFace.setOnClickListener(v -> onButtonImageClicked());

        bt_back =  view.findViewById(R.id.btn_back);
        bt_back.setOnClickListener(v -> button_back());

        saveActivityBtn = view.findViewById(R.id.fragment_create_route_button_sava);
        saveActivityBtn.setOnClickListener(v -> {
            saveUserButton();
        });

        bt_help  = view.findViewById(R.id.btn_help);
        bt_help.setOnClickListener(v -> button_help());

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


    private void saveUserButton(){
        // Get the values entered by the user
        String name = nameUser.getEditText().getText().toString().trim();
        String email = emailUser.getEditText().getText().toString().trim();
        String password = passwordUser.getEditText().getText().toString().trim();

        // Verify that all fields have been filled in and an image has been selected
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || bitmap == null) {
            robot.speak(TtsRequest.create("Please enter all the required details.", true));
        }else  if (name.isEmpty() ) {
            robot.speak(TtsRequest.create("Please enter the name.", true));
        }else if (email.isEmpty() ) {
            robot.speak(TtsRequest.create("Please enter the email.", true));
        }else if (password.isEmpty() ) {
            robot.speak(TtsRequest.create("Please enter the password.", true));
        } else if ( bitmap == null) {
            robot.speak(TtsRequest.create("Please select a photo.", true));
        } else if (!isValidEmail(email)) {
            robot.speak(TtsRequest.create("Please enter a valid email address.", true));
        } else if (!isValidPassword(password)) {
            robot.speak(TtsRequest.create("The password must have at least 8 characters, one uppercase, one lowercase character, and at least one number.", true));
        } else {
            // All is valid, so run SendImageTask to send the image
            new SendImageTask().execute(getImageDataAsByteArray(bitmap));
        }
    }


    public void onButtonImageClicked() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can open the camera
                openCamera();
            } else {
                // Permission denied, show a warning to the user and ask for permission again
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // Show a warning to the user explaining why you need access to the camera
                    // and then request permission again.
                    Toast.makeText(getActivity(), "Camera permission is required to access the camera.", Toast.LENGTH_LONG).show();
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    // The user has selected "Don't ask again", so you can show a message
                    // or disable some features that require access to the camera.
                    Toast.makeText(getActivity(), "Camera permission is required to access the camera.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create a camera capture request
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1); // Rear camera
            cameraIntent.putExtra("android.intent.extra.zoom", 1.0f); // Set zoom level to 1.0 (no zoom)

            startActivityForResult(cameraIntent, CAMERA_PICTURE);
        } else {
            Toast.makeText(getActivity(), "Camera not found", Toast.LENGTH_SHORT).show();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PICTURE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap capturedBitmap = (Bitmap) data.getExtras().get("data");
                if (capturedBitmap != null) {
                    // Controlling image size
                    int imageSizeInBytes = capturedBitmap.getByteCount();

                    // Checking the image size
                    int maxSizeBytes = 1024 * 1024; // 1 MB
                    if (imageSizeInBytes > maxSizeBytes) {
                        // Show an error message to the user
                        Toast.makeText(getActivity(), "Image size exceeds the maximum allowed limit.", Toast.LENGTH_SHORT).show();
                    } else {
                        // The image has been successfully captured, so assign it to "bitmap"
                        bitmap = capturedBitmap;
                        addImageFace.setImageBitmap(bitmap);
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to capture image.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private byte[] getImageDataAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    private void button_back(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_RegisterUserFragment_to_RegisterOrViewActivityFragment);
    }


    private void button_help() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),  R.style.AlertDialogStyle);
        builder.setCancelable(true);
        builder.setTitle("Help");
        builder.setMessage(R.string.helperRegisterUserFragment);
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


    private class SendImageTask extends AsyncTask<byte[], Void, String> {

        @Override
        protected String doInBackground(byte[]... params) {
            byte[] byteArray = params[0];

            if (byteArray == null || byteArray.length == 0) {
                Log.e("SendImageTask", "Image data is empty or null.");
                return null;
            }

            try {
                String serverIpAddress = "143.225.85.144";
                int serverPort = 65535; // Server port

                Socket socket = new Socket(serverIpAddress, serverPort);

                // Gets the socket stream output
                OutputStream outputStream = socket.getOutputStream();

                // Send the size of the byte array at the start of the stream
                byte[] sizeBytes = ByteBuffer.allocate(4).putInt(byteArray.length).array();
                outputStream.write(sizeBytes);

                // Send byte array
                outputStream.write(byteArray, 0, byteArray.length);
                outputStream.flush();

                // Receive the biometric key from the server
                InputStream inputStream = socket.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                String receivedBiometricKey = byteArrayOutputStream.toString().trim();

                // Close the socket after sending and receiving
                socket.close();

                return receivedBiometricKey;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String biometricKey) {
            if (biometricKey != null && !biometricKey.isEmpty() && !biometricKey.equals("NO_FACE_DETECTED")) {

                // Whether the biometric key was generated successfully and is not empty
                // I proceed with saving the user data in the Room database

                nameUser = nameUser.findViewById(R.id.fragment_insert_activity_text_input_layout);
                emailUser = emailUser.findViewById(R.id.fragment_insert_activity_text_input_layout_emailActivity);
                passwordUser = passwordUser.findViewById(R.id.fragment_insert_activity_text_input_layout_passwordActivity);


                String name = nameUser.getEditText().getText().toString().trim();
                String email = emailUser.getEditText().getText().toString().trim();
                String password = passwordUser.getEditText().getText().toString().trim();
                int userId = new Random().nextInt(Integer.MAX_VALUE);


                // Save user data to Room database using AsyncTask
                new RegisterUserFragment.SaveUserDataTask().execute(name, email, biometricKey,password, String.valueOf(userId));

            } else if (biometricKey.equals("NO_FACE_DETECTED")) {

                // No face detected, show message to user.
                robot.speak(TtsRequest.create("Attention, there is no face in the image. Please retry.", true));
                Log.e("SendImageTask", "No faces detected.");

                navController.navigate(R.id.action_RegisterUserFragment_to_RegisterOrViewActivityFragment);

            } else {

                // In case of null or empty biometric key, I handle the error or the absence of data
                robot.speak(TtsRequest.create("Attention, there is no face in the image and consequently the biometric key has not been generated.", true));
                Log.e("SendImageTask", "Error generating biometric key.");

                //addImageFace.setEnabled(true);

                navController.navigate(R.id.action_RegisterUserFragment_to_RegisterOrViewActivityFragment);

            }
        }
    }


    private class SaveUserDataTask extends AsyncTask<String, Void, Boolean> {

        private String biometricKey;
        private String name;
        private String email;
        private String password;
        private int idUser;

        @Override
        protected Boolean doInBackground(String... userData) {
            name = userData[0];
            email = userData[1];
            biometricKey = userData[2];
            password = userData[3];
            idUser = Integer.parseInt(userData[4]);

            // Check if a user with same email already exists
            TemiUser existingUser = temiUserRoomDatabase.userDao().getUserByEmail(email);
            if (existingUser != null) {
                return false; // A user with the same email already exists
            }

            try {
                // Create the UserDataEntity instance and set the values
                TemiUser userDataEntity = new TemiUser();
                userDataEntity.setBiometricKey(biometricKey);
                userDataEntity.setName(name);
                userDataEntity.setEmail(email);
                userDataEntity.setPassword(password);
                userDataEntity.setUserId(idUser);

                // Insert the data into the database
                temiUserRoomDatabase.userDao().insertUserIntoDb(userDataEntity);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean isInserted) {
            if (isInserted) {
                // User data inserted successfully
                robot.speak(TtsRequest.create("User data inserted successfully.", true));
                printUserList();
                navController.navigate(R.id.action_RegisterUserFragment_to_RegisterOrViewActivityFragment);
            } else {
                // User with the same biometric key already exists
                robot.speak(TtsRequest.create("Error, the user already exists.", true));
            }
        }
    }


    private void printUserList() {
        executor.execute(() -> {
            List<TemiUser> userList = temiUserRoomDatabase.userDao().getAll();
            getActivity().runOnUiThread(() -> {
                for (TemiUser user : userList) {
                    Log.d("Database", "------------In Room Database ------------");
                    Log.d("Database", "User ID: " + user.getUserId());
                    Log.d("Database", "Name: " + user.getName());
                    Log.d("Database", "Email: " + user.getEmail());
                    Log.d("Database", "Password: " + user.getPassword());
                    Log.d("Database", "Biometric_Key: " + user.getBiometricKey());
                }
            });
        });
    }

}