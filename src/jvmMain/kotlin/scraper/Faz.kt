package scraper

import News
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Faz {
    companion object {
        private const val htmlClass = "ticker-news-item"
        private const val url = "https://www.faz.net/faz-live"

        fun getNews(newsList: MutableList<News>) {
            println("Scraping: $url")
            Jsoup.connect(url).get()
                .select(".$htmlClass")
                .forEach { parseToHeadline(it, newsList) }
        }

        private fun parseToHeadline(div: Element, newsList: MutableList<News>) {
            val newsContainer = div.getElementsByClass(htmlClass)
            if (newsContainer.size > 1) {
                newsContainer.forEach { parseToHeadline(it, newsList) }
                println("Unexpected number of tags: ${newsContainer.size}")
                return
            } else if (newsContainer.isEmpty()) {
                System.err.println("No tag with class:$htmlClass in here")
                return
            }
            println("found $htmlClass")

            val newsEntry = newsContainer.first()
            val titleAndLink = newsEntry?.getElementsByClass("ticker-news-title")?.first()?.getElementsByTag("a")?.first()
            val url = titleAndLink?.attr("href") ?: ""
            val overline = newsEntry?.getElementsByClass("ticker-news-super")?.first()?.wholeOwnText() ?: ""
            val title = titleAndLink?.wholeOwnText() ?: ""
            val author = newsEntry?.getElementsByClass("ticker-news-author")?.first()?.wholeOwnText() ?: ""
            val date = newsEntry?.getElementsByClass("ticker-news-time")?.first()?.wholeOwnText() ?: ""

            newsList.add(
                News(
                    title = title,
                    url = url,
                    provider = "FAZ",
                    overline = overline,
                    author = author,
                    date = date
                )
            )
        }
    }
}