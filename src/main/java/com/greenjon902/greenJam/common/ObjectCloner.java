package com.greenjon902.greenJam.common;

public class ObjectCloner {
    /**
     * A utility to clone objects as the developers of java's built in clone system forgot to make it usable.
     */
    public static <T> T clone(T original) {
        if (original instanceof AstNode node) {

            AstNode clone = new AstNode(new Object[node.storage.length]);
            for (int i = 0; i < node.storage.length; i++) {
                clone.storage[i] = clone(node.storage[i]);
            }
            return (T) clone;
        } else if (original instanceof String string) {
            return (T) string; // Strings don't need cloning
        } else {
            throw new IllegalArgumentException("Failed to clone object of type " + original.getClass());
        }
    }
}
