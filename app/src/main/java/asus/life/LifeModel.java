package asus.life;

import java.util.Random;

/**
 * Created by Asus on 27.01.2016.
 */
public class LifeModel {
    private static final Byte CELL_ALIVE = 1;
    private static final Byte CELL_DEAD = 0;

    private static final byte NEIGHBOUR_MIN = 2;
    private static final byte NEIGHBOUR_MAX = 3;
    private static final byte NEIGHBOUR_BORN = 3;

    private static int mCols;
    private static int mRows;
    private Byte[][] mCells;

    /**
     * Конструктор
     */
    public LifeModel(int rows, int cols, int cellsNumber){
        mCols = cols;
        mRows = rows;
        mCells = new Byte[mRows][mCols];

        initValues(cellsNumber);
    }

    /**
     * Инициализация первого поколения случайным образом
     * @param cellsNumber количество клеток в первом поколении
     */
    private void initValues(int cellsNumber){
        for (int i = 0; i < mRows; ++i)
            for (int j = 0; j < mCols; ++j)
                mCells[i][j] = CELL_DEAD;
        Random rnd = new Random(System.currentTimeMillis());
        for (int i = 0; i < cellsNumber; i++) {
            int cc;
            int cr;
            do{
                cc = rnd.nextInt(mCols);
                cr = rnd.nextInt(mRows);
            }while(isCellAlive(cr, cc));
            mCells[cr][cc] = CELL_ALIVE;
        }
    }

    /**
     * Переход к следующему поколению
     */
    public void next(){
        Byte[][] tmp = new Byte[mRows][mCols];

        // цикл по всем клеткам
        for (int i = 0; i < mRows; ++i) {
            for (int j = 0; j < mCols; ++j) {
                // вычисляет количтество соседей для каждой клетки
                int n = itemAt(i - 1, j - 1) + itemAt(i - 1, j) + itemAt(i - 1, j + 1) +
                        itemAt(i, j - 1) + itemAt(i, j + 1) +
                        itemAt(i + 1, j - 1) + itemAt(i + 1, j) + itemAt(i + 1, j + 1);

                tmp[i][j] = mCells[i][j];
                if (isCellAlive(i, j)){
                    // если клетка жива, а соседей у нее не достаточно или слишком много, клетка умирает
                    if (n < NEIGHBOUR_MIN || n > NEIGHBOUR_MAX) tmp[i][j] = CELL_DEAD;
                } else {
                    // если у пустой клетки ровно столько соседей, сколько нужно, она оживает
                    if (n == NEIGHBOUR_BORN) tmp[i][j] = CELL_ALIVE;
                }
            }
        }
        mCells = tmp;
    }

    /***
     * @return Размер поля
     */
    public int getCount(){
        return mCols * mRows;
    }
    /**
     * @param col Номер строки
     * @param row Номер столбца
     * @return Значение ячейки, находящейся в указанной строке, в указанном столбце
     */
    private Byte itemAt (int row, int col){
        if (row < 0 || row >= mRows || col < 0 || col >= mCols) return 0;
        return mCells[row][col];
    }

    /**
     * @param row Номер строки
     * @param col Номер столбца
     * @return Жива ли клетка, находящейся в указанной строке, в казанном столбце
     */
    public Boolean isCellAlive(int row, int col){
        return itemAt(row, col) == CELL_ALIVE;
    }

    /**
     * @param position Позиция (для клетки [row, col], вычисляется как row * mCols + col)
     * @return Жива ли клетка, находящаяся в указанной позиции
     */
    public Boolean isCellAlive(int position){
        int r = position / mCols;
        int c = position % mCols;

        return isCellAlive(r,c);
    }
}
