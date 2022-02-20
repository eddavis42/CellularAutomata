# CellularAutomata
Cellular Automata program with 2 sets of rules for different simulations: once which simulates the Game of Life, and one which roughly simulates the spread of Covid-19 or other diseases. I originally created this program in Fall of 2021 for a lab assignment in CS-152 at UNM. The program uses a library in the "BasicPanel.jar" file which was created by Dr. Leah Buechley.

The program generates a grid of "cells" with multiple possible states including "ALIVE" designated by green color, "EMPTY" designated by black, "INFECTED" designated by red, "RECOVERED" designated by blue, and "DEAD" designated by white. By default the Covid simulation is run with the grid starting fully alive with one infected cell in the center. Each ALIVE cell with one or more INFECTED neighboring cells has a chance of becoming INFECTED with each iteration, with the chance increasing as the number of INFECTED neighbors increases. Each INFECTED cell has a chance of staying infected, changing to RECOVERED, or changing to DEAD with each iteration.

Pressing the space bar will clear the grid, turning all cells to EMPTY. Clicking and dragging on any empty cells will change them to ALIVE.
To change the starting grid, replace the variable ALIVE in lines 45 and 46 in the constructor method of the java file with EMPTY or other states.

                currentStates[i][j] = ALIVE;
                nextStates[i][j] = ALIVE;
                
To run the Game of Life simulation, change the method on line 130 from:

                nextStates[i][j] = CovidRule(i,j);
                
to:

                nextStates[i][j] = rule(i,j);

Due to how I built the iterating, the cells on the borders will not be changed through successive iterations, but still influence neighboring cells. Changing lines 45 and 46 in the constructor to:

                currentStates[i][j] = EMPTY;
                nextStates[i][j] = EMPTY;
                
will run a more traditional game of life simulation. Leaving the lines as they are will produce an interesting pattern.
