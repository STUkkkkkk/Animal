package stukk.common;

public class FilePath {
    public static final String basePath = System.getProperty("user.dir") + "\\img\\";

    public static void main(String[] args) {
        System.out.println(basePath);
    }
}
