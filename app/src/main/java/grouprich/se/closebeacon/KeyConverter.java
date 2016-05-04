package grouprich.se.closebeacon;

import android.util.Base64;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public final class KeyConverter {

    public static PublicKey stringToPublicKey(String keyInString) throws GeneralSecurityException {
        byte key[] = Base64.decode(keyInString, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
