package mazes.generators.maze;

import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import misc.graphs.Graph;

import java.util.Random;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.

        //Randomize the weights of the walls
        Random rand = new Random();
        for (Wall wall : maze.getWalls()) {
            wall.setDistance(rand.nextDouble());
        }
        //Create a graph representing the walls and rooms
        Graph<Room, Wall> graph = new Graph<>(maze.getRooms(), maze.getWalls());
        //Use spanning trees to make a graph that can be solved with:
        //  1. only 1 solution
        //  2. taking out the least amount of walls
        ISet<Wall> toRemove = graph.findMinimumSpanningTree();
        //Reset the weights of the walls
        for (Wall wall : maze.getWalls()) {
            wall.resetDistanceToOriginal();
        }
        return toRemove;
    }
}
