package Topics.Design;

public class MyArrayList {
    private static final int SIZE_FACTOR = 4;
    private Object data[];
    private int index;
    private int size;

    public MyArrayList() {
        this.data = new Object[SIZE_FACTOR];
        this.size = SIZE_FACTOR;
    }

    public void add(Object obj) {
        if (index == size-1) {
            increaseSizeAndReallocate();
        }
        data[index++] = obj;
    }

    public Object get(int i) throws Exception{
        if (i < 0 || i > index-1) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        return data[i];
    }

    public void remove(int i) {
        if (i < 0 || i > index-1) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        System.arraycopy(data, i+1, data, i, index-i);
        index--;
    }

    private void increaseSizeAndReallocate() {
        size += SIZE_FACTOR;
        Object newData[] = new Object[size];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }
}
