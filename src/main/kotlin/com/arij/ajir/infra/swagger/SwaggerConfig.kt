package com.arij.ajir.infra.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// 나중에 지워질 것
@Configuration
class SwaggerConfig {

    @Bean
    fun openApi(): OpenAPI? {
        val apiKey = SecurityScheme() // 보안 스키마 정의
            .type(SecurityScheme.Type.APIKEY) // APIKEY를 사용
            .`in`(SecurityScheme.In.HEADER) // APIKEY는 "header"에 위치
            .name("Authorization") // APIKEY의 이름이 "Authorization"

        val securityRequirement = SecurityRequirement() // API의 보안 요구사항 정의
            .addList("Bearer Token") // 보안 요구사항으로 "Bearer Token" 추가

        return OpenAPI()
            .components( // OpenAPI 객체의 컴포넌트를 설정
                Components()
                    .addSecuritySchemes("Bearer Token", apiKey) // 컴포넌트에 이름이 "Bearer Token"인 보안 스키마 추가
            )
            .addSecurityItem(securityRequirement) // 보안 요구사항 추가
            .info(Info().title("Todo API").description("Todo API schema").version("1.0.0"))
    }
}