package com.gtomato.android.util;

import android.util.SparseArray;

import java.util.ArrayList;

/**
 * Multi-dimension FIFO SparseArray.
 *
 * @see SparseArray
 *
 * @author  sunny-chung
 */
public class MultiSparseArray<E> {

    private final SparseArray<ArrayList<E>> mArray;

    public MultiSparseArray() {
        mArray = new SparseArray<>();
    }

    public MultiSparseArray(int initialCapacity) {
        mArray = new SparseArray<>(initialCapacity);
    }

    public void put(int key, E value) {
        ArrayList<E> values = mArray.get(key);
        if (values == null) values = new ArrayList<>();
        values.add(value);
        mArray.put(key, values);
    }

    public E pop(int key) {
        ArrayList<E> values = mArray.get(key);
        if (values != null && values.size() > 0) {
            E value = values.get(0);
            values.remove(0);
            return value;
        }
        return null;
    }

    public int size() {
        return mArray.size();
    }

    public int keyAt(int index) {
        return mArray.keyAt(index);
    }

    public ArrayList<E> valuesAt(int index) {
        return mArray.valueAt(index);
    }
}
