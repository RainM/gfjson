import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenizerTest {
    @Test
    public void parseIncrement() {
        String str = readFile("increment.json");
        var tokenizer = new Tokenizer();
        tokenizer.wrap(str);

        checkStartObject(tokenizer);
        checkString(tokenizer, "e");
        checkString(tokenizer, "depthUpdate");
        checkString(tokenizer, "E");
        checkLong(tokenizer, 123456789);
        checkString(tokenizer, "s");
        checkString(tokenizer, "BNBBTC");
        checkString(tokenizer, "U");
        checkLong(tokenizer, 157);
        checkString(tokenizer, "u");
        checkLong(tokenizer, 160);
        checkString(tokenizer, "b");
        checkStartArray(tokenizer);
        checkStartArray(tokenizer);
        checkString(tokenizer, "0.0024");
        checkString(tokenizer, "10");
        checkEndArray(tokenizer);
        checkEndArray(tokenizer);
        checkString(tokenizer, "a");
        checkStartArray(tokenizer);
        checkStartArray(tokenizer);
        checkString(tokenizer, "0.0026");
        checkString(tokenizer, "100");
        checkEndArray(tokenizer);
        checkEndArray(tokenizer);
        checkEndObject(tokenizer);
        checkEnd(tokenizer);
    }

    @Test
    public void parseCar() {
        String str = readFile("car.json");
        var tokenizer = new Tokenizer();
        tokenizer.wrap(str);

        checkStartObject(tokenizer);
        checkString(tokenizer, "engine");
        checkStartObject(tokenizer);
        checkString(tokenizer, "cylinders");
        checkLong(tokenizer, 4);
        checkString(tokenizer, "horse power");
        checkFloat(tokenizer, 1234, -1);
        checkEndObject(tokenizer);
        checkString(tokenizer, "is electric");
        checkBoolean(tokenizer, false);
        checkString(tokenizer, "is petrol");
        checkBoolean(tokenizer, true);
        checkString(tokenizer, "min temperature");
        checkLong(tokenizer, -30);
        checkString(tokenizer, "max temperature");
        checkLong(tokenizer, 50);
        checkEndObject(tokenizer);
        checkEnd(tokenizer);
    }

    private static void checkStartArray(Tokenizer tokenizer) {
        assertEquals(Token.START_ARRAY, tokenizer.next());
    }

    private static void checkEnd(Tokenizer tokenizer) {
        assertEquals(Token.END, tokenizer.next());
    }

    private static void checkEndObject(Tokenizer tokenizer) {
        assertEquals(Token.END_OBJECT, tokenizer.next());
    }

    private static void checkEndArray(Tokenizer tokenizer) {
        assertEquals(Token.END_ARRAY, tokenizer.next());
    }

    private static void checkStartObject(Tokenizer tokenizer) {
        assertEquals(Token.START_OBJECT, tokenizer.next());
    }

    @SneakyThrows
    private static String readFile(final String fileName) {
        return Files.readString(Paths.get("src", "test", "resources", fileName));
    }

    private static void checkLong(Tokenizer tokenizer, final int expected) {
        assertEquals(Token.LONG, tokenizer.next());
        assertEquals(expected, tokenizer.getLong());
    }

    private static void checkBoolean(Tokenizer tokenizer, final boolean expected) {
        assertEquals(Token.BOOLEAN, tokenizer.next());
        assertEquals(expected, tokenizer.getBoolean());
    }

    private static void checkFloat(Tokenizer tokenizer, final long expectedMantissa, final int expectedExponent) {
        assertEquals(Token.FLOAT, tokenizer.next());
        assertEquals(expectedMantissa, tokenizer.getMantissa());
        assertEquals(expectedExponent, tokenizer.getExponent());
    }

    private static void checkString(Tokenizer tokenizer, final String expected) {
        assertEquals(Token.STRING, tokenizer.next());
        assertEquals(expected, tokenizer.getString().toString());
    }
}