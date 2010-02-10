/* Copyright (c) 2009 Matthias Käppler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.droidfu.activities;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.Window;

public class BetterActivityHelper {

    private static final String PROGRESS_DIALOG_TITLE_RESOURCE = "droidfu_progress_dialog_title";

    private static final String PROGRESS_DIALOG_MESSAGE_RESOURCE = "droidfu_progress_dialog_message";

    public static final String ERROR_DIALOG_TITLE_RESOURCE = "droidfu_error_dialog_title";

    // FIXME: this method currently doesn't work as advertised
    public static int getWindowFeatures(Activity activity) {
        Window window = activity.getWindow();
        if (window == null) {
            return 0;
        }
        try {
            // Method m =
            // activity.getWindow().getClass().getMethod("getFeatures");
            // Method[] m = window.getClass().getMethods();
            // m.setAccessible(true);
            // return (Integer) m.invoke(window);
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static ProgressDialog createProgressDialog(final Activity activity,
            int progressDialogTitleId, int progressDialogMsgId) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        if (progressDialogTitleId > 0) {
            progressDialog.setTitle(progressDialogTitleId);
        } else {
            progressDialog.setTitle(activity.getResources().getIdentifier(
                PROGRESS_DIALOG_TITLE_RESOURCE, "string", activity.getPackageName()));
        }
        if (progressDialogMsgId > 0) {
            progressDialog.setMessage(activity.getString(progressDialogMsgId));
        } else {
            progressDialogMsgId = activity.getResources().getIdentifier(
                PROGRESS_DIALOG_MESSAGE_RESOURCE, "string", activity.getPackageName());
            progressDialog.setMessage(activity.getString(progressDialogMsgId));
        }
        progressDialog.setIndeterminate(true);
        progressDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                activity.onKeyDown(keyCode, event);
                return false;
            }
        });
        // progressDialog.setInverseBackgroundForced(true);
        return progressDialog;
    }

    public static AlertDialog newYesNoDialog(final Activity activity, String dialogTitle,
            String screenMessage, int iconResourceId, OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.yes, listener);
        builder.setNegativeButton(android.R.string.no, listener);

        builder.setTitle(dialogTitle);
        builder.setMessage(screenMessage);
        builder.setIcon(iconResourceId);

        return builder.create();
    }

    public static AlertDialog newMessageDialog(final Activity activity, String dialogTitle,
            String screenMessage, int iconResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setTitle(dialogTitle);
        builder.setMessage(screenMessage);
        builder.setIcon(iconResourceId);

        return builder.create();
    }

    public static boolean isApplicationBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }
}
