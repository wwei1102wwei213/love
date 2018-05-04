package com.slove.play.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/***
 * *
 * 解密 加密
 * *
 */
public class DecryptUtils {

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /***
     * MD5加密
     * @param origin
     * @param charsetname
     * @return
     */
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    /**
     * 解密base64
     * @param content
     * @return
     */
    public static String decodeBase64(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        String temp = "";
        try {
            temp = new String(Base64.decode(content.getBytes(), Base64.DEFAULT));
        } catch (Exception e) {
            return "";
        }
        return temp.trim();
    }

    /**
     * 加密base64
     * @param content
     * @return
     */
    public static String encode(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        return new String(Base64.encode(content.getBytes(), Base64.NO_WRAP)).trim();
    }


    /***
     * 先URLDecoder后解Base64
     * @param content
     * @return
     */
    public static String decodeURLAndBase64(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        String temp = "";
        try {
            temp = URLDecoder.decode(content);
            temp = new String(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        } catch (Exception e) {
            return "";
        }
        return temp.trim();
    }

    /**
     * 先Base64后URLEncoder
     * @param content
     * @return
     */
    public static String encodeBase64AndURL(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        String str = new String(Base64.encode(content.getBytes(), Base64.NO_WRAP)).trim();
        return URLEncoder.encode(str);
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static class AES {
        static final String algorithmStr = "AES/ECB/PKCS5Padding";

        private static final Object TAG = "AES";

        static private KeyGenerator keyGen;

        static private Cipher cipher;

        static boolean isInited = false;

        private static  void init() {
            try {
                /**为指定算法生成一个 KeyGenerator 对象。
                 *此类提供（对称）密钥生成器的功能。
                 *密钥生成器是使用此类的某个 getInstance 类方法构造的。
                 *KeyGenerator 对象可重复使用，也就是说，在生成密钥后，
                 *可以重复使用同一 KeyGenerator 对象来生成进一步的密钥。
                 *生成密钥的方式有两种：与算法无关的方式，以及特定于算法的方式。
                 *两者之间的惟一不同是对象的初始化：
                 *与算法无关的初始化
                 *所有密钥生成器都具有密钥长度 和随机源 的概念。
                 *此 KeyGenerator 类中有一个 init 方法，它可采用这两个通用概念的参数。
                 *还有一个只带 keysize 参数的 init 方法，
                 *它使用具有最高优先级的提供程序的 SecureRandom 实现作为随机源
                 *（如果安装的提供程序都不提供 SecureRandom 实现，则使用系统提供的随机源）。
                 *此 KeyGenerator 类还提供一个只带随机源参数的 inti 方法。
                 *因为调用上述与算法无关的 init 方法时未指定其他参数，
                 *所以由提供程序决定如何处理将与每个密钥相关的特定于算法的参数（如果有）。
                 *特定于算法的初始化
                 *在已经存在特定于算法的参数集的情况下，
                 *有两个具有 AlgorithmParameterSpec 参数的 init 方法。
                 *其中一个方法还有一个 SecureRandom 参数，
                 *而另一个方法将已安装的高优先级提供程序的 SecureRandom 实现用作随机源
                 *（或者作为系统提供的随机源，如果安装的提供程序都不提供 SecureRandom 实现）。
                 *如果客户端没有显式地初始化 KeyGenerator（通过调用 init 方法），
                 *每个提供程序必须提供（和记录）默认初始化。
                 */
                keyGen = KeyGenerator.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            // 初始化此密钥生成器，使其具有确定的密钥长度。
            keyGen.init(128); //128位的AES加密
            try {
                // 生成一个实现指定转换的 Cipher 对象。
                cipher = Cipher.getInstance(algorithmStr);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }
            //标识已经初始化过了的字段
            isInited = true;
        }

        private static byte[] genKey() {
            if (!isInited) {
                init();
            }
            //首先 生成一个密钥(SecretKey),
            //然后,通过这个秘钥,返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
            return keyGen.generateKey().getEncoded();
        }

        private static byte[] encrypt(byte[] content, byte[] keyBytes) {
            byte[] encryptedText = null;
            if (!isInited) {
                init();
            }
            /**
             *类 SecretKeySpec
             *可以使用此类来根据一个字节数组构造一个 SecretKey，
             *而无须通过一个（基于 provider 的）SecretKeyFactory。
             *此类仅对能表示为一个字节数组并且没有任何与之相关联的钥参数的原始密钥有用
             *构造方法根据给定的字节数组构造一个密钥。
             *此构造方法不检查给定的字节数组是否指定了一个算法的密钥。
             */
            Key key = new SecretKeySpec(keyBytes, "AES");
            try {
                // 用密钥初始化此 cipher。
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            try {
                //按单部分操作加密或解密数据，或者结束一个多部分操作。(不知道神马意思)
                encryptedText = cipher.doFinal(content);
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return encryptedText;
        }

        private static byte[] encrypt(String content, String password) {
            try {
                byte[] keyStr = getKey(password);
                SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
                Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
                byte[] byteContent = content.getBytes("utf-8");
                cipher.init(Cipher.ENCRYPT_MODE, key);//   ʼ
                byte[] result = cipher.doFinal(byteContent);
                return result; //
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static byte[] decrypt(byte[] content, String password) {
            try {
                byte[] keyStr = getKey(password);
                SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
                Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
                cipher.init(Cipher.DECRYPT_MODE, key);//   ʼ
                byte[] result = cipher.doFinal(content);
                return result; //
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static byte[] getKey(String password) {
            byte[] rByte = null;
            if (password!=null) {
                rByte = password.getBytes();
            }else{
                rByte = new byte[24];
            }
            return rByte;
        }

        /**
         * 将二进制转换成16进制
         * @param buf
         * @return
         */
        public static String parseByte2HexStr(byte buf[]) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < buf.length; i++) {
//                Log.i("Aes","buf[i]=>>>>>>>>"+buf[i]);
                String hex = Integer.toHexString(buf[i] & 0xFF);
//                Log.i("Aes","hex=>>>>>>>>>>>"+hex.toUpperCase());
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        }

        /**
         * 将16进制转换为二进制
         * @param hexStr
         * @return
         */
        public static byte[] parseHexStr2Byte(String hexStr) {
            if (hexStr.length() < 1)
                return null;
            byte[] result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                        16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }

        //注意: 这里的password(秘钥必须是16位的)
        private static final String keyBytes = "3939393838380000";
//                "a2cdefgabcdefg15";

        /**
         *加密
         */
        public static String encode(String content){
            //加密之后的字节数组,转成16进制的字符串形式输出
            return parseByte2HexStr(encrypt(content, keyBytes));
        }

        /**
         *解密
         */
        public static String decode(String content){
            //解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
            byte[] b = decrypt(parseHexStr2Byte(content), keyBytes);
            return new String(b);
        }
    }

    public static String ToSpacePrint(String strg){
        String str = "";
        for(int i = 0;i<strg.length();i++){
            int temp = 0;
            if(i<2){
                str = str + strg.charAt(i)+"";
            }else{
                temp = i;
                while (temp>1){
                    temp = temp%2;
                }
                if(temp==0){
                    str = str +" ";
                }
                str = str + strg.charAt(i);
            }
        }
        return str;
    }

    public static String strToAsc(String str){
        char[] str_char = str.toCharArray();
        String nstr = "";
        for(int i=0;i<str_char.length;i++){
            nstr += (int)str_char[i]+" ";
        }
        return nstr;
    }

    public static String ascToStr(String str){
        String[] str_char = str.split(" ");
        String nstr = "";
        for (int i = 0; i < str_char.length; i++) {
            nstr = (char) Integer.parseInt(str_char[i])+"";
        }
        return nstr;
    }

    public static byte[] getAsciiByte(String str){
        byte[] temp = new byte[str.length()];

        for(int i = 0;i <str.length();i++){
//           temp[i] = (byte)Integer.parseInt(Integer.toHexString(Integer.parseInt(str.charAt(i)+""))+"");
        }
        return temp;
    }

    public static class Tea {
        public static int fn = 0;//在解密服务端返回的数据时，fn需要置0
        private final static int[] KEY = new int[]{//加密解密所用的KEY
                0x1223311, 0x889922, 0x33, 0x44
        };

        public static void setKey(int[] key){
            KEY[0] = key[0];
            KEY[1] = key[1];
            KEY[2] = key[2];
            KEY[3] = key[3];
        }

        public static int[] getKey(){
            return KEY;
        }

        private static long BYTE_1 = 0xFFL;
        private static long BYTE_2 = 0xFF00L;
        private static long BYTE_3 = 0xFF0000L;
        private static long BYTE_4 = 0xFF000000L;

        public static Long UIFILTER = Long.decode("0xffffffff");
        //加密
        private static byte[] encrypt(byte[] content, int offset, int[] key, int times){//times为加密轮数
            long[] tempInt = byteToInt(content, offset);
            long y = Long.decode(tempInt[0]+""), z = Long.decode(tempInt[1]+""),sum = 0,  i;
            long delta=Long.decode("0x9e3779b9"); //这是算法标准给的值
            int a = key[0], b = key[1], c = key[2], d = key[3];
            for (i = 0; i < 16; i++) {
                sum += delta;
                sum &= UIFILTER;
                y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
                y &= UIFILTER;
                z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
                z &= UIFILTER;
            }
            return LongToByte(new long[]{y,z}, 0);
        }

        //解密
        private static byte[] decrypt(byte[] encryptContent, int offset, int[] key, int times){

            long[] tempInt = byteToInt(encryptContent, offset);
            long y = Long.decode(tempInt[0]+""), z = Long.decode(tempInt[1]+""),sum = Long.decode("0xE3779B90"),  i;
            long delta=Long.decode("0x9e3779b9"); //这是算法标准给的值
            int a = key[0], b = key[1], c = key[2], d = key[3];
            for(i = 0; i < times; i++) {
                z += (~(((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d)) + 1);
                z &= UIFILTER;
                y += (~(((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b)) + 1);
                y &= UIFILTER;
                sum += ((~delta) + 1);
                sum &= UIFILTER;
            }
            return LongToByte(new long[]{y,z}, 0);
        }
        //byte[]型数据转成int[]型数据
        private static long[] byteToInt(byte[] content, int offset){
            if(content==null||content.length==0){
                return new long[]{};
            }
            long[] result = new long[content.length >> 2];
            for(int i = 0, j = 0; j < content.length; i++, j += 4){
                result[i] = (content[j]) & BYTE_1| (content[j + 1]) << 8 & BYTE_2|
                        (content[j + 2]) << 16 &BYTE_3 | (content[j + 3]) << 24 & BYTE_4;
            }
            return result;
        }

        private static byte[] LongToByte(long[] content, int offset){
            if(content==null||content.length==0){
                return new byte[]{};
            }
            byte[] result = new byte[content.length << 2];//乘以2的n次方 == 左移n位 即 content.length * 4 == content.length << 2
            for(int i = 0, j = offset; j < result.length; i++, j += 4){
                result[j + 3] = (byte) ((content[i] >> 24) & UIFILTER);
                result[j + 2] = (byte) ((content[i] >> 16) & UIFILTER);
                result[j + 1] = (byte) ((content[i] >> 8) & UIFILTER);
                result[j] = (byte) ((content[i]) & UIFILTER);
            }
            return result;
        }

        //通过TEA算法加密信息
        public static String encryptByTea(String info){
            if(info==null||info.length()==0){
                return "";
            }
            byte[] temp = info.getBytes();
            int n = 8 - temp.length % 8;//若temp的位数不足8的倍数,需要填充的位数

            byte[] encryptStr = new byte[temp.length + n];
            System.arraycopy(temp, 0, encryptStr, 0, temp.length);
            ByteBuffer eBuffer = ByteBuffer.allocate(encryptStr.length);
            byte[] temparray = new byte[8];
            for(int offset = 0; offset < encryptStr.length; offset += 8){
                System.arraycopy(encryptStr, offset, temparray,0 , 8);
                byte[] tempEncrpt = encrypt(temparray, 0, KEY, 16);
                eBuffer.put(tempEncrpt);
            }
            byte[] result = eBuffer.array();
            eBuffer.clear();
            return toHexAscii(result);
        }

        //通过TEA算法解密信息
        public static  String decryptByTea(String dec){
            if(dec==null||dec.length()==0){
                return "";
            }
            byte[] secretInfo = change(dec);
            byte[] decryptStr = null;
            ByteBuffer tempBuffer = ByteBuffer.allocate(secretInfo.length);
            byte[] temparray = new byte[8];
            for(int offset = 0; offset < secretInfo.length; offset += 8){
                System.arraycopy(secretInfo, offset, temparray, 0, 8);
                decryptStr = decrypt(temparray, 0, KEY, 16);
                tempBuffer.put(decryptStr);
            }
            byte[] tempDecrypt = tempBuffer.array();
            tempBuffer.clear();
            String s = new String(tempDecrypt, 0, tempDecrypt.length);
            return s.trim();
        }

        /*16进制字符串转byte[]*/
        public static byte[] change(String inputStr) {
            byte[] result = new byte[inputStr.length() / 2];
            for (int i = 0; i < inputStr.length() / 2; ++i)
                result[i] = (byte)(Integer.parseInt(inputStr.substring(i * 2, i * 2 +2), 16) & 0xff);
            return result;
        }

        /*byte[]转16进制字符串*/
        public static String toHexAscii(byte[] data){
            String[] temp = new String[data.length];
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<data.length;i++){
                int t = (int)data[i];
                temp[i] = Integer.toHexString(t & 0xFF).toUpperCase();
                if(temp[i].length()==1) temp[i] = "0" + temp[i];
                sb.append(temp[i]);
            }
            return sb.toString();
        }
    }
}
