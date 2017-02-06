package lpanek.tdd.tests.unit.product;

import static lpanek.tdd.tests.util.ConstructingUtil.money;
import static lpanek.tdd.tests.util.ConstructingUtil.price;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.product.ProductType;

public class ProductTypeTest {

    @Test
    public void should_HaveSpecifiedNameAndPrice_When_Constructed() {
        // when
        ProductType productType = new ProductType("Banana", price(1, 80));

        // then
        assertThat(productType.getPrice()).isEqualTo(money(1, 80));
    }
}
