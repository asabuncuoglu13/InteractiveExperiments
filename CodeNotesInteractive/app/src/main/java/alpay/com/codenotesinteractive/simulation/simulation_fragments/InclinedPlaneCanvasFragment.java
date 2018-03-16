package alpay.com.codenotesinteractive.simulation.simulation_fragments;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import alpay.com.codenotesinteractive.R;

import static android.content.Context.SENSOR_SERVICE;

public class InclinedPlaneCanvasFragment extends Fragment implements View.OnClickListener{

    public View view;
    SensorManager mSensorManager;
    final float[] mValuesMagnet      = new float[3];
    final float[] mValuesAccel       = new float[3];
    final float[] mValuesOrientation = new float[3];
    final float[] mRotationMatrix    = new float[9];
    TextView sensor_text;

    public InclinedPlaneCanvasFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inclined_plane_canvas, container, false);
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);

        view.findViewById(R.id.sensor_button).setOnClickListener(this);
        sensor_text = (TextView) view.findViewById(R.id.sensor_text);
        final SensorEventListener mEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            public void onSensorChanged(SensorEvent event) {
                // Handle the events for which we registered
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                        break;
                    default:
                        break;
                }
            };
        };

        // You have set the event listener up, now just need to register this with the
        // sensor manager along with the sensor wanted.
        setListeners(sensorManager, mEventListener);
        return view;
    }

    // Register the event listener and sensor type.
    public void setListeners(SensorManager sensorManager, SensorEventListener mEventListener)
    {
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.sensor_button)
        {
            SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
            SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
            final CharSequence test;
            test = "results: " + mValuesOrientation[0]*90 +" "+mValuesOrientation[1] * 90 + " "+ mValuesOrientation[2] *90;
            sensor_text.setText(test);
        }
    }

}
