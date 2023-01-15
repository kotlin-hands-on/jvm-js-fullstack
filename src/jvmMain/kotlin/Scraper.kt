import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class Scraper {
    val headlineList = mutableListOf<Headline>()
    fun getHeadlines() {
        Jsoup.connect("https://table.media/100-headlines/").get()
            .select(".section-presseschau")
            .forEach { parseToHeadline(it) }
    }

    private fun parseToHeadline(div: Element) {
        val divs = div.getElementsByTag("div").filter { element -> !element.hasClass("section-presseschau") }
        if (divs.size > 1) {
            divs.forEach { parseToHeadline(it) }
            println("Unexpected number of child div tags: ${divs.size}")
            return
        } else if (divs.size < 1) {
            System.err.println("No div tag in here")
            return
        }
        println("found section presseschau")
        val element = divs[0]
        val childNodes = element.childNodes()
        for (i in 0..childNodes.size - 1 step 3) {
            val text = childNodes[i]
            try {
                val anchor = childNodes[i + 1] as Element
                addHeadline(anchor, text.toString())
            } catch (e: Exception) {
            } finally {
                continue
            }
        }
    }

    private fun addHeadline(anchor: Element, text: String) {
        val url = anchor.attr("href")
        val provider = anchor.text()
        val headline = Headline(text.trim(), url, provider)
        headlineList.add(headline)
    }

    fun filterBy(s: String?): List<Headline> {
       return  if (s==null) headlineList else headlineList.filter { it.text.contains(s, true) }
    }
}