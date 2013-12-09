package gui.astar;

import java.util.*;

public interface Node {
    public boolean goalTest(Object endingState);
    public void printNode();
}