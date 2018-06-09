package com.sf.libplayer.wav;

/**
 * Created by sufan on 2018/4/18.
 * wav文件头信息
 * 参照wav文件格式
 */

public class WavFileHeader {

    public static final int WAV_FILE_HEADER_SIZE = 44;
    public static final int WAV_CHUNKSIZE_EXCLUDE_DATA = 36;

    public static final int WAV_CHUNKSIZE_OFFSET = 4;
    public static final int WAV_SUB_CHUNKSIZE1_OFFSET = 16;
    public static final int WAV_SUB_CHUNKSIZE2_OFFSET = 40;

    public String mChunkID="RIFF";
    public int mChunkSize=0;
    public String mFormat="WAVE";

    public String mSubChunk1ID="fmt ";
    public int mSubChunk1Size = 16;
    public short mAudioFormat = 1;
    public short mNumChannel = 1;
    public int mSampleRate = 8000;
    public int mByteRate = 0;
    public short mBlockAlign = 0;
    public short mBitsPerSample = 8;

    public String mSubChunk2ID = "data";
    public int mSubChunk2Size = 0;


    public WavFileHeader(){

    }

    public WavFileHeader(int sampleRateInHz, int channels, int bitsPerSample){
        mSampleRate = sampleRateInHz;
        mNumChannel = (short) channels;
        mBitsPerSample = (short) bitsPerSample;
        mByteRate = mSampleRate * mNumChannel * mBitsPerSample / 8;
        mBlockAlign = (short) (mNumChannel * mBitsPerSample / 8);
    }

}
