package chloe.movietalk.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.*
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import jakarta.persistence.criteria.CriteriaBuilder
import org.aspectj.apache.bcel.classfile.Module
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.cglib.core.Local
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod
import java.time.LocalDateTime

@Configuration
class SwaggerConfig {
    companion object {
        private val JWT_SCHEME_NAME = "JWT"
        private val BEARER_TOKEN_PREFIX = "Bearer"
    }

    @Bean
    fun openAPI(): OpenAPI = OpenAPI().apply {
        info = Info().apply {
            title("MovieTalk API Docs")
            description("MovieTalk 프로젝트의 API 문서입니다.")
            version("1.0.0")
        }
        addSecurityItem(SecurityRequirement().addList(JWT_SCHEME_NAME))
        components = Components().addSecuritySchemes(
            JWT_SCHEME_NAME,
            SecurityScheme().apply {
                name(JWT_SCHEME_NAME)
                type(SecurityScheme.Type.HTTP)
                scheme(BEARER_TOKEN_PREFIX)
                bearerFormat(JWT_SCHEME_NAME)
                `in`(SecurityScheme.In.HEADER)
            }
        )
    }

    @Bean
    fun operationCustomizer(): OperationCustomizer = OperationCustomizer { operation, _ ->
        operation?.let { addResponseBodyWrapperSchema(it) }
        operation
    }

    private fun addResponseBodyWrapperSchema(operation: Operation) {
        operation.responses.forEach { (statusCode, response) ->
            if (statusCode.startsWith("2") && response.content != null) {
                response.content.forEach { (_, mediaType) ->
                    mediaType?.schema?.let { originalSchema ->
                        val wrapped = wrapSchemaForSuccessResponse(originalSchema, statusCode.toIntOrNull())
                        mediaType.schema = wrapped
                    }
                }
            }
        }
    }

    private fun wrapSchemaForSuccessResponse(originalSchema: Schema<*>, statusCode: Int?): Schema<*> {
        return ObjectSchema().apply {
            addProperty("success", BooleanSchema().example(true))
            addProperty("status", IntegerSchema().example(statusCode))
            addProperty(
                "timestamp", StringSchema().format("date-time").example(LocalDateTime.now().toString())
            )
            addProperty("data", if (originalSchema is ArraySchema) wrapPageSchema(originalSchema) else originalSchema)
        }
    }

    private fun wrapPageSchema(originalSchema: Schema<*>?): Schema<*>? {
        val sortSchema = ObjectSchema().apply {
            addProperty("sorted", BooleanSchema().example(true))
            addProperty("unsorted", BooleanSchema().example(false))
            addProperty("empty", BooleanSchema().example(false))
        }

        val pageableSchema = ObjectSchema().apply {
            addProperty("pageNumber", IntegerSchema().example(0))
            addProperty("pageSize", IntegerSchema().example(10))
            addProperty("offset", IntegerSchema().example(0))
            addProperty("paged", BooleanSchema().example(true))
            addProperty("unpaged", BooleanSchema().example(false))
            addProperty("sort", sortSchema)
        }

        val pageSchema = ObjectSchema().apply {
            addProperty("content", originalSchema)
            addProperty("pageable", pageableSchema)
            addProperty("last", BooleanSchema().example(false))
            addProperty("totalElements", IntegerSchema().example(50))
            addProperty("totalPages", IntegerSchema().example(5))
            addProperty("first", BooleanSchema().example(true))
            addProperty("size", IntegerSchema().example(10))
            addProperty("number", IntegerSchema().example(0))
            addProperty("sort", sortSchema)
            addProperty("numberOfElements", IntegerSchema().example(10))
            addProperty("empty", BooleanSchema().example(false))
        }

        return pageSchema
    }
}
