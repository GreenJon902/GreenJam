package com.greenjon902.greenJam.common;

import java.util.Arrays;

public class AstNode implements Printable {
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
    public int hashCode() {
        return Arrays.hashCode(storage);
    }

    @Override
    public String toString() {
        return "AstNode{" +
                "storage=" + Arrays.toString(storage) +
                '}';
    }

    @Override
    public String format() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : storage) {
            if (object instanceof Printable) {
                stringBuilder.append(" ").append(((Printable) object).format().replace("\n", "\n ")).append("\n");
            } else {
                stringBuilder.append(" ").append(object.toString().replace("\n", "\n ")).append("\n");
            }
        }
        stringBuilder.setCharAt(0, '>');

        if (stringBuilder.charAt(stringBuilder.length() - 1) == '\n') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }
}
