package com.operatorsapp.utils;

import android.util.SparseArray;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableSparseArray<E> extends SparseArray<E> implements Serializable {

    private static final long serialVersionUID = 824056059663678000L;

    public SerializableSparseArray(int capacity){
        super(capacity);
    }

    public SerializableSparseArray(){
        super();
    }

    /**
     * This method is private but it is called using reflection by java
     * serialization mechanism. It overwrites the default object serialization.
     *
     * <br/><br/><b>IMPORTANT</b>
     * The access modifier for this method MUST be set to <b>private</b> otherwise
     * will be thrown.
     *
     * @param oos
     *            the stream the data is stored into
     * @throws IOException
     *             an exception that might occur during data storing
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        Object[] data = new  Object[size()];

        for (int i=data.length-1;i>=0;i--){
            Object[] pair = {keyAt(i),valueAt(i)};
            data[i] = pair;
        }
        oos.writeObject(data);
    }

    /**
     * This method is private but it is called using reflection by java
     * serialization mechanism. It overwrites the default object serialization.
     *
     * <br/><br/><b>IMPORTANT</b>
     * The access modifier for this method MUST be set to <b>private</b> otherwise
     * will be thrown.
     *
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        Object[] data = (Object[]) ois.readObject();
        for (int i=data.length-1;i>=0;i--){
            Object[] pair = (Object[]) data[i];
            this.append((Integer)pair[0],(E)pair[1]);
        }
        return;
    }


}