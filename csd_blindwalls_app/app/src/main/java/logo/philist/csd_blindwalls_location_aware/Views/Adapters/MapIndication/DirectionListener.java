package logo.philist.csd_blindwalls_location_aware.Views.Adapters.MapIndication;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class DirectionListener implements SensorEventListener {

    private RotationListener rotationListener;

    private SensorManager sensorManager;

    private float[] mGravity;
    private float[] mGeomagnetic;

    public DirectionListener(Activity activity, RotationListener rotationListener){
        this.rotationListener = rotationListener;

        this.sensorManager = activity.getSystemService(SensorManager.class);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Update correct float arrays with correct sensor type
        int sensorType = sensorEvent.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            mGravity = sensorEvent.values;
        } else if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = sensorEvent.values;
        }

        // when both float arrays aren't null; calculate orientation
        if (mGravity != null && mGeomagnetic != null) {
            float[] rotationR = new float[9];
            float[] rotationI = new float[9];

            // Calculating rotation matrix of the gravity and geomagnetic, Google. Into 2 arrays
            if (SensorManager.getRotationMatrix(rotationR, rotationI, mGravity, mGeomagnetic)) {
                float[] orientation = new float[3];

                // gets the orientation out of rotation, Google.
                SensorManager.getOrientation(rotationR, orientation);

                // First plane of orientation is azimut, z axis, horizontal.
                float azimut = orientation[0];

                // From radians to degrees
                float rotation = -azimut * 180 / (float)Math.PI;

                this.rotationListener.setRotation(rotation);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void unregister() {
        this.sensorManager.unregisterListener(this);
    }
}
