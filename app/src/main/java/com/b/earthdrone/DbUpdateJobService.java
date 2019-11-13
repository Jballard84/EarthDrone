package com.b.earthdrone;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DbUpdateJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Map_Activity.MyTask myTask = new Map_Activity.MyTask();
        myTask.execute();
        return false;
    }
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }


}
