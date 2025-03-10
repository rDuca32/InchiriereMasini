    package domain;

    public abstract class EntityConverter <T extends Entitate> {
        public abstract String toString(T shape);

        public abstract T fromString(String string);
    }
