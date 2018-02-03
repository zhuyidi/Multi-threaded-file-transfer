package multhreadfiletransport.util;

import multhreadfiletransport.model.FileInfo;
import multhreadfiletransport.model.RecieverSectionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Created by dela on 1/25/18.
 */

// 将String还原成一个对象
public class ParseUtil {
    // 1. 给一个size字节的字节数组, 将其解析成一个数字
    public static long getByteStrLen(byte[] len) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        int size = Integer.parseInt(resourceBundle.getString("headerSize"));

        return Long.parseLong(new String(len));
    }

    // 2. 解析一个FileInfo对象
    // 将一个文件字符串转换成文件信息
    public static FileInfo parseStringToFileInfo(String fileString) {
        FileInfo fileInfo = new FileInfo();

        String[] infoTemps = fileString.split(":");
        fileInfo.setFileName(infoTemps[0]);
        fileInfo.setFileLen(Integer.parseInt(infoTemps[1]));

        return fileInfo;
    }


    // 将一个文件列表字符串转换成文件信息List
    public static List<FileInfo> parseStringToFileInfoList(String fileString) {
        List<FileInfo> fileInfoList = new ArrayList<>();

        String[] fileTemps = fileString.split("\t");
        for (String temp : fileTemps) {
            FileInfo fileInfo = parseStringToFileInfo(temp);
            fileInfoList.add(fileInfo);
        }

        return fileInfoList;
    }

    // 3. 解析section对象
    // 解析一个section对象
    public static RecieverSectionInfo parseStringToSectionInfo(String sectionString) {
        String[] temps = sectionString.split(":");
        return new RecieverSectionInfo(temps[0], temps[1], Long.parseLong(temps[2]), Long.parseLong(temps[3]));
    }

    // 解析List<section对象>
    public static List<RecieverSectionInfo> parseStringToSectionInfoList(String sectionString) {
        List<RecieverSectionInfo> sectionInfoList = new ArrayList<>();

        String[] temps = sectionString.split("\t");
        for (String temp : temps) {
            sectionInfoList.add(parseStringToSectionInfo(temp));
        }

        return sectionInfoList;
    }
}
