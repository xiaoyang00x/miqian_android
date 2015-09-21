package com.miqian.mq.net;

import com.miqian.mq.utils.MobileOS;

/**
 * Created by Jackie on 2015/9/16.
 */

public abstract class MultiVersionAsyncTask<Params, Progress, Result> extends android.os.AsyncTask<Params, Progress, Result> {

    public final MultiVersionAsyncTask<Params, Progress, Result> executeOnExecutor(Params... params) {
        if (!MobileOS.isIcsOrNewer()) {
            return (MultiVersionAsyncTask<Params, Progress, Result>) super.execute(params);
        } else {
            return (MultiVersionAsyncTask<Params, Progress, Result>) super.executeOnExecutor(MultiVersionAsyncTask.THREAD_POOL_EXECUTOR, params);
        }
    }
}