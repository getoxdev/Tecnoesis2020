package com.github.tenx.tecnoesis20.ui.splash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;

import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.ui.main.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.victor.loading.newton.NewtonCradleLoading;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity implements InstallStateUpdatedListener {

    @BindView(R.id.act_splash_mbtn_next)
    MaterialButton mbtnNext;

    @BindView(R.id.act_splash_progress)
    NewtonCradleLoading progress;


    private AppUpdateManager appUpdateManager;

    private static final int APP_UPDATE_REQUEST_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        appUpdateManager = AppUpdateManagerFactory.create(this);

        mbtnNext.setOnClickListener(v -> {
//            go to next screen
            showProgress();
            FirebaseMessaging.getInstance().subscribeToTopic("user").addOnCompleteListener(task -> {
                        checkUpdates(() -> moveToActivity(MainActivity.class));

            });
        });
    }

    public void showProgress(){
        progress.start();
        progress.setVisibility(View.VISIBLE);
        mbtnNext.setEnabled(false);
    }

    public void hideProgress()
    {   mbtnNext.setEnabled(true);
        progress.stop();
        progress.setVisibility(View.INVISIBLE);
    }

    private void moveToActivity(Class myActivityClass){
        Intent i = new Intent(SplashActivity.this, myActivityClass);
        hideProgress();
        startActivity(i);
        finish();
    }


    @Override
    public void onStateUpdate(InstallState state) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackbarForCompleteUpdate();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {

                    // If the update is downloaded but not installed,
                    // notify the user to complete the update.
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdate();
                    }
                });

    }

    private void checkUpdates(TaskHelper callback){
        // Creates instance of the manager.
// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, SplashActivity.this , APP_UPDATE_REQUEST_CODE);
                }catch (IntentSender.SendIntentException e){
                    Timber.e("Error sending intent : %s",e.getMessage());
                    callback.onComplete();
                }

            }else {
                callback.onComplete();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == APP_UPDATE_REQUEST_CODE){
            if(resultCode == RESULT_OK)
               popupSnackbarForCompleteUpdate();
            else if(resultCode == RESULT_CANCELED)
                moveToActivity(MainActivity.class);
            else
                checkUpdates(() -> moveToActivity(MainActivity.class));

        }
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.rl_splash),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.md_green_50));
        snackbar.show();
    }

}
