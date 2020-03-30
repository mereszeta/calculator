package pl.edu.agh.calculator;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LogicService extends Service {
    private final IBinder mBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        LogicService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LogicService.this;
        }
    }

    public double add(double n1, double n2){
        return n1 + n2;
    }

    public double subtract(double n1, double n2){
        return n1 - n2;
    }

    public double multiply(double n1, double n2){
        return n1 * n2;
    }

    public double divide(double n1, double n2){
        return n1 / n2;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
