import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {
        String targetDir = "d:\\CopyLenta\\";
        Document doc = null;
        try {
            doc = Jsoup.connect("https://lenta.ru").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String title = doc.title();
        System.out.println("Title : " + title);

        Elements picture = doc.select(" img[src~=(?i)\\.(png|jpe?g)]");
        Pattern pattern = Pattern.compile("[^/][a-zA-Z0-9_]+\\.(png|jpe?g)");

        picture.forEach(p -> {
            try {
                Matcher matcher = pattern.matcher(p.attr("src"));
                matcher.find();

                InputStream in = URI.create(p.attr("src")).toURL().openStream();
                Files.copy(in, Paths.get(targetDir, matcher.group()), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Скопирован: " + p.attr("src") + " => " + Paths.get(targetDir, matcher.group()) );

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
