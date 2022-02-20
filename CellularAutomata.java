/*****************************************
  * A cellular automata simulating program. The program features two sets of rules; one for the game of life, and
 * one to simulate the spread of Covid-19 (in a hyper-simplified way). The probabilities I came up with for simulating
 * Covid have no basis in reality and are purely hypothetical.
 ***********************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.Math;

public class CellularAutomata extends BasicPanel implements ActionListener {
    int size;
    int[][] currentStates;
    int[][] nextStates;
    final int ALIVE = 1;//used for game of life, as well as to model cells pre-contact with Covid
    final int EMPTY = 0;//represents "dead" cells in game of life, and empty space in Covid simulation
    final int INFECTED = 2;//used for Covid simulation
    final int RECOVERED = 3;//used for Covid simulation
    final int DEAD = 4;//used for Covid simulation
    final int CELLSIZE = 10;
    final Color ALIVE_COLOR = Color.GREEN;
    final Color EMPTY_COLOR = Color.BLACK;
    final Color INFECTED_COLOR = Color.RED;
    final Color RECOVERED_COLOR = Color.BLUE;
    final Color DEAD_COLOR = Color.WHITE;
    final Color GRID_COLOR = new Color(50, 50, 50);
    JButton runButton;
    boolean running;

    CellularAutomata() {
        size = 50;
        running = false;
        setSize(size * CELLSIZE, size * CELLSIZE);
        runButton = new JButton("run");
        this.add(runButton);
        runButton.addActionListener(this);
        currentStates = new int[size][size];
        nextStates = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                currentStates[i][j] = EMPTY;
                nextStates[i][j] = EMPTY;
            }
        }
        currentStates[size/2 - 1][size/2] = ALIVE;
        currentStates[size/2][size/2] = INFECTED;
        currentStates[size/2 + 1][size/2] = ALIVE;
    }

    public static void main(String[] args) {
        CellularAutomata CA = new CellularAutomata();
        MyFrame frame = new MyFrame(CA);
        CA.animate(3);
    }

    @Override
    public void paintComponent(Graphics g) {
        displayCurrentStates(g);
        if (running)
            iterate();
    }

    int rule(int row, int column) {
        //count number of ALIVE neighbors
        int liveNeighbors=0;
        for (int i=row-1; i<=row+1;i++){
            for (int j=column-1;j<=column+1;j++){
                if (!(i==row && j==column)) {//! means if not true
                    liveNeighbors = liveNeighbors+currentStates[i][j];
                }
            }
        }
        //compute rule and return next state for cell
        if (currentStates[row][column]==ALIVE && (liveNeighbors==2 || liveNeighbors==3))
            return ALIVE;
        if (currentStates[row][column] ==EMPTY && liveNeighbors ==3)
            return ALIVE;
        return EMPTY;
    }

    int CovidRule(int row, int column) {
        if (currentStates[row][column] == ALIVE){
        //count number of INFECTED neighbors
            int infectedNeighbors=0;
            for (int i=row-1; i<=row+1;i++){
                for (int j=column-1;j<=column+1;j++){
                    if (!(i==row && j==column)) {
                        if (currentStates[i][j] == INFECTED) {
                            infectedNeighbors++;
                        }
                    }
                }
            }
            //compute rule and return next state for cell
            //healthy cell has probability of becoming infected equal to 15% times it's number of infected neighbors, or
            //a 100% chance with 7 or more infected neighbors
            double infectedRisk = (Math.random()*100);
            if (infectedRisk <= 15*infectedNeighbors){
                return INFECTED;
            }
            else
                return ALIVE;
        }
        if (currentStates[row][column] == INFECTED){//infected cell has 70% chance of staying infected, 15% chance of dying,
            //and 15% chance of recovering
            double diceRoll = (Math.random()*100);
            if (diceRoll < 70)
                return INFECTED;
            if (diceRoll > 70 && diceRoll < 85)
                return DEAD;
            if (diceRoll > 85)
                return RECOVERED;
        }
        if (currentStates[row][column] == DEAD)
            return DEAD;
        if (currentStates[row][column] == RECOVERED)
            return RECOVERED;

        return EMPTY;
    }


    void iterate(){
        for(int i=1;i<size-1;i++){
            for (int j=1;j<size-1;j++){
                nextStates[i][j] = rule(i,j);
            }
        }
        for (int i=0;i<size;i++)
            currentStates[i] = nextStates[i].clone();//setting 2D array equal to values in another requires extra work
    }

    void displayCurrentStates(Graphics g) {
        for (int i = 0; i < size; i++) {
            for (int j=0;j<size;j++){
                if (currentStates[i][j] == ALIVE) {
                    g.setColor(ALIVE_COLOR);
                }
                if (currentStates[i][j] == DEAD) {
                    g.setColor(DEAD_COLOR);
                }
                if (currentStates[i][j] == INFECTED) {
                    g.setColor(INFECTED_COLOR);
                }
                if (currentStates[i][j] == RECOVERED) {
                    g.setColor(RECOVERED_COLOR);
                }
                if (currentStates[i][j] == EMPTY) {
                    g.setColor(EMPTY_COLOR);
                }
                g.fillRect(CELLSIZE*j,CELLSIZE*i,CELLSIZE,CELLSIZE);
                g.setColor(GRID_COLOR);
                g.drawRect(CELLSIZE*j,CELLSIZE*i,CELLSIZE,CELLSIZE);
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            running = false;
            runButton.setText("run");
        }
        else {
            running = true;
            runButton.setText("stop");
        }
        //running = !running; would also work
        requestFocus();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                if (x>j*CELLSIZE && x<(j+1)*CELLSIZE && y > i*CELLSIZE && y<(i+1)*CELLSIZE){
                    if (currentStates[i][j] == ALIVE){
                        currentStates[i][j] = EMPTY;
                    }
                    if (currentStates[i][j] == EMPTY){
                        currentStates[i][j] = ALIVE;
                    }
                    //currentStates[i][j] = 1-currentStates[i][j]; would also work
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                if (x>j*CELLSIZE && x<(j+1)*CELLSIZE && y > i*CELLSIZE && y<(i+1)*CELLSIZE){
                    currentStates[i][j] = ALIVE;
                }
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if(c==' ')
            for (int i=0;i<size;i++){
                for (int j=0;j<size;j++){
                    currentStates[i][j] = EMPTY;
                }
            }

    }


}
