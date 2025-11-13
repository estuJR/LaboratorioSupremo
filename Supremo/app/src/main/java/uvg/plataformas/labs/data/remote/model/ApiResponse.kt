package uvg.plataformas.labs.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetsResponse(
    @SerialName("data") val data: List<Asset>,
    @SerialName("timestamp") val timestamp: Long
)

@Serializable
data class Asset(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String? = null,
    val maxSupply: String? = null,
    val marketCapUsd: String? = null,
    val volumeUsd24Hr: String? = null,
    val priceUsd: String,
    val changePercent24Hr: String? = null,
    val vwap24Hr: String? = null
)

@Serializable
data class AssetDetailResponse(
    @SerialName("data") val data: Asset,
    @SerialName("timestamp") val timestamp: Long
)