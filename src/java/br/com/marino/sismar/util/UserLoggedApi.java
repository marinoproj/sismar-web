package br.com.marino.sismar.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class UserLoggedApi {
    
    private int codUser;
    private int codClient;
    private String token;

    public UserLoggedApi(String token) {
    
        Algorithm algorithm = Algorithm.HMAC256(Util.SECRET_API);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        
        codUser = jwt.getClaim("codUser").asInt();
        codClient = jwt.getClaim("codClient").asInt();
        this.token = token;
        
    }    

    public int getCodUser() {
        return codUser;
    }

    public void setCodUser(int codUser) {
        this.codUser = codUser;
    }

    public int getCodClient() {
        return codClient;
    }

    public void setCodClient(int codClient) {
        this.codClient = codClient;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }    
    
}
