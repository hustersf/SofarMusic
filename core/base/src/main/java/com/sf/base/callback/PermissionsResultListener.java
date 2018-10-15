package com.sf.base.callback;

/**
 * Created by sufan on 17/4/11.
 */

public interface PermissionsResultListener {

    void onPermissionGranted();

    void onPermissionDenied();
}
