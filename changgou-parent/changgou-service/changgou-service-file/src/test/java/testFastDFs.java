import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Package: PACKAGE_NAME
 * @Author: ZZM
 * @Date: Created in 2019/8/19 20:35
 * @Address:CN.SZ
 **/
public class testFastDFs {
    /**
     * 文件上传
     *
     * @throws Exception
     */
    @Test
    public void upload() throws Exception {
        //加载配置
        ClientGlobal.init("E:\\JavaProject\\Shop\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建trackerClient
        TrackerClient trackerClient = new TrackerClient();
        //创建trackerServer
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建storageServer
        StorageServer storageServer = null;
        //创建storageClient
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //arg0:本地文件路径,arg1:文件扩展名,arg3:文件元数据,(文件大小,尺寸,作者,像素..)
        String[] jpgs = storageClient.upload_file("E:\\aoge\\life.jpg", "jpg", null);
        for (String jpg : jpgs) {
            System.out.println(jpg);
        }
    }

    /**
     * 文件下载
     *
     * @throws Exception
     */
    @Test
    public void download() throws Exception {
        //加载配置
        ClientGlobal.init("E:\\JavaProject\\Shop\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //指定组名和远程文件名
        byte[] group1s = storageClient.download_file("group1", "M00/00/00/wKjThF1anKWAWIpPAAE9RBulNV0301.jpg");
        FileOutputStream is = new FileOutputStream(new File("E:\\aoge\\laojie.jpg"));
        is.write(group1s);
        is.close();
    }

    /**
     * 图片删除
     *
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        //加载配置
        ClientGlobal.init("E:\\JavaProject\\Shop\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        int i = storageClient.delete_file("group1", "M00/00/00/wKjThF1anKWAWIpPAAE9RBulNV0301.jpg");
        if (i == 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
    }

    /**
     * 根据组名称 获取该组对应的组服务信息
     *
     * @throws Exception
     */
    @Test
    public void getStroageServerInfo() throws Exception {
        ClientGlobal.init("E:\\JavaProject\\Shop\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer, "group1");
        System.out.println(storageServer.getInetSocketAddress().getHostString() + ":" + storageServer.getInetSocketAddress().getPort());
    }

    /**
     * 根据文件名和组名获取文件的信息
     *
     * @throws Exception
     */
    @Test
    public void getFileinfo() throws Exception {
        ClientGlobal.init("E:\\JavaProject\\Shop\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        FileInfo fileInfo = storageClient.get_file_info("group1", "M00/00/00/wKjThF1aqSKAaEzIAAE9RBulNV0798.jpg");
        System.out.println(fileInfo.getSourceIpAddr() + ":" + fileInfo.getFileSize());
    }

    /**
     * 根据文件名和组名和tracker 获取该文件所在的storage的服务器的数组信息
     *
     * @throws Exception
     */
    @Test
    public void getserverinfo() throws Exception {
        ClientGlobal.init("E:\\JavaProject\\Shop\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        ServerInfo[] group1s = trackerClient.getFetchStorages(trackerServer, "group1", "M00/00/00/wKjThF1aqSKAaEzIAAE9RBulNV0798.jpg");
        for (ServerInfo group1 : group1s) {
            System.out.println(group1.getIpAddr() + ":" + group1.getPort());
        }
    }

    //获取tracker的ip和端口
    @Test
    public void getTrackerInfo() throws Exception {
        ClientGlobal.init("E:\\JavaProject\\Shop\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        //tracker的ip信息
        String hostString = trackerServer.getInetSocketAddress().getHostString();
        int port = ClientGlobal.getG_tracker_http_port();
        System.out.println("http://" + hostString + ":" + port);

    }

}
