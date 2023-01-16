package scraper

import News
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Tonline {
    companion object {
        private const val htmlClass = "einr7x61"
        private const val url = "https://www.t-online.de/schlagzeilen/"

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
            val titleAndLink = newsEntry?.getElementsByTag("a")?.first()
            val url = titleAndLink?.attr("href") ?: ""
            val title = titleAndLink?.wholeOwnText() ?: ""
            val overline = newsEntry?.getElementsByClass("css-169b1y4")?.first()?.text() ?: ""

            newsList.add(
                News(
                    title = title,
                    url = url,
                    provider = "T-Online",
                    overline = overline
                )
            )
        }
    }
}