package scraper

import News
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Sueddeutsche {
    companion object {
        val htmlClass = "entrylist__content"
        val url = "https://www.sueddeutsche.de/news"

        fun getNews(newsList: MutableList<News>) {
            println("Scraping: ${url}")
            Jsoup.connect(url).get()
                .select(".${htmlClass}")
                .forEach { parseToHeadline(it, newsList) }
        }

        private fun parseToHeadline(div: Element, newsList: MutableList<News>) {
            val newsContainer = div.getElementsByClass(htmlClass)
            if (newsContainer.size > 1) {
                newsContainer.forEach { parseToHeadline(it, newsList) }
                println("Unexpected number of child div tags: ${newsContainer.size}")
                return
            } else if (newsContainer.size < 1) {
                System.err.println("No div tag in here")
                return
            }
            println("found ${htmlClass}")

            val newsEntry = newsContainer.first()
            val url = newsEntry?.getElementsByClass("entrylist__link")?.first()?.attr("href") ?: ""
            val overline = newsEntry?.getElementsByClass("entrylist__overline")?.first()?.text() ?: ""
            val title = newsEntry?.getElementsByClass("entrylist__title")?.first()?.text() ?: ""
            val author = newsEntry?.getElementsByClass("entrylist__author")?.first()?.text() ?: ""
            val teaser = newsEntry?.getElementsByClass("entrylist__detail")?.first()?.wholeOwnText() ?: ""
            val breadcrumbs = newsEntry?.getElementsByClass("breadcrumb-list__item")?.map { it.text() } ?: emptyList()

            newsList.add(
                News(
                    title = title,
                    url = url,
                    provider = "Sueddeutsche",
                    overline = overline,
                    teaser = teaser,
                    breadcrumbs = breadcrumbs,
                    author = author
                )
            )
        }
    }
}