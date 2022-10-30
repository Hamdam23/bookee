package hamdam.bookee.tools.token;

import hamdam.bookee.tools.exceptions.jwt_token.MissingTokenException;

public class TokenChecker {

    public static void checkHeader(String header, boolean isAccessToken) {
        if (header == null || !header.startsWith("Bearer ")) {
            if (isAccessToken) throw new MissingTokenException("Access");
            throw new MissingTokenException("Refresh");
        }
    }
}
