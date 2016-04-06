package asus.life;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;


/**
 * Created by Asus on 26.01.2016.
 */
public class RunScreen extends Activity implements View.OnClickListener {

    public static final String EXT_COLS = "cols";
    public static final String EXT_ROWS = "rows";
    public static final String EXT_CELLS = "cells";

    Button mCloseButton;

    private GridView mLifeGrid;
    private LifeAdapter mAdapter;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run);

        mCloseButton = (Button) findViewById(R.id.close);
        mCloseButton.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        int cols = extras.getInt(EXT_COLS);
        int rows = extras.getInt(EXT_ROWS);
        int cells = extras.getInt(EXT_CELLS);
        mAdapter = new LifeAdapter(this, cols, rows, cells);

        mLifeGrid = (GridView) findViewById(R.id.life_grid);
        mLifeGrid.setAdapter(mAdapter);
        mLifeGrid.setNumColumns(cols);
        mLifeGrid.setEnabled(false);
        mLifeGrid.setStretchMode(GridView.NO_STRETCH);

        mHandler = new Handler();
        mHandler.postDelayed(mUpdateGeneration, 1000);
    }

    private Runnable mUpdateGeneration = new Runnable(){
        public void run(){
            mAdapter.next();
            mLifeGrid.setAdapter(mAdapter);

            mHandler.postDelayed(mUpdateGeneration, 1000);
        }
    };

    public void onClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.submit_close).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                finish();
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){

            }
        }).show();
    }

    class LifeAdapter extends BaseAdapter {

        private Context mContext;

        private LifeModel mLifeModel;

        public LifeAdapter (Context context, int cols, int rows, int cells){
            mContext = context;
            mLifeModel = new LifeModel(cols, rows, cells);
        }

        public void next(){
            mLifeModel.next();
        }

        /**
         *  Возвращает количество элементов в GridView
         */
        public int getCount(){
            return mLifeModel.getCount();
        }

        /**
         * Возвращает объект, хранящийся под номером position
         */
        public Object getItem(int position){
            return mLifeModel.isCellAlive(position);
        }

        /**
         * Возвращает идентификатор элемента, хранящегося в под номером position
         */
        public long getItemId(int position){
            return position;
        }

        /**
         * Возвращает элемент управления, который будет выведен под номером position
         */
        public View getView(int position, View convertView, ViewGroup parent){
            ImageView view; //выводится картинка

            if (convertView == null) {
                view = new ImageView(mContext);

                // задаем атрибуты
                view.setLayoutParams(new GridView.LayoutParams(10, 10));
                view.setAdjustViewBounds(false);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                view.setPadding(1, 1, 1, 1);
            } else view = (ImageView) convertView;

            // выводим черный квадратик, если клетка пустая, и зеленый, если она жива
            view.setImageResource(mLifeModel.isCellAlive(position) ? R.drawable.cell : R.drawable.empty);

            return view;
        }
    }
}
