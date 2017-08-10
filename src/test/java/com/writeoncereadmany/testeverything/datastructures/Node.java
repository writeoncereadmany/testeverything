package com.writeoncereadmany.testeverything.datastructures;

public class Node implements ConsList {

    private final String value;
    private final ConsList list;

    public Node(String value, ConsList list) {
        this.value = value;
        this.list = list;
    }
}
