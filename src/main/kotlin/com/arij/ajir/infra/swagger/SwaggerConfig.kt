package com.arij.ajir.infra.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// 나중에 지워질 것
@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .addSecurityItem(
            SecurityRequirement().addList("Bearer Authentication")
        )
        .components(Components())
        .info(
            Info()
                .title("AJIR API")
                .description("테스트하려고 만든 것 나중에 지워질지도?")
                .version("1.0.0")
        )
}