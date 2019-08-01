import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {

        String targetDir = "d:\\CopyLenta\\";
        final String SELECT_PICTURE = " img[src~=(?i)\\.(png|jpe?g)]";
        final String PATTERN_SELECT = "[^/][a-zA-Z0-9_]+\\.(png|jpe?g)";
        final String ATTRIBUTE_SELECT = "src";
        Document doc = null;

        try {
            doc = Jsoup.connect("https://lenta.ru").get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Неверный адрес ресурса => Завершение работы программы.");
            System.exit(1);
        }

        if (doc == null) {
            System.out.println("Неверный адрес ресурса1");
            System.exit(1);
        }

        String title = doc.title();
        System.out.println("Title : " + title);

        Elements picture = doc.select(SELECT_PICTURE);
        Pattern pattern = Pattern.compile(PATTERN_SELECT);

        picture.forEach(p -> copyFile(p.attr(ATTRIBUTE_SELECT), targetDir, pattern));

    }

    private static void copyFile(String addressFile, String targetDir, Pattern pattern){
        try {
            Matcher matcher = pattern.matcher(addressFile);
            matcher.find();
//                InputStream in = URI.create(p.attr("src")).toURL().openStream();
            InputStream in = new URL(addressFile).openStream();
            Files.copy(in, Paths.get(targetDir, matcher.group()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Скопирован: " + addressFile + " => " + Paths.get(targetDir, matcher.group()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
