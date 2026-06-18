package com.estoque.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Controle de Estoque API")
                        .description("API para gerenciamento de produtos e movimentações de estoque")
                        .version("1.0.0"))
                .tags(List.of(
                        new Tag().name("Produtos").description("Cadastro e manutenção de produtos"),
                        new Tag().name("Estoque").description("Entradas, saídas e consultas de estoque"),
                        new Tag().name("Health").description("Verificação de saúde da aplicação")
                ));
    }
}
