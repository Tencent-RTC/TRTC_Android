package com.tencent.trtc.debug;


import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.Deflater;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Module:   GenerateTestUserSig
 *
 * Description: generates UserSig for testing. UserSig is a security signature designed by Tencent Cloud for its
 * cloud services.
 * It is calculated based on `SDKAppID`, `UserID`, and `EXPIRETIME` using the HMAC-SHA256 encryption
 * algorithm.
 *
 * Attention: do not use the code below in your commercial app. This is because:
 *
 * The code may be able to calculate UserSig correctly, but it is only for quick testing of the SDK’s
 * basic features, not for commercial apps.
 * `SDKSECRETKEY` in client code can be easily decompiled and reversed, especially on web.
 * Once your key is disclosed, attackers will be able to steal your Tencent Cloud traffic.
 *
 * The correct method is to deploy the `UserSig` calculation code and encryption key on your project
 * server so that your app can request from your server a `UserSig` that is calculated whenever one is
 * needed.
 * Given that it is more difficult to hack a server than a client app, server-end calculation can better
 * protect your key.
 *
 * Reference: https://cloud.tencent.com/document/product/647/17275#Server
 */
public class GenerateTestUserSig {

    /**
     * Domain name for CDN publishing and stream mixing
     */
    public static final String CDN_DOMAIN_NAME = "PLACEHOLDER";

    /**
     * `bizId` for CDN publishing and stream mixing.
     * Replace it with the actual value when using CND,
     * and replace it with 0 when not using CDN, so that the compilation can pass.
     */
    public static final int BIZID = PLACEHOLDER;

    /**
     * `appId` for CDN publishing and stream mixing.
     * Replace it with the actual value when using CND,
     * and replace it with 0 when not using CDN, so that the compilation can pass.
     */
    public static final int APPID = PLACEHOLDER;

    /**
     * Tencent Cloud `SDKAppID`. Set it to the `SDKAppID` of your account.
     *
     * You can view your `SDKAppID` after creating an application in the [TRTC console](https://console.cloud.tencent
     * .com/rav).
     * `SDKAppID` uniquely identifies a Tencent Cloud account.
     */
    public static final int SDKAPPID = PLACEHOLDER;

    /**
     * Signature validity period, which should not be set too short
     *
     * Unit: second
     * Default value: 604800 (7 days)
     */
    private static final int EXPIRETIME = 604800;


    /**
     * Follow the steps below to obtain the key required for UserSig calculation.
     *
     * Step 1. Log in to the [TRTC console](https://console.cloud.tencent.com/rav), and create an application if you
     * don’t have one.
     * Step 2. Find your application, click “Application Info”, and click the “Quick Start” tab.
     * Step 3. Copy and paste the key to the code, as shown below.
     *
     * Note: this method is for testing only. Before commercial launch, please migrate the UserSig calculation code
     * and key to your backend server to prevent key disclosure and traffic stealing.
     * Reference: https://cloud.tencent.com/document/product/647/17275#Server
     */
    public static final String SDKSECRETKEY = "PLACEHOLDER";

    /**
     * Calculating UserSig
     *
     * The asymmetric encryption algorithm HMAC-SHA256 is used in the function to calculate UserSig based on
     * `SDKAppID`, `UserID`, and `EXPIRETIME`.
     *
     * do not use the code below in your commercial app. This is because:
     *
     * The code may be able to calculate UserSig correctly, but it is only for quick testing of the SDK’s basic
     * features, not for commercial apps.
     * `SDKSECRETKEY` in client code can be easily decompiled and reversed, especially on web.
     * Once your key is disclosed, attackers will be able to steal your Tencent Cloud traffic.
     *
     * The correct method is to deploy the `UserSig` calculation code on your project server so that your app can
     * request from your server a `UserSig` that is calculated whenever one is needed.
     * Given that it is more difficult to hack a server than a client app, server-end calculation can better protect
     * your key.
     *
     * Reference: https://cloud.tencent.com/document/product/647/17275#Server
     */
    public static String genTestUserSig(String userId) {
        return genTLSSignature(SDKAPPID, userId, EXPIRETIME, null, SDKSECRETKEY);
    }

    /**
     * Generating a TLS Ticket.
     *
     * @param sdkAppId      appid of your application
     * @param userId        User ID
     * @param expire        Validity period, in seconds
     * @param userBuf       `null` by default
     * @param priKeyContent Private key required for generating a TLS ticket
     * @return If an error occurs, an empty string will be returned or exceptions printed. If the operation succeeds,
     *     a valid ticket will be returned.
     */
    private static String genTLSSignature(long sdkAppId, String userId, long expire, byte[] userBuf,
                                          String priKeyContent) {
        long currTime = System.currentTimeMillis() / 1000;
        JSONObject sigDoc = new JSONObject();
        try {
            sigDoc.put("TLS.ver", "2.0");
            sigDoc.put("TLS.identifier", userId);
            sigDoc.put("TLS.sdkappid", sdkAppId);
            sigDoc.put("TLS.expire", expire);
            sigDoc.put("TLS.time", currTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String base64UserBuf = null;
        if (null != userBuf) {
            base64UserBuf = Base64.encodeToString(userBuf, Base64.NO_WRAP);
            try {
                sigDoc.put("TLS.userbuf", base64UserBuf);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String sig = hmacsha256(sdkAppId, userId, currTime, expire, priKeyContent, base64UserBuf);
        if (sig.length() == 0) {
            return "";
        }
        try {
            sigDoc.put("TLS.sig", sig);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Deflater compressor = new Deflater();
        compressor.setInput(sigDoc.toString().getBytes(Charset.forName("UTF-8")));
        compressor.finish();
        byte[] compressedBytes = new byte[2048];
        int compressedBytesLength = compressor.deflate(compressedBytes);
        compressor.end();
        return new String(base64EncodeUrl(Arrays.copyOfRange(compressedBytes, 0, compressedBytesLength)));
    }

    private static String hmacsha256(long sdkAppId, String userId, long currTime, long expire, String priKeyContent,
                                     String base64UserBuf) {
        String contentToBeSigned =
                "TLS.identifier:" + userId + "\n" + "TLS.sdkappid:" + sdkAppId + "\n" + "TLS.time:" + currTime + "\n"
                        + "TLS.expire:" + expire + "\n";
        if (null != base64UserBuf) {
            contentToBeSigned += "TLS.userbuf:" + base64UserBuf + "\n";
        }
        try {
            byte[] byteKey = priKeyContent.getBytes("UTF-8");
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA256");
            hmac.init(keySpec);
            byte[] byteSig = hmac.doFinal(contentToBeSigned.getBytes("UTF-8"));
            return new String(Base64.encode(byteSig, Base64.NO_WRAP));
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (InvalidKeyException e) {
            return "";
        }
    }

    private static byte[] base64EncodeUrl(byte[] input) {
        byte[] base64 = new String(Base64.encode(input, Base64.NO_WRAP)).getBytes();
        for (int i = 0; i < base64.length; ++i) {
            switch (base64[i]) {
                case '+':
                    base64[i] = '*';
                    break;
                case '/':
                    base64[i] = '-';
                    break;
                case '=':
                    base64[i] = '_';
                    break;
                default:
                    break;
            }
        }
        return base64;
    }

}
