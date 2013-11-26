package aStar;

import java.util.*;

public interface Node {
    public boolean goalTest(Object o);
    public void printNode();
}
