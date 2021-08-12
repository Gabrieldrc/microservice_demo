package com.gdrc.microservice_arq.store.product.repository;

import com.gdrc.microservice_arq.store.product.entity.Category;
import com.gdrc.microservice_arq.store.product.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTest;

    @Test
    public void when_find_by_category_then_return_list_of_product() {
        //given
        Product product = Product.builder()
                                .name("computer")
                                .category(
                                        Category.builder()
                                                .id(1L).build()
                                ).description("")
                                .stock(Double.parseDouble("10"))
                                .price(Double.parseDouble("1240.99"))
                                .status("Created")
                                .createAt(new Date())
                                .build();
        underTest.save(product);
        //when
        List<Product> founds = underTest.findByCategory(product.getCategory());
        //then
        assertThat(founds.size()).isEqualTo(3);


    }
}