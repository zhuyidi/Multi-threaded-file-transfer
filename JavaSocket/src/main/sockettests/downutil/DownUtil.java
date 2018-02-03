package sockettests.downutil;

/**
 * Created by dela on 1/20/18.
 */
// 通过URL对象的openStream()读取该URL资源的InputStream,
// 通过该方法读取远程资源, 实现多线程下载
public class DownUtil {
    private String path; // 指定下载资源的路径
    private String targetFile; // 指定所下载的文件的保存位置
    private int threadNum; // 定义需要使用多少个线程下载
    private DownThread[] threads; // 定义下载的线程对象
    private int fileSize; // 定义下载的文件总大小


}
