package GameUI;

import Mechanics.Field;
import Mechanics.NewCell;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;

public class Canvas extends java.awt.Canvas implements MouseListener {
    // Graphics
    protected int fieldOffset;
    protected int fieldWidth;
    protected int fieldDx;
    protected int n = 13;
    protected GraphicalCell[][] shapes;
    // Mechanics
    protected Field field;

    Canvas() {
        super();
        addMouseListener(this);
        field = new Field(n);
        shapes = new GraphicalCell[n][n];
    }

    Canvas(int n) {
        super();
        this.n = n;
        addMouseListener(this);
        field = new Field(n);
        shapes = new GraphicalCell[n][n];
    }

    public void repaint(Rectangle area) {
        int x = (int) area.getX();
        int y = (int) area.getY();
        int width = (int) area.getWidth();
        int height = (int) area.getHeight();
        repaint(x, y, width, height);
    }

    public void setN(int n) {
        this.n = n;
        field = new Field(n);
        shapes = new GraphicalCell[n][n];
    }

    private void computeFieldWidth() {
        int psz = 1;
        fieldDx = (getWidth() - psz) / n; // DEFAULT_WIDTH = n * (psz + csz) + psz;
        fieldWidth = fieldDx * n + psz;
        fieldOffset = (getWidth() - fieldWidth) / 2;
    }

    public int alive() {
        return field.alive;
    }

    public int generation() {
        return field.generation;
    }

    @Override
    public void paint(Graphics g) {
        setSize(getHeight(), getHeight());
        System.out.println("cavasHeight=" + getHeight());
        computeFieldWidth();
        int psz = 1;
        int npoints = 4;
        for (int i = 0, y = fieldOffset; i < n; i++, y += fieldDx) {
            for (int j = 0, x = fieldOffset; j < n; j++, x += fieldDx) {
                int[] xpoints = {
                        (x + psz),
                        (x + fieldDx),
                        (x + fieldDx),
                        (x + psz)
                };
                int[] ypoints = {
                        (y + psz),
                        (y + psz),
                        (y + fieldDx),
                        (y + fieldDx)
                };
                shapes[i][j] = new GraphicalCell(xpoints, ypoints, npoints);
                NewCell cell = field.getCell(i, j);
                shapes[i][j].setCell(cell);
                cell.setShape(shapes[i][j]);
                shapes[i][j].draw(getGraphics());
            }
        }
    }

    public void step() {
        HashSet<NewCell> changed = field.step();
        for (NewCell cell : changed) {
            GraphicalCell shape = cell.getShape();
            shape.draw(getGraphics());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int ex = e.getX() - fieldOffset;
        if (ex < 0 || ex >= fieldWidth) return;

        int ey = e.getY() - fieldOffset;
        if (ey < 0 || ey >= fieldWidth) return;

        int j = ex / fieldDx;
        int i = ey / fieldDx;
        field.reverseState(i, j);
        shapes[i][j].draw(getGraphics());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //do nothing
    }
}
