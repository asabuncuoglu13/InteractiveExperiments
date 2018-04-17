package alpay.com.interactiveexperiments.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

import alpay.com.interactiveexperiments.R;
import alpay.com.interactiveexperiments.camera.Components.CameraSource;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TextRecognitionActivity extends AppCompatActivity {

    @BindView(R.id.surface_view)
    SurfaceView cameraView;
    @BindView(R.id.text_view)
    TextView textView;
    CameraSource cameraSource;
    TextRecognizer textRecognizer;
    final int RequestCameraPermissionID = 1001;
    public final static int REQUEST_CODE = 1;
    String recognizedText = "";


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_recognition_from_camera_view);
        ButterKnife.bind(this);
        textRecognizer = new TextRecognizer.Builder(this).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(this, R.string.detector_dependency, Toast.LENGTH_SHORT).show();
        } else {
            buildCameraSource();
            setTextRecognizerProcessor();
        }
    }

    private void buildCameraSource() {
        cameraSource = new CameraSource.Builder(this, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    checkAndRequestPermission();
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
    }

    private void setTextRecognizerProcessor() {
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {
            }
            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if (items.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < items.size(); ++i) {
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                stringBuilder.append("\n");
                            }
                            recognizedText = stringBuilder.toString();
                            textView.setText(recognizedText);
                        }
                    });
                }
            }
        });
    }

    private void checkAndRequestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
            return;
        }
    }

    private void saveRecognizedText(String recognizedText) {
        Intent intent = new Intent();
        intent.putExtra("textFromCamera", recognizedText);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.read_code_button)
    public void readCode() {
        if (!recognizedText.equals(""))
            saveRecognizedText(recognizedText);
        else
            saveRecognizedText("NO TEXT");
    }
}