开发说明文档：

    1.秘钥文件film.jks,在base目录中
      秘钥配置
      storePassword：12345679
      keyAlias：filma
      keyPassword：123456798

    2.服务地址配置
      HttpFlag文件, 在业务包biz.http中, 文件中各配置都有说明

    3.打包配置
      在base目录下的build.gradle中, 项目中build.gradle文件有多个, 需要注意, 不要弄错目录
      在该文件中查找 productFlavors 即可定位到要配置的代码, 该位置有说明

    4.升级包MD5
      用命令行运行 certutil -hashfile “文件路径”\“文件名”.apk MD5 即可得到
      注：命令行显示的md5是有空格的，去除空格再填入升级配置

    5.应用图标
      mipmap中ic_launcher.png, 各种dpi的都需要

    6.闪屏背景
      drawable-xhdpi中base_bg_splash.png
      根据图片来选择是否要用.9图
