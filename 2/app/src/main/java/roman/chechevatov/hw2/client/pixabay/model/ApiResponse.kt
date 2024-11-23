package roman.chechevatov.hw2.client.pixabay.model

data class ApiResponse(
    val total: Long,
    val totalHits: Long,
    val hits: List<ImageResponse>
) {
}