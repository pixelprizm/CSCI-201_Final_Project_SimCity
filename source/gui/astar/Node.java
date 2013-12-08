package gui.astar;

import java.util.*;

public interface Node {
    public boolean goalTest(Position o);
    public void printNode();
}