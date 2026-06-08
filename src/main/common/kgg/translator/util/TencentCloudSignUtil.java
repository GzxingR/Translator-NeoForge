package kgg.translator.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public final class TencentCloudSignUtil {
    private static final String ALGORITHM = "TC3-HMAC-SHA256";
    private static final Set<String> RESTRICTED_REQUEST_HEADERS = Set.of("host", "content-length", "connection", "expect", "upgrade");

    private TencentCloudSignUtil() {}

    public static Map<String, String> buildAuthHeaders(
            String secretId,
            String secretKey,
            String service,
            String host,
            String region,
            String action,
            String version,
            String payload
    ) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String date = utcDate(timestamp);
        String actionLower = action.toLowerCase(Locale.ROOT);

        String canonicalHeaders = "content-type:application/json; charset=utf-8\n"
                + "host:" + host + "\n"
                + "x-tc-action:" + actionLower + "\n";
        String signedHeaders = "content-type;host;x-tc-action";
        String hashedPayload = sha256Hex(payload);
        String canonicalRequest = "POST\n/\n\n" + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedPayload;

        String credentialScope = date + "/" + service + "/tc3_request";
        String stringToSign = ALGORITHM + "\n" + timestamp + "\n" + credentialScope + "\n" + sha256Hex(canonicalRequest);

        byte[] secretDate = hmac256(("TC3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = hmac256(secretDate, service);
        byte[] secretSigning = hmac256(secretService, "tc3_request");
        String signature = toHex(hmac256(secretSigning, stringToSign));

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", ALGORITHM + " Credential=" + secretId + "/" + credentialScope
                + ", SignedHeaders=" + signedHeaders + ", Signature=" + signature);
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Host", host);
        headers.put("X-TC-Action", action);
        headers.put("X-TC-Version", version);
        headers.put("X-TC-Timestamp", String.valueOf(timestamp));
        headers.put("X-TC-Region", region);
        return headers;
    }

    public static void applyRequestHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        headers.forEach((name, value) -> {
            if (!RESTRICTED_REQUEST_HEADERS.contains(name.toLowerCase(Locale.ROOT))) {
                builder.header(name, value);
            }
        });
    }

    private static String utcDate(long timestampSeconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(timestampSeconds * 1000));
    }

    private static String sha256Hex(String value) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return toHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
    }

    private static byte[] hmac256(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, mac.getAlgorithm()));
        return mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}