package com.example.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsUtil {

    public static boolean hasPermissions(final Activity activity, final String permission,
                                         int rationaleMessageId, final int requestCode) {

        // Check to see if permission is already granted
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Explain why the permission is needed if the user has denied it before.
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionRationaleDialog(activity, rationaleMessageId,
                        (dialog, which) -> {
                            // Request the permission again
                            ActivityCompat.requestPermissions(activity,
                                    new String[] {permission}, requestCode);
                        });

            } else {
                // Request permission
                ActivityCompat.requestPermissions(activity,
                        new String[] {permission}, requestCode);
            }
            return false;
        }
        return true;
    }

    private static void showPermissionRationaleDialog(Activity activity, int messageId,
                                                      DialogInterface.OnClickListener onClickListener) {
        // Show a dialog explaining why the permission is required for the app
        new AlertDialog.Builder(activity)
                .setTitle(R.string.permission_needed)
                .setMessage(messageId)
                .setPositiveButton(R.string.ok, onClickListener)
                .create()
                .show();
    }

}
