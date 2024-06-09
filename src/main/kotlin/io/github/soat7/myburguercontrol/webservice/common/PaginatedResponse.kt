package io.github.soat7.myburguercontrol.infrastructure.rest.common

data class PaginatedResponse<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val currentPage: Int,
    val pageSize: Int
)
