package scraper

import News
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Zdf {
    companion object {
        private const val htmlClass = "container"
        private const val url = "https://www.zdf.de/nachrichten/nachrichtenticker-100.html"

        fun getNews(newsList: MutableList<News>) {
            println("Scraping: $url")
            Jsoup.connect(url).get()
                .select(".$htmlClass")
                .forEach { container ->
                    parseToHeadline(container, newsList)
                }
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
            val overline = newsEntry?.getElementsByClass("teaser-cat-category")?.first()?.text() ?: ""
            val title = newsEntry?.getElementsByClass("normal-space")?.first()?.wholeOwnText() ?: ""
            val text = newsEntry?.getElementsByClass("panel-content")?.first()?.wholeOwnText() ?: ""
            val date = newsEntry?.getElementsByClass("teaser-time")?.first()?.wholeOwnText() ?: ""

            newsList.add(
                News(
                    title = title,
                    provider = "ZDF",
                    overline = overline,
                    text = text,
                    date = date
                )
            )
        }
    }
}