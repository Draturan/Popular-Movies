package com.example.simone.popularmovies.async;

/**
 * Created by Simone on 28/03/2018 for Popular-Movies project
 * Guide find here:
 * http://www.jameselsey.co.uk/blogs/techblog/extracting-out-your-asynctasks-into-separate-classes-makes-your-code-cleaner/
 * as suggestion of reviewer of step 1 \o/ Fighting for best practices!
 */

/**
 * This is a useful callback mechanism so we can abstract our AsyncTasks out into separate, re-usable
 * and testable classes yet still retain a hook back into the calling activity. Basically, it'll make classes
 * cleaner and easier to unit test.
 *
 * @param <T>
 */
public interface AsyncTaskCompleteListener<T> {
    void onPreTaskExecute();
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param result The resulting object from the AsyncTask.
     */
    void onTaskComplete(T result);
}
