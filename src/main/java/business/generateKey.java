package business;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

public class generateKey {
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public SecretKey getKey(){
        return key;
    }
}
