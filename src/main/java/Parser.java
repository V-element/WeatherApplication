import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");
    private static int indexOfTable;

    private static Document getPage() throws IOException {
        String url = "http://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't obtain date from string");
    }

    private static void printValuesOfDay(Elements values) {

        int numberOfLines = 0;
        if (indexOfTable == 0) {
            Element firstValueLine = values.get(0);
            String firstLineText = firstValueLine.text();
            if (firstLineText.contains("Утро")) {
                numberOfLines = 4;
            } else if(firstLineText.contains("День")) {
                numberOfLines = 3;
            } else if(firstLineText.contains("Вечер")) {
                numberOfLines = 2;
            } else {
                numberOfLines = 1;
            }
        } else {
            numberOfLines = 4;
        }

        for (int i = 0; i < numberOfLines; i++) {
            Element valueOfLine = values.get(i + indexOfTable);
            for (Element td : valueOfLine.select("td")) {
                System.out.print(td.text() + "   ");
            }
            System.out.println();

        }
        indexOfTable += numberOfLines;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        for (Element name: names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "    Явления     Температура     Давление    Влажность   Ветер");
            printValuesOfDay(values);
        }

    }

}
