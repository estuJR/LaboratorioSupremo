package uvg.plataformas.labs.data.remote

import android.util.Log
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import uvg.plataformas.labs.data.remote.model.AssetsResponse
import uvg.plataformas.labs.data.remote.model.AssetDetailResponse

class ApiService {

    private val client = KtorClient.client
    private val baseUrl = "https://rest.coincap.io/v3"

    suspend fun getAssets(): Result<AssetsResponse> {
        return try {
            val httpResponse: HttpResponse = client.get("$baseUrl/assets") {
                contentType(ContentType.Application.Json)
            }
            val response: AssetsResponse = httpResponse.body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAssetDetail(assetId: String): Result<AssetDetailResponse> {
        return try {
            val httpResponse: HttpResponse = client.get("$baseUrl/assets/$assetId") {
                contentType(ContentType.Application.Json)
            }
            val response: AssetDetailResponse = httpResponse.body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}