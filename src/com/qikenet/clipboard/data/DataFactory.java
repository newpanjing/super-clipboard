package com.qikenet.clipboard.data;

import com.qikenet.clipboard.entity.Transfer;
import java.io.*;
import java.nio.charset.Charset;

public class DataFactory {

    /**
     * 编码
     * @return
     */
    public static void encoding(OutputStream out, Transfer transfer) throws Exception {

        /**
         *
         * 0 字节类型 1字节
         * 1 文件名长度 1字节
         * 2-n 文件名
         * n数据长度 4字节
         * n-x数据
         *
         */

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(baout);

        //类型 1字节
        dout.write((byte) transfer.getType());
        String fileName = transfer.getFileName();

        //文件名
        if (fileName != null) {
            byte[] data = fileName.getBytes(Charset.forName("Utf8"));

            //如果文件名大于255字节，重命名+后缀
            dout.write(data.length);
            dout.write(data);
        }else {
            dout.write(0);
        }

        //数据长度
        int length = transfer.getData().length;
        dout.writeInt(length);

        //数据
        dout.write(transfer.getData());
        dout.flush();

        //数据块长度

        DataOutputStream os = new DataOutputStream(out);
        os.writeInt(baout.toByteArray().length);
        os.write(baout.toByteArray());
        os.flush();
        out.flush();


    }

    public static byte[] readBytes(InputStream in, int len) throws IOException{

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (len > 0) {
            byte[] buffer = new byte[len];
            int tmp= in.read(buffer);
            out.write(buffer,0,tmp);
            len -= tmp;
        }

        return out.toByteArray();
    }

    public static Transfer decoding(InputStream in) throws Exception {

        Transfer transfer = new Transfer();

        /**
         *
         * 0 字节类型 1字节
         * 1 文件名长度 1字节
         * 2-n 文件名
         * n数据长度 4字节
         * n-x数据
         *
         */
        DataInputStream dataInputStream = new DataInputStream(in);

        int length= dataInputStream.readInt();
        byte[] datas= readBytes(in,length);
        ByteArrayInputStream bis = new ByteArrayInputStream(datas);
        DataInputStream dis = new DataInputStream(bis);


        byte type = dis.readByte();
        transfer.setType(type);

        switch (type) {
            case -1:
                break;

            case Transfer.TYPE_IMAGE:
                //图片随机文件名
                transfer.setFileName(System.currentTimeMillis()+".png");
            case Transfer.TYPE_STRING:

                //文件名长度
                dis.skipBytes(1);


                break;

            case Transfer.TYPE_FILE:

                //文件名长度
                byte flen= dis.readByte();
                //文件名数据
                byte[] fd= readBytes(in,flen);
                transfer.setFileName(new String(fd,Charset.forName("Utf8")));

                break;
        }

        //数据长度
        int len = dis.readInt();
        byte[] bytes=readBytes(in, len);
        transfer.setData(bytes);




        return transfer;
    }




}
