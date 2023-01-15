package scraper

import News
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Scraper {
    val newsList = mutableListOf<News>()
   fun getNews(): List<News> {
       Sueddeutsche.getNews(newsList)
       TableMedia.getNews(newsList)
       return newsList
   }

    fun filterBy(s: String?): List<News> {
        return if (s == null) newsList else newsList.filter { it.contains(s) }
    }
}