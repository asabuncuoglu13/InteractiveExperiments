package alpay.com.codenotesinteractive.compiler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

import alpay.com.codenotesinteractive.R;
import alpay.com.codenotesinteractive.compiler.Components.CameraSource;
import alpay.com.codenotesinteractive.compiler.Components.CodeBlocksCompiler;
import alpay.com.codenotesinteractive.simulation.SimulationActivity;


public class CompilerFragment extends Fragment implements View.OnClickListener{

    private final String TAG = "RecogBlocksActivity";
    View view;
    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    String code;
    CodeBlocksCompiler c = new CodeBlocksCompiler();


    public CompilerFragment() {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_compiler, container, false);

        cameraView = (SurfaceView) view.findViewById(R.id.surface_view);
        textView = (TextView) view.findViewById(R.id.text_view);
        view.findViewById(R.id.read_code_button).setOnClickListener(this);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();
        if (!textRecognizer.isOperational()) {
           Toast.makeText(getContext(), R.string.detector_dependency, Toast.LENGTH_SHORT).show();
        } else {

            cameraSource = new CameraSource.Builder(getContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
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

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() != 0)
                    {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i =0;i<items.size();++i)
                                {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                code = stringBuilder.toString();
                                textView.setText(code);
                            }
                        });
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.read_code_button)
        {
            if(code != null)
            {
                int[] s = c.compile(code);
                if(s[0] == -1)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.no_code_dialog_message)
                            .setTitle(R.string.no_code_dialog_title);
                    AlertDialog dialog = builder.create();
                }else
                {
                    Intent intent = new Intent(getActivity(), SimulationActivity.class);
                    intent.putExtra("output", s);
                    startActivity(intent);
                }
            }else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.no_code_dialog_message)
                        .setTitle(R.string.no_code_dialog_title);
                AlertDialog dialog = builder.create();
            }

        }
    }
}