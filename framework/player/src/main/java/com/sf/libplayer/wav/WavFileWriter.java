package com.sf.libplayer.wav;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by sufan on 2018/4/18.
 */

public class WavFileWriter {

    private static final String TAG = "WavFileWriter";

    private String mFilePath;
    private int mDataSize = 0;
    private DataOutputStream dos;


    /**
     *
     * @param filePath
     * @param sampleRateInHz  采样率 44100
     * @param channels        声道数  1单声道  2双声道
     * @param bitsPerSample   每个样点对应的位数  16
     * @return
     */
    public boolean openFile(String filePath, int sampleRateInHz, int channels, int bitsPerSample) {
        if (dos != null) {
            closeFile();
        }

        mFilePath = filePath;
        try {
            dos = new DataOutputStream(new FileOutputStream(mFilePath));
            return writeHeader(sampleRateInHz, channels, bitsPerSample);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean closeFile() {
        boolean result=false;
        if (dos != null) {
            try {
                result=writeDataSize();
                dos.close();
                dos=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean writeData(byte[] buffer, int offset, int count) {
        if (dos == null) {
            return false;
        }
        try {
            dos.write(buffer, offset, count);
            mDataSize += count;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 将一些需要计算出来的字段重新赋值
     * mChunkSize  位置4-8，值=36+原始音频数据大小
     * mSubChunk1Size  固定值16
     * mSubChunk2Size  位置40-44  值=原始音频数据大小
     */
    private boolean writeDataSize() {
        if (dos == null) {
            return false;
        }
        try {
            RandomAccessFile waveAccessFile = new RandomAccessFile(mFilePath, "rw");
            waveAccessFile.seek(WavFileHeader.WAV_CHUNKSIZE_OFFSET);
            waveAccessFile.write(intToByteArray(WavFileHeader.WAV_CHUNKSIZE_EXCLUDE_DATA + mDataSize), 0, 4);
            waveAccessFile.seek(WavFileHeader.WAV_SUB_CHUNKSIZE2_OFFSET);
            waveAccessFile.write(intToByteArray(mDataSize), 0, 4);
            waveAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean writeHeader(int sampleRateInHz, int channels, int bitsPerSample) {
        if (dos == null) {
            return false;
        }

        WavFileHeader header = new WavFileHeader(sampleRateInHz, channels, bitsPerSample);

        //按照wav文件结构依次写入
        try {
            dos.writeBytes(header.mChunkID);
            //这里不直接用writeInt的原因是它采用的大端法存储
            dos.write(intToByteArray(header.mChunkSize), 0, 4);
            dos.writeBytes(header.mFormat);
            dos.writeBytes(header.mSubChunk1ID);
            dos.write(intToByteArray(header.mSubChunk1Size), 0, 4);
            dos.write(shortToByteArray(header.mAudioFormat), 0, 2);
            dos.write(shortToByteArray(header.mNumChannel), 0, 2);
            dos.write(intToByteArray(header.mSampleRate), 0, 4);
            dos.write(intToByteArray(header.mByteRate), 0, 4);
            dos.write(shortToByteArray(header.mBlockAlign), 0, 2);
            dos.write(shortToByteArray(header.mBitsPerSample), 0, 2);
            dos.writeBytes(header.mSubChunk2ID);
            dos.write(intToByteArray(header.mSubChunk2Size), 0, 4);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private static byte[] intToByteArray(int data) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(data).array();
    }

    private static byte[] shortToByteArray(short data) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(data).array();
    }
}
