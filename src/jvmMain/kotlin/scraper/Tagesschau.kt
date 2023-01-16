package scraper

import News
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Tagesschau {
    companion object {
        private const val htmlClass = "boxCon"
        private const val baseUrl = "https://www.tagesschau.de"
        private const val url = "$baseUrl/allemeldungen/"

        fun getNews(newsList: MutableList<News>) {
            println("Scraping: $url")
            Jsoup.connect(url).get()
                .select(".$htmlClass")
                .forEach { container ->
                    container.getElementsByTag("li").forEach { li ->
                        parseToHeadline(li, newsList)
                    }
                }
        }

        private fun parseToHeadline(li: Element, newsList: MutableList<News>) {
            val anchor = li.getElementsByTag("a")
            val url = anchor.attr("href")
            val title = anchor.text()

            newsList.add(
                News(
                    title = title,
                    url = "$baseUrl$url",
                    provider = "Tagesschau",
                )
            )
        }
    }
}