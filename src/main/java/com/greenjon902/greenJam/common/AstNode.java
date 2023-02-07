package com.greenjon902.greenJam.common;

import java.util.Arrays;

public class AstNode {
    public final Object[] storage;

    public AstNode(Object... storage) {
        this.storage = storage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AstNode astNode = (AstNode) o;
        return Arrays.equals(storage, astNode.storage);
    }

    @Override
    public String toString() {
        return "AstNode{" +
                "storage=" + Arrays.toString(storage) +
                '}';
    }
}
