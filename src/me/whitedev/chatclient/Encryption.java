package me.whitedev.chatclient;

public class Encryption {

    public StringBuilder builder = new StringBuilder();
    public String encrypt(Object input) {
        builder.setLength(0);
        for (char object : String.valueOf(input).toCharArray()) {
            builder.append(Character.toChars(object * 354));
        }
        return builder.toString();
    }

    public String decrypt(Object input) {
        builder.setLength(0);
        for (char object : String.valueOf(input).toCharArray()) {
            builder.append(Character.toChars(object / 354));
        }
        return builder.toString();
    }
}
