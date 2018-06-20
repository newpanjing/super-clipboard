package com.qikenet.clipboard.client;

import com.qikenet.clipboard.entity.Transfer;
import com.qikenet.clipboard.utils.ClipboardUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;

public class Client extends Thread {

    private static int SERVER_PORT = 5000;
    private static String HOST = "127.0.0.1";

    private String key = "";
    private boolean change = false;

    private SocketPool pool;

    private void initPool() {
        pool = new SocketPool(HOST,SERVER_PORT);
    }

    /**
     * 读取剪切板的文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    private byte[] readFile(File file) throws IOException {

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        fis.close();

        return out.toByteArray();
    }

    /**
     * 获取剪切板数据
     *
     * @return
     * @throws Exception
     */
    private synchronized void clipboardData() throws Exception {

        Object data = ClipboardUtil.getData();
        if (data != null) {
            byte[] value = null;
            int type = -1;
            String fileName = null;

            if (data instanceof String) {
                String v = String.valueOf(data);
                value = v.getBytes(Charset.forName("Utf8"));

                if (!key.equals(v)) {
                    key = v;
                    change = true;
                } else {
                    change = false;
                }
                type = Transfer.TYPE_STRING;

            } else if (data instanceof BufferedImage) {
                BufferedImage bi = (BufferedImage) data;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(bi, "png", out);
                value = out.toByteArray();

                String v = bi.getHeight() + "" + bi.getWidth();

                if (!key.equals(v)) {
                    key = v;
                    change = true;
                } else {
                    change = false;
                }
                type = Transfer.TYPE_IMAGE;

            } else if (data instanceof File) {
                File file = (File) data;
                if (file.length() > 10485760||file.isDirectory()) {
                    //大于10M 丢弃
                   //System.out.println("剪切板数据大于10M,10485760字节，将丢弃。");
                    return;
                }
                String v = file.getPath();

                if (!key.equals(v)) {
                    key = v;
                    change = true;
                    value = readFile(file);
                    fileName = file.getName();
                } else {
                    change = false;
                }
                type = Transfer.TYPE_FILE;

            }

            if (change) {
                System.out.println("数据发生改变，key=" + key);
                Transfer transfer = new Transfer();
                transfer.setData(value);
                transfer.setType(type);
                transfer.setFileName(fileName);

                //调用socket 发到服务器
                pool.push(transfer);
            }
        }


    }

    @Override
    public void run() {

        //启动socket
        initPool();

        //监听剪切板，上传中转服务
        while (true) {
            try {
                clipboardData();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) throws Exception {

        new Client().start();
    }
}
