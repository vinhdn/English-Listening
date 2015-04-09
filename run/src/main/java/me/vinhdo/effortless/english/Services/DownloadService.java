package me.vinhdo.effortless.english.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.vinhdo.effortless.english.EffortlessApplication;
import me.vinhdo.effortless.english.Models.Lession;
import me.vinhdo.effortless.english.Models.Models;
import me.vinhdo.effortless.english.R;
import me.vinhdo.effortless.english.Utils.Constants;

/**
 * Created by Vinh on 3/12/15.
 */
public class DownloadService extends Service {
    private RemoteViews mRemoteViews;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;

    private String TAG = DownloadService.class.getName();
    private int ID_NOTIFI_DOWNLOAD = 111;
    private DownloadBinder mBinder = new DownloadBinder();
    private EffortlessApplication mApp;

    public class DownloadBinder extends Binder{
        public DownloadService getService(){
            return DownloadService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = (EffortlessApplication) getApplication();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void saveOfflineLession(Lession lession) {
        if (lession != null) {
            try {
                Models model = lession;
                while (model != null && model.getId() > 0) {
                    mApp.getLocalStorage().insertModels(model);
                    model = model.getParent();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            File outputFile = new File(Constants.FOLDER_LESSION + "/" +
                    lession.getParent().getId(),
                    lession.getId() + ".mp3");
            if (outputFile.exists())
                return;
            new DownloadTask(lession.getId(),
                    lession.getParent().getId(),
                    lession.getName(),
                    lession.getLink()).execute();
        }
    }

    public void broadCastFinishDownload(boolean isSuccess) {
        Log.d("HOAN", "broadcast Finish Download");
        Intent intent = new Intent();
        intent.setAction(PlayerBroadcast.BR_FINISH_DOWNLOAD);
        intent.putExtra("Success",isSuccess);
        sendBroadcast(intent);
    }

    private class DownloadTask extends AsyncTask<Void, Integer, String> {

        int id, idPar;
        String name, link;
        android.support.v4.app.NotificationCompat.Builder mBuilder;

        public DownloadTask(int ID, int idPar, String name, String link) {
            this.id = ID;
            this.idPar = idPar;
            this.name = name;
            this.link = link;
        }

        private void Download() throws Exception {
            try {
                URL url = new URL(link);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                File outputFile = new File(Constants.FOLDER_LESSION + "/" + idPar, id + ".mp3");
                if (!outputFile.getParentFile().exists())
                    outputFile.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                long lengthCurrent = 0;
                long lengthFile = c.getContentLength();
                int percent = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    lengthCurrent += len1;
                    if ((lengthCurrent * 100) / lengthFile > percent) {
                        percent = (int) ((lengthCurrent * 100) / lengthFile);
                        if (Build.VERSION.SDK_INT >= 11) {
                            mBuilder.setProgress(100, Math.abs(percent), false);
                            mNotificationManager.notify(ID_NOTIFI_DOWNLOAD, mBuilder.build());
                        }else{
                            notificationNormal("Saving in process - " + percent + "%",name,ID_NOTIFI_DOWNLOAD);
                        }
                    }
                    fos.write(buffer, 0, len1);

                }
                fos.close();
                is.close();// till here, it works fine - .apk is download to my
                // sdcard in download file
                broadCastFinishDownload(true);
                if (Build.VERSION.SDK_INT >= 11) {
                    mBuilder.setContentText("Download complete")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    mNotificationManager.notify(ID_NOTIFI_DOWNLOAD, mBuilder.build());
                }else{
                    notificationNormal("Download complete!",name,ID_NOTIFI_DOWNLOAD);
                }
                mNotificationManager.cancel(ID_NOTIFI_DOWNLOAD);
            } catch (Exception e) {
                Log.d("ERROR", e.toString());
                if (Build.VERSION.SDK_INT >= 11) {
                    mBuilder.setContentText("Download error!")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    mNotificationManager.notify(ID_NOTIFI_DOWNLOAD, mBuilder.build());
                }else{
                    notificationNormal("Download error!",name,ID_NOTIFI_DOWNLOAD);
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            broadCastFinishDownload(false);
            stopSelf();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //Update(params[0], params[1]);
                Download();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (Build.VERSION.SDK_INT >= 11) {
                mBuilder = new NotificationCompat.Builder(getApplicationContext());
                mBuilder.setContentTitle(name)
                        .setContentText("saving in progress")
                        .setSmallIcon(R.drawable.icon_downloaded);
                mBuilder.setProgress(100, 0, false);
                mNotificationManager.notify(ID_NOTIFI_DOWNLOAD, mBuilder.build());
            } else {
                notificationNormal("saving in progress",name,ID_NOTIFI_DOWNLOAD);
            }

        }

    }

    private void notificationNormal(String content,String subtitle,int id) {
        Log.d(TAG, "notificationNormal");
//        Context context = getApplicationContext();
//        Notification notification = new Notification(
//                R.drawable.icon_downloaded, content, System.currentTimeMillis());
        PendingIntent pendingintent = PendingIntent.getActivity(
                this,
                0,
                (new Intent("android.intent.action.MAIN")).addCategory(
                        "android.intent.category.LAUNCHER").setComponent(
                        getPackageManager().getLaunchIntentForPackage(
                                getPackageName()).getComponent()), 0);
//        notification.contentView = mRemoteViews;
//        notification.setLatestEventInfo(context, content,
//                subtitle, pendingintent);
////        startForeground(id,notification);
//        mNotificationManager.notify(id,mNotificationBuilder.build());
        Notification noti = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.icon_downloaded)
                .setTicker(subtitle)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(subtitle)
                .setContentText(content)
                .setContentIntent(pendingintent)
                        //At most three action buttons can be added
                .setAutoCancel(true).build();
        mNotificationManager.notify(id,noti);
        Log.d(TAG, "start nhe");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
