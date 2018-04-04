package com.sf.libzxing.util;

import android.hardware.Camera;


/**
 * Created by sufan on 17/7/13.
 */

public class LightControl {

    Camera m_Camera;

    public LightControl(Camera camera) {
        m_Camera = camera;
    }

    public void turnOn() {
        try {
            Camera.Parameters mParameters = this.m_Camera.getParameters();
            mParameters.setFlashMode("torch");
            this.m_Camera.setParameters(mParameters);
        } catch (Exception var2) {

        }

    }

    public void turnOff() {
        try {
            Camera.Parameters mParameters = this.m_Camera.getParameters();
            mParameters.setFlashMode("off");
            this.m_Camera.setParameters(mParameters);
        } catch (Exception var2) {

        }

    }
}
