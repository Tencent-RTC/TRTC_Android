package com.tencent.trtc.mediashare.helper.reader.decoder.pipeline;

import android.util.Log;

import com.tencent.trtc.mediashare.helper.reader.exceptions.ProcessException;
import com.tencent.trtc.mediashare.helper.reader.exceptions.SetupException;


/**
 * Module for processing each frame
 */
public abstract class Stage {
    protected static final int    DEFAULT_FRAME_COUNT = 3;
    private static final   String TAG                 = "Stage";
    protected              State  mState              = State.INIT;

    /**
     * Initialization settings
     */
    public abstract void setup() throws SetupException;

    /**
     * <p>Process one frame</p>
     * Waiting is not allowed in this method
     */
    public abstract void processFrame() throws ProcessException;

    /**
     * Release held resources
     */
    public abstract void release();

    public boolean isDone() {
        return mState == State.DONE;
    }

    protected void setState(State state) {
        mState = state;
        if (State.DONE == mState) {
            Log.i(TAG, this + "is done");
        }
    }

    protected boolean isAllDataReady() {
        return mState == State.ALL_DATA_READY || mState == State.DONE;
    }

    protected enum State {
        INIT,
        SETUPED,

        /**
         * All data is ready, and it will end when the next node is read.
         */
        ALL_DATA_READY,

        /**
         * This Stage processing is completed
         */
        DONE
    }
}
