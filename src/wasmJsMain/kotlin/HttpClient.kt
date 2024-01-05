// Until Ktor starts work with wasmJs
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Promise

class HttpClient {
    suspend inline fun <reified T> get(url: String): T =
        Json.decodeFromString<T>(fetchAsync(url))

    suspend inline fun <reified T> post(url: String, body: T) {
        fetchAsync(url, FetchOptions(method = "POST", body = Json.encodeToString(body), headers = mapOf("Content-Type" to "application/json")))
    }

    suspend inline fun delete(url: String) {
        fetchAsync(url, FetchOptions(method = "DELETE"))
    }
}

private fun buildFetchOptions(method: JsAny?, body: JsAny?, headers: JsAny?): JsAny =
    js("{ method: method, body: body, headers: headers }")

private fun fetch(url: String, opts: JsAny? = null): Promise<JsString> =
    js("fetch(url, opts).then(x => x.text())")

private fun <T: JsAny?> createJsArray(): JsArray<T> = js("[]")

private fun Map<String, String>.toJsEntries(): JsArray<JsArray<JsString>> {
    val result = createJsArray<JsArray<JsString>>()
    for ((key, value) in this) {
        val entry = createJsArray<JsString>().apply {
            set(0, key.toJsString())
            set(1, value.toJsString())
        }
        result[result.length] = entry
    }
    return result
}

suspend fun fetchAsync(url: String, opts: FetchOptions? = null): String {
    val promise = fetch(url, buildFetchOptions(opts?.method?.toJsString(), opts?.body?.toJsString(), opts?.headers?.toJsEntries()))
    return suspendCoroutine { cont ->
        promise.then(
            onFulfilled = { cont.resume(it.toString()).toJsReference() },
            onRejected = { cont.resumeWithException(IllegalStateException("Promise error: $it")).toJsReference() }
        )
    }
}

data class FetchOptions(
    val method: String? = null,
    val body: String? = null,
    val headers: Map<String, String> = emptyMap(),
)

