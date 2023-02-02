package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.StringSynonym;
import com.rolfje.anonimatron.synonyms.Synonym;

import java.util.Map;
import java.util.Random;

/**
 * Generates an output string that looks like a BCrypt hash but isn't valid.
 */
public class BCryptHashAnonymizer implements Anonymizer {

    protected static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    protected static final String DEFAULT_VARIANT = "2b";
    protected static final String DEFAULT_DIFFICULTY = "14";

    protected static final Random RANDOM = new Random();

    @Override
    public String getType() {
        return "BCRYPT_HASH";
    }

    @Override
    public Synonym anonymize(Object from, int size, boolean shortlived, Map<String, String> parameters) {
        if (from == null) {
            return null;
        }

        if (from instanceof String) {
            String variant = DEFAULT_VARIANT;
            if (parameters != null && parameters.containsKey("variant")) {
                variant = parameters.get("variant");
            }

            String difficulty = DEFAULT_DIFFICULTY;
            if (parameters != null && parameters.containsKey("difficulty")) {
                difficulty = parameters.get("difficulty");
            }

            return anonymize(from, size, shortlived, variant, difficulty);
        }

        throw new UnsupportedOperationException("Can not anonymize objects of type " + from.getClass());
    }

    @Override
    public Synonym anonymize(Object from, int size, boolean shortlived) {
         if (from == null) {
            return null;
        }

        if (from instanceof String) {
            return anonymize(from, size, shortlived, DEFAULT_VARIANT, DEFAULT_DIFFICULTY);
        }

        throw new UnsupportedOperationException("Can not anonymize objects of type " + from.getClass());
    }

    private Synonym anonymize(Object from, int size, boolean shortlived, String variant, String difficulty) {
        String fromString = from.toString();

        int length = fromString.length();
        StringBuilder sb = new StringBuilder("$");
        sb.append(variant);
        sb.append("$");
        sb.append(difficulty);
        sb.append("$");

        for (int i = sb.length(); i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }

        return new StringSynonym(
                getType(),
                fromString,
                sb.toString(),
                shortlived
        );
    }

}
