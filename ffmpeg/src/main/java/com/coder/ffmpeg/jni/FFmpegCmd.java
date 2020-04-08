package com.coder.ffmpeg.jni;

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
 class FFmpegCmd {

    private static OnFFmpegCmdListener mCmdListener;
    private static int mProgress = 0;

    public static boolean DEBUG = true;


    static {
        System.loadLibrary("avdevice");
        System.loadLibrary("avutil");
        System.loadLibrary("avcodec");
        System.loadLibrary("swresample");
        System.loadLibrary("avformat");
        System.loadLibrary("swscale");
        System.loadLibrary("avfilter");
        System.loadLibrary("postproc");
        System.loadLibrary("ffmpeg-invoke");
    }

    private static native int run(int cmdLen, String[] cmd);

    static int runCmd(String[] cmd){
        cmd = command(cmd);
        return run(cmd.length,cmd);
    }

    static int runCmd(String[] cmd,OnFFmpegCmdListener cmdListener){
        cmd = command(cmd);
        mCmdListener = cmdListener;
        int result = run(cmd.length,cmd);
        if (mCmdListener!=null){
            mCmdListener = null;
        }
        return result;
    }

    static String[] command(String[] cmd){
        String[] cmds = new String[cmd.length+1];
        for (int i = 0; i < cmds.length; i++) {
            if (i<1){
                cmds[i] = cmd[i];
            }else if (i == 1){
                cmds[i] = "-d";
            }else {
                cmds[i] = cmd[i-1];
            }
        }
        return DEBUG?cmds:cmd;
    }

    static native int getProgress();

    static void onProgress(int progress){
        if (mCmdListener!=null){
            mCmdListener.onProgress(progress);
        }
    }

    public interface OnFFmpegCmdListener{
        void onProgress(int progress);
    }
}
