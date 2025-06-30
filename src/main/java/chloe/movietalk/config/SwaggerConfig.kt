package chloe.movietalk.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.*
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod
import java.time.LocalDateTime

@Configuration
class SwaggerConfig {
    private val JWT_SCHEME_NAME = "JWT"
    private val BEARER_TOKEN_PREFIX = "Bearer"

    @Bean
    fun openAPI(): OpenAPI? {
        return OpenAPI()
            .info(
                Info()
                    .title("MovieTalk API Docs")
                    .description("MovieTalk 프로젝트의 API 문서입니다.")
                    .version("1.0.0")
            )
            .addSecurityItem(SecurityRequirement().addList(JWT_SCHEME_NAME))
            .components(
                Components()
                    .addSecuritySchemes(
                        JWT_SCHEME_NAME, SecurityScheme()
                            .name(JWT_SCHEME_NAME)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme(BEARER_TOKEN_PREFIX)
                            .bearerFormat(JWT_SCHEME_NAME)
                            .`in`(SecurityScheme.In.HEADER)
                    )
            )
    }

    @Bean
    fun operationCustomizer(): OperationCustomizer {
        return OperationCustomizer { operation: Operation?, handlerMethod: HandlerMethod? ->
            this.addResponseBodyWrapperSchema(operation!!)
            operation
        }
    }

    private fun addResponseBodyWrapperSchema(operation: Operation) {
        val responses = operation.getResponses()

        for (entry in responses.entries) {
            val statusCode = entry.key
            val response = entry.value

            if (statusCode.startsWith("2") && response.getContent() != null) {
                val content = response.getContent()

                content.forEach { (mediaTypeKey: String?, mediaType: MediaType?) ->
                    val originalSchema = mediaType!!.getSchema()
                    if (originalSchema != null) {
                        val wrappedSchema = wrapSchemaForSuccessResponse(originalSchema, statusCode.toInt())
                        mediaType.setSchema(wrappedSchema)
                    }
                }
            }
        }
    }

    private fun wrapSchemaForSuccessResponse(originalSchema: Schema<*>, statusCode: Int?): Schema<*> {
        val wrapperSchema: Schema<*> = ObjectSchema()

        wrapperSchema.addProperty("success", BooleanSchema().example(true))
        wrapperSchema.addProperty("status", IntegerSchema().example(statusCode))
        wrapperSchema.addProperty(
            "timeStamp", StringSchema().format("date-time")
                .example(LocalDateTime.now().toString())
        )

        println(originalSchema.javaClass)

        // array 형식인 경우 (Page<T> 형식)
        if (originalSchema is JsonSchema) {
            wrapperSchema.addProperty("data", wrapPageSchema(originalSchema))
        } else {
            wrapperSchema.addProperty("data", originalSchema)
        }

        return wrapperSchema
    }

    private fun wrapPageSchema(originalSchema: Schema<*>?): Schema<*>? {
        val sortSchema = ObjectSchema()
            .addProperty("sorted", BooleanSchema().example(true))
            .addProperty("unsorted", BooleanSchema().example(false))
            .addProperty("empty", BooleanSchema().example(false))

        val pageableSchema = ObjectSchema()
            .addProperty("pageNumber", IntegerSchema().example(0))
            .addProperty("pageSize", IntegerSchema().example(10))
            .addProperty("offset", IntegerSchema().example(0))
            .addProperty("paged", BooleanSchema().example(true))
            .addProperty("unpaged", BooleanSchema().example(false))
            .addProperty("sort", sortSchema)

        val pageSchema = ObjectSchema()
            .addProperty("content", originalSchema)
            .addProperty("pageable", pageableSchema)
            .addProperty("last", BooleanSchema().example(false))
            .addProperty("totalElements", IntegerSchema().example(50))
            .addProperty("totalPages", IntegerSchema().example(5))
            .addProperty("first", BooleanSchema().example(true))
            .addProperty("size", IntegerSchema().example(10))
            .addProperty("number", IntegerSchema().example(0))
            .addProperty("sort", sortSchema)
            .addProperty("numberOfElements", IntegerSchema().example(10))
            .addProperty("empty", BooleanSchema().example(false))

        return pageSchema
    }
}
