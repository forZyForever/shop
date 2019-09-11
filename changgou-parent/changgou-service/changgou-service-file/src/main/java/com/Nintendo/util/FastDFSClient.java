package com.Nintendo.util;

import com.Nintendo.file.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Package: com.Nintendo.util
 * @Author: ZZM
 * @Date: Created in 2019/8/19 9:59
 * @Address:CN.SZ
 **/
//类中实现FastDFS信息获取以及文件的相关操
public class FastDFSClient {
    private static final Logger log = LoggerFactory.getLogger(FastDFSClient.class);

    //类每次加载时都读取配置文件
    static {
        String path = new ClassPathResource("fdfs_client.conf").getPath();
        try {
            ClientGlobal.init(path);
        } catch (Exception e) {
            log.error("读取配置文件异常", e);
        }
    }

    /**
     * 获取storageClient
     * @return
     */
    public static StorageClient createStorageClient() {
        TrackerClient trackerClient = new TrackerClient();
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            return storageClient;
        } catch (Exception e) {
            log.error("获取storageClient发生异常", e);
        }
        return null;
    }

    /**
     * 获取trackerServer
     * @return
     */
    public static TrackerServer createTrackerServer() {
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            return trackerServer;
        } catch (Exception e) {
            log.error("获取trackerServer发生异常", e);
        }
        return null;
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    public static String[] upload(FastDFSFile file) {
        StorageClient storageClient = createStorageClient();
        NameValuePair[] meta_list = new NameValuePair[]{new NameValuePair(file.getAuthor()), new NameValuePair(file.getName())};
        try {
            //文件字节数组,文件扩展名,文件元数据
            String[] strings = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
            return strings;
            // strings[0]==group1  strings[1]=M00/00/00/wKjThF1aW9CAOUJGAAClQrJOYvs424.jpg
        } catch (Exception e) {
            log.error("上传文件发生异常", e);
        }
        return null;
    }

    /**
     * 文件下载
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downFile(String groupName, String remoteFileName) {
        ByteArrayInputStream is = null;
        try {
            StorageClient storageClient = createStorageClient();
            //文件组名,远程文件名
            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
            is = new ByteArrayInputStream(bytes);
            return is;
        } catch (Exception e) {
            log.error("远程下载文件异常", e);
        } finally {
            try {
                if (null!=is) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("关闭流发生异常", e);
            }
        }
        return null;
    }

    /**
     * 文件删除
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName,String remoteFileName){
        try {
            StorageClient storageClient = createStorageClient();
            int i = storageClient.delete_file(groupName, remoteFileName);
            if (i==0){
            log.info("删除文件成功");
            }else {
                log.info("删除文件失败");
            }
        } catch (Exception e) {
           log.error("删除文件发生异常",e);
        }
    }

    /**
     * 根据组名获取组的信息
     * @param groupName
     * @return
     */
   public StorageServer getStroageServerInfo(String groupName)  {
       try {
           TrackerClient trackerClient = new TrackerClient();
           TrackerServer trackerServer = trackerClient.getConnection();
           StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer, groupName);
           return storeStorage;
       } catch (Exception e) {
           log.error("根据组名获取组信息发生异常",e);
       }
    return null;
   }

    /**
     * 根据文件组名和文件存储路径获取Storage服务的IP、端口数组信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
   public static ServerInfo[] getServerInfo(String groupName, String remoteFileName){
       try {
           TrackerClient trackerClient = new TrackerClient();
           TrackerServer trackerServer = trackerClient.getConnection();
           return trackerClient.getFetchStorages(trackerServer,groupName,remoteFileName);
       } catch (Exception e) {
           log.error("获取服务数组信息发生异常",e);
       }
       return null;
   }

    /**
     * 根据文件名和组名获取组信息的文件信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo createFileInfo(String groupName, String remoteFileName){
        try {
            StorageClient storageClient = createStorageClient();
            FileInfo fileInfo = storageClient.get_file_info(groupName, remoteFileName);
            return fileInfo;
        } catch (Exception e) {
           log.error("获取文件信息发生异常",e);
        }
        return null;
    }

    /**
     * 获取tracker的ip和端口信息
     * @return
     */
    public static  String getTrackerMessage(){
        try {
            TrackerServer trackerServer = createTrackerServer();
            String hostString = trackerServer.getInetSocketAddress().getHostString();
            int port = ClientGlobal.getG_tracker_http_port();
            return "http://" + hostString + ":" + port;
        } catch (Exception e) {
         log.error("获取tracker的ip信息发生异常",e);
        }
        return null;
    }
}
