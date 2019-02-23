package com.fuyou.play.biz.oss;


/**
 * Created by Administrator on 2018-08-27.
 */
public class OSSUpdateBiz {

    private static String EndPoint = "oss-cn-shenzhen.aliyuncs.com";
    private static String AccessKeyId = "LTAIaPceRz4dhGbW";
    private static String AccessKeySecret = "oO5lRDPv6t5XgR8whXnzfOXI8BOeLa";
    private static String BucketName = "fuyouoss";
    private static String Folder = "image/";

    public void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建OSSClient实例。
                /*OSSClient ossClient = new OSSClient(EndPoint, AccessKeyId, AccessKeySecret);
                File file = new File("/storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1534848152670.jpg");
                if (file.exists()) {
                    try {
                        LogCustom.show("上传开始:"+System.currentTimeMillis());
                        ossClient.putObject(BucketName, Folder + file.getName(), file);
                        LogCustom.show("上传成功:"+System.currentTimeMillis());
                    } catch (Exception e){
                        LogCustom.show("上传失败:"+System.currentTimeMillis());
                    }
                }
                // 关闭OSSClient。
                try {
                    ossClient.shutdown();
                } catch (Exception e){

                }*/
            }
        }).start();
    }

}
