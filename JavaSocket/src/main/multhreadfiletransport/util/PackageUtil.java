package multhreadfiletransport.util;

import multhreadfiletransport.model.FileInfo;
import multhreadfiletransport.model.RecieverSectionInfo;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by dela on 1/25/18.
 */

public class PackageUtil {
    // 1. 将每一个打包好的字符串的长度计算出来, 并将这个长度设置成包头, 加在字符串前面
    public static byte[] addHeader(String info) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("file-config");
        int size = Integer.parseInt(resourceBundle.getString("headerSize"));

        byte[] result = info.getBytes();
        long headerLen = result.length;

        long temp = 10;
        for (int i = 1; i < size; i++) {
            temp *= 10;
        }

        String header = String.valueOf(headerLen + temp).substring(1);
        byte[] yes = (header + info).getBytes();

        return yes;
    }


    // 2. 将List<FileInfo>转换成一个字符串
    // 每一个FileInfo内部使用:分割, 而每个FileInfo用\t分割

    // 打包一个FileInfo
    public static String packageFileInfo(FileInfo fileInfo) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(fileInfo.getFileName());
        stringBuffer.append(":");
        stringBuffer.append(fileInfo.getFileLen());

        return stringBuffer.toString();
    }

    // 打包一个List<FileInfo>
    public static String packageFileInfoList(List<FileInfo> fileInfoList) {
        StringBuffer stringBuffer = new StringBuffer();

        for (FileInfo fileInfo : fileInfoList) {
            stringBuffer.append(packageFileInfo(fileInfo));
            stringBuffer.append("\t");
        }

        return stringBuffer.toString();
    }

    // 3. 打包一个section对象
    public static String packageSectionInfo(RecieverSectionInfo recieverSectionInfo) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(recieverSectionInfo.getTargetFileName());
        stringBuffer.append(":");
        stringBuffer.append(recieverSectionInfo.getTempFileName());
        stringBuffer.append(":");
        stringBuffer.append(recieverSectionInfo.getOffset());
        stringBuffer.append(":");
        stringBuffer.append(recieverSectionInfo.getSectionLen());

        return stringBuffer.toString();
    }

    public static String packageSectionInfoList(List<RecieverSectionInfo> sectionInfoList) {
        StringBuffer stringBuffer = new StringBuffer();

        for (RecieverSectionInfo sectionInfo : sectionInfoList) {
            stringBuffer.append(packageSectionInfo(sectionInfo));
            stringBuffer.append("\t");
        }

        return stringBuffer.toString();
    }
}
