package scraper

import News
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Spiegel {
    companion object {
        private const val url = "https://www.spiegel.de/schlagzeilen/"

        fun getNews(newsList: MutableList<News>) {
            println("Scraping: $url")
            Jsoup.connect(url).get()
                .getElementsByTag("article")
                .forEach { parseToHeadline(it, newsList) }
        }

        private fun parseToHeadline(div: Element, newsList: MutableList<News>) {
            val anchor = div.getElementsByTag("a")
            if (anchor.size > 1) {
                anchor.forEach { parseToHeadline(it, newsList) }
                println("Unexpected number of tags: ${anchor.size}")
                return
            } else if (anchor.isEmpty()) {
                System.err.println("No anchor tag in here")
                return
            }
            println("found anchor tag")

            val date = div.getElementsByClass("items-end").first()?.firstElementChild()?.wholeOwnText() ?: ""
            val url = anchor.attr("href")
            val title = anchor.attr("title")
            //TODO val author = they are there


            newsList.add(
                News(
                    title = title,
                    url = url,
                    provider = "Spiegel",
                    date = date
                )
            )
        }
    }
}