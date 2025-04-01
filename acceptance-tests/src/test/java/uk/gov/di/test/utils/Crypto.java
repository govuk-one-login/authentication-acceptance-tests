package uk.gov.di.test.utils;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.jose4j.base64url.Base64Url;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Crypto {
    private static String encodeArgon2Hash(byte[] hash, Argon2Parameters parameters)
            throws IllegalArgumentException {
        StringBuilder stringBuilder = new StringBuilder();
        Base64.Encoder b64encoder = Base64.getEncoder().withoutPadding();
        String var10000;
        switch (parameters.getType()) {
            case 0 -> var10000 = "$argon2d";
            case 1 -> var10000 = "$argon2i";
            case 2 -> var10000 = "$argon2id";
            default -> throw new IllegalArgumentException(
                    "Invalid algorithm type: " + parameters.getType());
        }

        String type = var10000;
        stringBuilder.append(type);
        stringBuilder
                .append("$v=")
                .append(parameters.getVersion())
                .append("$m=")
                .append(parameters.getMemory())
                .append(",t=")
                .append(parameters.getIterations())
                .append(",p=")
                .append(parameters.getLanes());
        if (parameters.getSalt() != null) {
            stringBuilder.append("$").append(b64encoder.encodeToString(parameters.getSalt()));
        }

        stringBuilder.append("$").append(b64encoder.encodeToString(hash));
        return stringBuilder.toString();
    }

    public static byte[] generateSalt() {
        BytesKeyGenerator saltGenerator = KeyGenerators.secureRandom(32);
        return saltGenerator.generateKey();
    }

    public static String encodePassword(String password, byte[] salt) {
        byte[] hash = new byte[32];
        Argon2Parameters parameters =
                (new Argon2Parameters.Builder(2))
                        .withSalt(salt)
                        .withParallelism(1)
                        .withMemoryAsKB(15360)
                        .withIterations(2)
                        .build();
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);
        generator.generateBytes(password.toCharArray(), hash);
        return encodeArgon2Hash(hash, parameters);
    }

    public static String calculatePairwiseIdentifier(
            String subjectID, String sectorHost, byte[] salt) {
        try {
            var md = MessageDigest.getInstance("SHA-256");

            md.update(sectorHost.getBytes(StandardCharsets.UTF_8));
            md.update(subjectID.getBytes(StandardCharsets.UTF_8));

            byte[] bytes = md.digest(salt);

            var sb = Base64Url.encode(bytes);

            return "urn:fdc:gov.uk:2022:" + sb;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
