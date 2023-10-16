package com.example.temi1.views.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.temi1.R;
import com.example.temi1.Services.SearchingUserService;
import com.example.temi1.db.UserTemi.TemiUserDao;
import com.example.temi1.db.UserTemi.TemiUserRoomDatabase;
import com.example.temi1.model.TemiUser;
import com.example.temi1.viewmodel.GlobalViewModel;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import butterknife.ButterKnife;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotificationActivity extends AppCompatActivity {

    TextView taskInfo, FaceRecognition;
    Button confirmAndStayButton;
    Button confirmAndGoToBase;
    private Robot robot;
    private ImageView addImageFace;
    int CAMERA_PICTURE = 1;
    private Bitmap bitmap;
    private TextView taskName;
    private Button saveImageBtn;
    private ImageButton btn_back;
    private CardView cardView_activity_notification_description;
    public static final String TASK_INFO ="TASKINFO";
    public static final String TASK_IDUSER ="TASKIDUSER";
    private LinearLayout linearLayout_activityNotification;
    private Executor backgroundExecutor = Executors.newSingleThreadExecutor();
    private NavController navController;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Hide the action bar
        getSupportActionBar().hide();

        robot = Robot.getInstance();
        ButterKnife.bind(this);

        taskName = findViewById(R.id.Name);
        taskName.setVisibility(View.INVISIBLE);

        taskInfo = findViewById(R.id.notification_text);
        taskInfo.setText(getIntent().getStringExtra(TASK_INFO));
        taskInfo.setVisibility(View.INVISIBLE);

        FaceRecognition = findViewById(R.id.FaceRecognition);

        cardView_activity_notification_description = findViewById(R.id.cardView_activity_notification_description);
        cardView_activity_notification_description.setVisibility(View.INVISIBLE);


        Log.d("NotificationsActivity", "Activity Tasks: " + getIntent().getStringExtra(TASK_INFO));

        int taskUserId = getIntent().getIntExtra(TASK_IDUSER, 0);
        Log.d("NotificationsActivity", "Activity Tasks id user: " + taskUserId);
        
        try {
            getApplicationContext().stopService(new Intent(getApplicationContext(), SearchingUserService.class));
        } catch (Exception e) {
            Log.e("NotificationActivity", "Exception: " + e);
        }

        linearLayout_activityNotification = findViewById(R.id.linearLayout_activityNotification_btn);
        linearLayout_activityNotification.setVisibility(View.INVISIBLE);


        confirmAndStayButton = findViewById(R.id.activity_notification_btnConfirm);
        confirmAndStayButton.setOnClickListener(v -> button_confirmAndStayButton());
        confirmAndStayButton.setVisibility(View.INVISIBLE);


        confirmAndGoToBase = findViewById(R.id.activity_notification_goToHomeBaseButton);
        confirmAndGoToBase.setOnClickListener(v -> button_confirmAndGoToBase());
        confirmAndGoToBase.setVisibility(View.INVISIBLE);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> button_back());


        addImageFace = findViewById(R.id.addImageFace);
        addImageFace.setOnClickListener(v -> onButtonImageClicked1());

        saveImageBtn = findViewById(R.id.fragment_create_route_button_sava);
        saveImageBtn.setOnClickListener(v -> {
            saveUserButton();
        });
    }

    private void button_back(){
        GlobalViewModel viewModel = new ViewModelProvider(this).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        //navController.navigate(R.id.action_NotificationActivity_to_RegisterOrViewActivityFragment);
    }


    private void button_confirmAndStayButton(){
        stopService(new Intent(this, SearchingUserService.class));
        robot.stopMovement();
        finish();
    }


    private void button_confirmAndGoToBase(){
        goTo();
        finish();
    }


    public void goTo(){
        robot.goTo("Home Base");
    }


    public void onButtonImageClicked1() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
    }


    private void openCamera() {
        Intent cameraI = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraI, CAMERA_PICTURE);
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
                    Toast.makeText(this, "Camera permission is required to access the camera.", Toast.LENGTH_LONG).show();
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    // The user has selected "Don't ask again", so you can show a message
                    // or disable some features that require access to the camera.
                    Toast.makeText(this, "Camera permission is required to access the camera.", Toast.LENGTH_SHORT).show();
                }
            }
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
                        Toast.makeText(this, "Image size exceeds the maximum allowed limit.", Toast.LENGTH_SHORT).show();
                    } else {
                        // The image has been successfully captured, so assign it to "bitmap"
                        bitmap = capturedBitmap;
                        addImageFace.setImageBitmap(bitmap);
                    }
                } else {
                    Toast.makeText(this, "Failed to capture image.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private byte[] getImageDataAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    private void saveUserButton(){
        // Verify that all fields have been filled in and an image has been selected
        if ( bitmap == null) {
            robot.speak(TtsRequest.create("Please enter  a photo.", true));
        } else {
            // All is valid, so run SendImageTask to send the image
            new SendImageTask().execute(getImageDataAsByteArray(bitmap));
        }
    }


    private class SendImageTask extends AsyncTask<byte[], Void, String> {

        int taskUserId = getIntent().getIntExtra(TASK_IDUSER, 0);

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
                backgroundExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Execute a query to look for a match in the database on a background thread
                        TemiUser matchingUser = null;
                        if (biometricKey != null && !biometricKey.isEmpty()) {
                            TemiUserDao userDao = TemiUserRoomDatabase.getDatabase(getApplicationContext()).userDao();

                            // Extract the new biometric key as a double array (you need to implement this)
                            double[] newBiometricKeyArray = extractBiometricKey(biometricKey);

                            List<TemiUser> allUsers = userDao.getAll();
                            double bestMatchScore = 0.0; // Initialize the best match score
                            for (TemiUser user : allUsers) {
                                // Extract the stored biometric key as a double array (you need to implement this)
                                double[] storedBiometricKeyArray = extractBiometricKey(user.getBiometricKey());

                                // Calculate the similarity score between the new key and the stored key
                                double similarityScore = calculateSimilarity(newBiometricKeyArray, storedBiometricKeyArray);

                                // Define a threshold for similarity
                                double similarityThreshold = 0.9;

                                if (similarityScore >= similarityThreshold && similarityScore > bestMatchScore) {
                                    // Found a user with a similar biometric key
                                    matchingUser = user;
                                    bestMatchScore = similarityScore;
                                }
                            }
                        }

                        // Now you can update the UI on the main (UI) thread
                        TemiUser finalMatchingUser = matchingUser;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (finalMatchingUser != null) {
                                    // Found a user with a similar biometric key
                                    // You can take appropriate actions here, e.g., log in the found user
                                    Toast.makeText(NotificationActivity.this, "Recognized user", Toast.LENGTH_SHORT).show();

                                    // Check if the recognized user's ID matches the task creator's ID
                                    if (finalMatchingUser.getUserId() == taskUserId) {

                                        // The recognized user's ID matches the task creator's ID
                                        // You can show a notification with task info, for example
                                        taskName.setVisibility(View.VISIBLE);
                                        taskInfo.setVisibility(View.VISIBLE);
                                        cardView_activity_notification_description.setVisibility(View.VISIBLE);
                                        confirmAndStayButton.setVisibility(View.VISIBLE);
                                        confirmAndGoToBase.setVisibility(View.VISIBLE);
                                        addImageFace.setVisibility(View.INVISIBLE);
                                        linearLayout_activityNotification.setVisibility(View.VISIBLE);
                                        FaceRecognition.setVisibility(View.INVISIBLE);
                                        saveImageBtn.setVisibility(View.INVISIBLE);

                                    } else {
                                        // The recognized user's ID does not match the task creator's ID
                                        // You can handle this case as needed
                                        Toast.makeText(NotificationActivity.this, "The recognized user is not the task user", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // No user with a similar biometric key found
                                    Toast.makeText(NotificationActivity.this, "Unrecognized user.", Toast.LENGTH_SHORT).show();

                                    robot.goTo("home base");

                                }
                            }
                        });
                    }
                });
            } else if ("NO_FACE_DETECTED".equals(biometricKey)) {
                // Handle the case where no face is detected on the UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NotificationActivity.this, "Attention, there is no face in the image. Please retry.", Toast.LENGTH_SHORT).show();
                        Log.e("SendImageTask", "No faces detected.");
                    }
                });
            } else {
                // Handle other cases such as null or empty biometric key on the UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NotificationActivity.this, "Attention, there is no face in the image and consequently the biometric key has not been generated.", Toast.LENGTH_SHORT).show();
                        Log.e("SendImageTask", "Error generating biometric key.");
                        addImageFace.setEnabled(true);
                    }
                });
            }
        }
    }


    private double[] extractBiometricKey(String biometricKey) {
        // Split the string by spaces
        String[] keyParts = biometricKey.split("\\s+");

        double[] extractedKey = new double[keyParts.length];

        for (int i = 0; i < keyParts.length; i++) {
            try {
                extractedKey[i] = Double.parseDouble(keyParts[i]);
            } catch (NumberFormatException e) {
                // Handle parsing errors here, e.g., log an error or set a default value
                extractedKey[i] = 0.0;
            }
        }
        return extractedKey;
    }

    private double calculateSimilarity(double[] array1, double[] array2) {
        int minLength = Math.min(array1.length, array2.length);

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < minLength; i++) {
            dotProduct += array1[i] * array2[i];
            normA += Math.pow(array1[i], 2);
            normB += Math.pow(array2[i], 2);
        }

        if (normA == 0.0 || normB == 0.0) {
            return 0.0; // Handle division by zero
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}


