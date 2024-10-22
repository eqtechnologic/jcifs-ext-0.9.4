package jcifs.rap;

public abstract class Info implements Cloneable {

    public int getLevel() {
        StringBuffer level = new StringBuffer(getClass().getName());
        int index = level.length();
        while (Character.isDigit(level.charAt(--index)));
        level.delete(0, index + 1);
        return (level.length() > 0) ? Integer.parseInt(level.toString()) : 0;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException();
        }
    }

    public abstract String getDescriptor();

    public abstract void read(Buffer buffer);

    public abstract void write(Buffer buffer);

}
