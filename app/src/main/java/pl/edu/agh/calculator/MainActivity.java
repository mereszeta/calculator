package pl.edu.agh.calculator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText n1Number;
    private EditText n2Number;
    private TextView result;
    private Button addButton;
    private Button subtractButton;
    private Button multiplyButton;
    private Button divideButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        n1Number = (EditText) findViewById(R.id.edit_number1);
        n2Number = (EditText) findViewById(R.id.edit_number2);
        result = (TextView) findViewById(R.id.edit_result);
        addButton = (Button) findViewById(R.id.button_add);
        subtractButton = (Button) findViewById(R.id.button_subtract);
        multiplyButton = (Button) findViewById(R.id.button_multiply);
        divideButton = (Button) findViewById(R.id.button_divide);
        progressBar = (ProgressBar) findViewById(R.id.progress_horizontal);

    }

    LogicService logicService;
    boolean mBound = false;
    private ServiceConnection logicConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            LogicService.LocalBinder binder = (LogicService.LocalBinder) service;
            logicService = binder.getService();
            mBound = true;
            Toast.makeText(MainActivity.this, "Logic Service Connected!",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            logicService = null;
            mBound = false;
            Toast.makeText(MainActivity.this, "Logic Service Disconnected!",
                    Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            this.bindService(new Intent(MainActivity.this, LogicService.class),
                    logicConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            mBound = false;
            this.unbindService(logicConnection);
        }
    }

    public void addButtonClick(View button) {
        if (mBound) {
            double first = getEditableFieldValue(n1Number);
            double second = getEditableFieldValue(n2Number);
            if (first != Double.NEGATIVE_INFINITY && second != Double.NEGATIVE_INFINITY) {
                result.setText(Double.toString(logicService.add(first, second)));
            } else {
                result.setText("INVALID");
            }
        }
    }

    public void subtractButtonClick(View button) {
        if (mBound) {
            double first = getEditableFieldValue(n1Number);
            double second = getEditableFieldValue(n2Number);
            if (first != Double.NEGATIVE_INFINITY && second != Double.NEGATIVE_INFINITY) {
                result.setText(Double.toString(logicService.subtract(first, second)));
            } else {
                result.setText("INVALID");
            }
        }
    }


    public void multiplyButtonClick(View button) {
        if (mBound) {
            double first = getEditableFieldValue(n1Number);
            double second = getEditableFieldValue(n2Number);
            if (first != Double.NEGATIVE_INFINITY && second != Double.NEGATIVE_INFINITY) {
                result.setText(Double.toString(logicService.multiply(first, second)));
            } else {
                result.setText("INVALID");
            }
        }
    }


    public void divideButtonClick(View button) {
        if (mBound) {
            double first = getEditableFieldValue(n1Number);
            double second = getEditableFieldValue(n2Number);
            if (first != Double.NEGATIVE_INFINITY && second != Double.NEGATIVE_INFINITY && second != 0d) {
                result.setText(Double.toString(logicService.divide(first, second)));
            } else {
                result.setText("INVALID");
            }
        }
    }

    public void piButtonClick(View v) {
        progressBar.setProgress(0);
        new PiComputeTask().execute();
    }

    private double getEditableFieldValue(EditText textField) {
        Editable editable = textField.getText();
        if (editable == null) {
            Toast.makeText(MainActivity.this, "Error getting editable", Toast.LENGTH_LONG).show();
            return Double.NEGATIVE_INFINITY;
        }
        try {
            return Double.parseDouble(editable.toString());
        } catch (NumberFormatException nfe) {
            Toast.makeText(MainActivity.this, "Invalid input value", Toast.LENGTH_LONG).show();
            return Double.NEGATIVE_INFINITY;
        }

    }


    private class PiComputeTask extends AsyncTask<Void, Integer, Double> {
        private int N = 100000;

        protected Double doInBackground(Void... voids) {
            int k = 0;
            for (int i = 0; i < N; i++) {
                double x = Math.random();
                double y = Math.random();
                if (x * x + y * y <= 1) k += 1;
                publishProgress((int) ((i / (float) N) * 100));
            }
            publishProgress();
            return 4. * k / N;
        }

        protected void onPostExecute(Double result) {
            n1Number.setText(result.toString());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values.length > 0) {
                progressBar.setProgress(values[0]);
            }

        }
    }
}
