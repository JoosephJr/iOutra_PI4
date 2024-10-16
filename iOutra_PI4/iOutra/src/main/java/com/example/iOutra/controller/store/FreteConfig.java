package com.example.iOutra.controller.store;


import com.example.iOutra.model.Frete;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Random;

@Configuration
public class FreteConfig {

    private BigDecimal valor;

    public FreteConfig() {
        generateRandomValue();
    }

    private void generateRandomValue() {
        Random random = new Random();
        valor = BigDecimal.valueOf(9 + (30 - 9) * random.nextDouble());
    }

    @Bean
    public Frete freteExpresso() {
        return new Frete(valor, "Expresso", valor, 3);
    }
    

    @Bean
    public Frete freteEconomico() {
        BigDecimal valorComDesconto = valor.multiply(BigDecimal.valueOf(0.8)); // 20% discount
        return new Frete(valorComDesconto, "Econômico", valorComDesconto, 8);
    }

    @Bean
    public Frete fretePadrao() {
        BigDecimal valorComDesconto = valor.multiply(BigDecimal.valueOf(0.9)); // 10% discount
        return new Frete(valorComDesconto, "Padrão", valorComDesconto, 5);
    }
}
