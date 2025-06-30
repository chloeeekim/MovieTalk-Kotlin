package chloe.movietalk.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    private final String JWT_SCHEME_NAME = "JWT";
    private final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MovieTalk API Docs")
                        .description("MovieTalk 프로젝트의 API 문서입니다.")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(JWT_SCHEME_NAME, new SecurityScheme()
                                .name(JWT_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(BEARER_TOKEN_PREFIX)
                                .bearerFormat(JWT_SCHEME_NAME)
                                .in(SecurityScheme.In.HEADER)));
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            this.addResponseBodyWrapperSchema(operation);
            return operation;
        };
    }

    private void addResponseBodyWrapperSchema(Operation operation) {
        ApiResponses responses = operation.getResponses();

        for (Map.Entry<String, ApiResponse> entry : responses.entrySet()) {
            String statusCode = entry.getKey();
            ApiResponse response = entry.getValue();

            if (statusCode.startsWith("2") && response.getContent() != null) {
                Content content = response.getContent();

                content.forEach((mediaTypeKey, mediaType) -> {
                    Schema<?> originalSchema = mediaType.getSchema();
                    if (originalSchema != null) {
                        Schema<?> wrappedSchema = wrapSchemaForSuccessResponse(originalSchema, Integer.parseInt(statusCode));
                        mediaType.setSchema(wrappedSchema);
                    }
                });
            }
        }
    }

    private Schema<?> wrapSchemaForSuccessResponse(Schema<?> originalSchema, Integer statusCode) {
        final Schema<?> wrapperSchema = new ObjectSchema();

        wrapperSchema.addProperty("success", new BooleanSchema().example(true));
        wrapperSchema.addProperty("status", new IntegerSchema().example(statusCode));
        wrapperSchema.addProperty("timeStamp", new StringSchema().format("date-time")
                .example(LocalDateTime.now().toString()));

        System.out.println(originalSchema.getClass());

        // array 형식인 경우 (Page<T> 형식)
        if (originalSchema instanceof JsonSchema) {
            wrapperSchema.addProperty("data", wrapPageSchema(originalSchema));
        } else {
            wrapperSchema.addProperty("data", originalSchema);
        }

        return wrapperSchema;
    }

    private Schema<?> wrapPageSchema(Schema<?> originalSchema) {
        Schema<?> sortSchema = new ObjectSchema()
                .addProperty("sorted", new BooleanSchema().example(true))
                .addProperty("unsorted", new BooleanSchema().example(false))
                .addProperty("empty", new BooleanSchema().example(false));

        Schema<?> pageableSchema = new ObjectSchema()
                .addProperty("pageNumber", new IntegerSchema().example(0))
                .addProperty("pageSize", new IntegerSchema().example(10))
                .addProperty("offset", new IntegerSchema().example(0))
                .addProperty("paged", new BooleanSchema().example(true))
                .addProperty("unpaged", new BooleanSchema().example(false))
                .addProperty("sort", sortSchema);

        Schema<?> pageSchema = new ObjectSchema()
                .addProperty("content", originalSchema)
                .addProperty("pageable", pageableSchema)
                .addProperty("last", new BooleanSchema().example(false))
                .addProperty("totalElements", new IntegerSchema().example(50))
                .addProperty("totalPages", new IntegerSchema().example(5))
                .addProperty("first", new BooleanSchema().example(true))
                .addProperty("size", new IntegerSchema().example(10))
                .addProperty("number", new IntegerSchema().example(0))
                .addProperty("sort", sortSchema)
                .addProperty("numberOfElements", new IntegerSchema().example(10))
                .addProperty("empty", new BooleanSchema().example(false));

        return pageSchema;
    }
}
