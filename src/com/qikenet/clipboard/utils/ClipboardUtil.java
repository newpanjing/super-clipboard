package com.qikenet.clipboard.utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

public class ClipboardUtil {

    /**
     * 获取剪切板数据
     * @return
     * @throws Exception
     */
    public static Object getData() throws Exception{
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipT = clip.getContents(null);
        if (clipT != null) {
            if(clip.isDataFlavorAvailable(DataFlavor.stringFlavor)){
                return clipT.getTransferData(DataFlavor.stringFlavor);
            }else if(clip.isDataFlavorAvailable(DataFlavor.imageFlavor)){
                return clipT.getTransferData(DataFlavor.imageFlavor);
            }else if(clip.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)){
                List<File> fs=(List<File>) clipT.getTransferData(DataFlavor.javaFileListFlavor);
                if (fs != null && !fs.isEmpty()) {
                    return fs.get(0);
                }
            }
        }

        return null;
    }
}
