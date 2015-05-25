package com.silreg.recogservice;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.silreg.recogservice.Train;
import com.silreg.recogservice.Judge;

/**
 * Created by XRH on 2015-05-24.
 */
public class RecognizationService extends IntentService {
    public static final String EXTRA_MODE = "EXTRA_TAG";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RecognizationService(String name) {
        super(RecognizationService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

            boolean Trainflag = intent.getBooleanExtra(EXTRA_MODE, false);
             if(Trainflag)
             {
                 Train t1 = new Train(getFilesDir().getPath());
                 try {
                     if(t1.train())
                         System.out.println("traing complete");
                 } catch (Exception e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
//                     return false;
                 }

             }
             else
             {
                 Judge jj = new Judge(getFilesDir().getPath());
                 try {
                     if(jj.isOwner())
                         System.out.println("Bingo!		"+jj.getProbility());
                     else
                         System.out.println("Son of Bitch.	"+jj.getProbility());
                 } catch (Exception e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                 }
             }



    }
}
