package com.sf.libplayer.wav;

import android.util.Log;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by sufan on 2018/4/18.
 */

public class WavFileReader {
    private static final String TAG="WavFileReader";

    private DataInputStream dis;
    private WavFileHeader mWavFileHeader;


    public WavFileHeader getWavFileHeader(){
        return mWavFileHeader;
    }

    public boolean openFile(String filePath){
        if(dis!=null){
            closeFile();
        }
        try {
            dis=new DataInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return readHeader();
    }

    public void closeFile(){
        if(dis!=null){
            try {
                dis.close();
                dis=null;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public int readData(byte[] buffer, int offset, int count) {
        if (dis == null || mWavFileHeader == null) {
            return -1;
        }

        try {
            int nbytes = dis.read(buffer, offset, count);
            if (nbytes == -1) {
                return 0;
            }
            return nbytes;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }


    /**
     *read和read(byte b[])
     * read每次读取一个字节，返回0-255的int字节值
     * read(byte b[])读取一定数量的字节，返回实际读取的字节的数量
     */
    private boolean readHeader(){
        if(dis==null){
            return false;
        }

        WavFileHeader header=new WavFileHeader();
        byte[] intValue = new byte[4];
        byte[] shortValue = new byte[2];

        try {
            header.mChunkID = "" + (char) dis.readByte() + (char) dis.readByte() + (char) dis.readByte() + (char) dis.readByte();
            Log.d(TAG, "Read file chunkID:" + header.mChunkID);

            dis.read(intValue);
            header.mChunkSize=byteArrayToInt(intValue);
            Log.d(TAG, "Read file chunkSize:" + header.mChunkSize);

            header.mFormat = "" + (char) dis.readByte() + (char) dis.readByte() + (char) dis.readByte() + (char) dis.readByte();
            Log.d(TAG, "Read file format:" + header.mFormat);

            header.mSubChunk1ID = "" + (char) dis.readByte() + (char) dis.readByte() + (char) dis.readByte() + (char) dis.readByte();
            Log.d(TAG, "Read fmt chunkID:" + header.mSubChunk1ID);

            dis.read(intValue);
            header.mSubChunk1Size = byteArrayToInt(intValue);
            Log.d(TAG, "Read fmt chunkSize:" + header.mSubChunk1Size);

            dis.read(shortValue);
            header.mAudioFormat = byteArrayToShort(shortValue);
            Log.d(TAG, "Read audioFormat:" + header.mAudioFormat);

            dis.read(shortValue);
            header.mNumChannel = byteArrayToShort(shortValue);
            Log.d(TAG, "Read channel number:" + header.mNumChannel);

            dis.read(intValue);
            header.mSampleRate = byteArrayToInt(intValue);
            Log.d(TAG, "Read samplerate:" + header.mSampleRate);

            dis.read(intValue);
            header.mByteRate = byteArrayToInt(intValue);
            Log.d(TAG, "Read byterate:" + header.mByteRate);

            dis.read(shortValue);
            header.mBlockAlign = byteArrayToShort(shortValue);
            Log.d(TAG, "Read blockalign:" + header.mBlockAlign);

            dis.read(shortValue);
            header.mBitsPerSample = byteArrayToShort(shortValue);
            Log.d(TAG, "Read bitspersample:" + header.mBitsPerSample);

            header.mSubChunk2ID = "" + (char) dis.readByte() + (char) dis.readByte() + (char) dis.readByte() + (char) dis.readByte();
            Log.d(TAG, "Read data chunkID:" + header.mSubChunk2ID);

            dis.read(intValue);
            header.mSubChunk2Size = byteArrayToInt(intValue);
            Log.d(TAG, "Read data chunkSize:" + header.mSubChunk2Size);

            Log.d(TAG, "Read wav file success !");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        mWavFileHeader=header;
        return true;
    }


    private int byteArrayToInt(byte[] b){
        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    private short byteArrayToShort(byte[] b){
        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }
}
