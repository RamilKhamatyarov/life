package asus.life;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartScreen extends Activity implements View.OnClickListener {

    private static final int ALERT_NONE = 0;
    private static final int ALERT_COLUMNS = 1;
    private static final int ALERT_ROWS = 2;
    private static final int ALERT_CELLS = 3;

    private static final int COLUMNS_MIN = 5;
    private static final int COLUMNS_MAX = 25;

    private static final int ROWS_MIN = 5;
    private static final int ROWS_MAX = 35;

    Button mButton;

    private int checkInputParameters(int cols, int rows, int cells){
        if (cols < COLUMNS_MIN || cols > COLUMNS_MAX) return ALERT_COLUMNS;
        if (rows < ROWS_MIN || rows > ROWS_MAX) return ALERT_ROWS;
        if (cells > rows * cols) return ALERT_CELLS;
        return ALERT_NONE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        mButton = (Button) findViewById(R.id.RunButton);
        mButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "It works", Toast.LENGTH_SHORT).show();

        EditText colsEditor = (EditText) findViewById(R.id.ColumnsEditor);
        EditText rowsEditor = (EditText) findViewById(R.id.RowsEditor);
        EditText cellsEditor = (EditText) findViewById(R.id.CellsEditor);

        int cols = Integer.parseInt(colsEditor.getText().toString());
        int rows = Integer.parseInt(rowsEditor.getText().toString());
        int cells = Integer.parseInt(cellsEditor.getText().toString());

        int alertCode = checkInputParameters(cols, rows, cells);
        if (alertCode != ALERT_NONE){
            showDialog(alertCode);
            return;
        }

        Intent intent = new Intent();
        intent.setClass(this, RunScreen.class);

        intent.putExtra(RunScreen.EXT_COLS, cols);
        intent.putExtra(RunScreen.EXT_ROWS, rows);
        intent.putExtra(RunScreen.EXT_CELLS, cells);

        startActivity(intent);
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id){
        DialogInterface.OnClickListener doNothing = new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){

            }
        };
        int alertMessage;

        switch(id){
            case ALERT_COLUMNS:alertMessage = R.string.alert_columns; break;
            case ALERT_ROWS:alertMessage = R.string.alert_rows; break;
            case ALERT_CELLS:alertMessage = R.string.alert_cells; break;
            default:return null;
        }

        return new AlertDialog.Builder(this).setMessage(alertMessage).setNeutralButton(R.string.ok, doNothing).create();
    }
}
