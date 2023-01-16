package business;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class GenerateKey {
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public SecretKey getKey(){
        return key;
    }
}
