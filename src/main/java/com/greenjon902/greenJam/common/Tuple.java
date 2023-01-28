package com.greenjon902.greenJam.common;

import java.util.Objects;

public abstract class Tuple {
    public static class Two<A, B> extends Tuple {
        public final A A;
        public final B B;

        public Two(A a, B b) {
            A = a;
            B = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Two<?, ?> two = (Two<?, ?>) o;
            return Objects.equals(A, two.A) && Objects.equals(B, two.B);
        }

        @Override
        public int hashCode() {
            return Objects.hash(A, B);
        }

        @Override
        public String toString() {
            return "Two{" +
                    "A=" + A +
                    ", B=" + B +
                    '}';
        }
    }
}
