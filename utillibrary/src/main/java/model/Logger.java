package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michelle on 2018/8/20.
 */

public class Logger {

    protected static final String TAG = "Centerm";
    private static boolean debug=false;

    private static List<String> logList = new ArrayList<>();
    private Logger() {
    }

    public static void setLogDebug(boolean isDebug){
        debug=isDebug;
        if (!logList.isEmpty()){
            logList.clear();
        }
    }

    public static List<String> getLogList(){
        return logList;
    }
    /**
     * Send a VERBOSE log message.
     *
     * @param msg
     *            The message you would like logged.
     */
    public static void v(String msg) {
        if (debug) {
            logList.add(msg);
            android.util.Log.v(TAG, buildMessage(msg));
        }
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param msg
     *            The message you would like logged.
     * @param thr
     *            An exception to log
     */
    public static void v(String msg, Throwable thr) {
        if (debug){
            logList.add(msg);
            android.util.Log.v(TAG, buildMessage(msg), thr);
        }
    }

    /**
     * Send a DEBUG log message.
     *
     * @param msg
     */
    public static void d(String msg) {
        if (debug) {
            logList.add(msg);
            android.util.Log.d(TAG, buildMessage(msg));
        }
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param msg
     *            The message you would like logged.
     * @param thr
     *            An exception to log
     */
    public static void d(String msg, Throwable thr) {
        if (debug) {
            logList.add(msg);
            android.util.Log.d(TAG, buildMessage(msg), thr);
        }
    }

    /**
     * Send an INFO log message.
     *
     * @param msg
     *            The message you would like logged.
     */
    public static void i(String msg) {
        if (debug) {
            logList.add(msg);
            android.util.Log.i(TAG, buildMessage(msg));
        }
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param msg
     *            The message you would like logged.
     * @param thr
     *            An exception to log
     */
    public static void i(String msg, Throwable thr) {
        if (debug) {
            logList.add(msg);
            android.util.Log.i(TAG, buildMessage(msg), thr);
        }
    }

    /**
     * Send an ERROR log message.
     *
     * @param msg
     *            The message you would like logged.
     */
    public static void e(String msg) {
        if (debug){
            logList.add(msg);
            android.util.Log.i(TAG, buildMessage(msg));
        }
    }

    /**
     * Send a WARN log message
     *
     * @param msg
     *            The message you would like logged.
     */
    public static void w(String msg) {
        if (debug){
            logList.add(msg);
            android.util.Log.i(TAG, buildMessage(msg));
        }
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param msg
     *            The message you would like logged.
     * @param thr
     *            An exception to log
     */
    public static void w(String msg, Throwable thr) {
        if (debug) {
            logList.add(msg);
            android.util.Log.i(TAG, buildMessage(msg), thr);
        }
    }

    /**
     * Send an empty WARN log message and log the exception.
     *
     * @param thr
     *            An exception to log
     */
    public static void w(Throwable thr) {
        if (debug)
            android.util.Log.w(TAG, buildMessage(""), thr);
    }

    /**
     * Send an ERROR log message and log the exception.
     *
     * @param msg
     *            The message you would like logged.
     * @param thr
     *            An exception to log
     */
    public static void e(String msg, Throwable thr) {
        if (debug) {
            logList.add(msg);
            android.util.Log.i(TAG, buildMessage(msg), thr);
        }
    }

    /**
     * Building Message
     *
     * @param msg
     *            The message you would like logged.
     * @return Message String
     */
    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        return new StringBuilder().append(caller.getClassName()).append(".").append(caller.getMethodName()).append("(): ").append(msg).toString();

    }

}
