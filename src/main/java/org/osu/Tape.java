package org.osu;

import java.util.List;

public class Tape {
    int head;
    List<Integer> content;

    public Tape(int head, List<Integer> content) {
        this.head = head;
        this.content = content;
    }
}
