package com.qikenet.clipboard.entity;

import java.util.Arrays;

/**
 * 网络层传递对象
 */
public class Transfer {

    /**
     * 字符串
     */
    public static final int TYPE_STRING = 0;

    /**
     * 图片
     */
    public static final int TYPE_IMAGE = 1;

    /**
     * 文件，10M以下
     */
    public static final int TYPE_FILE = 2;

    private int type;

    private String fileName;

    private byte[] data;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "type=" + type +
                ", fileName='" + fileName + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
