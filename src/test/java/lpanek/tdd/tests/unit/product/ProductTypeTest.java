package lpanek.tdd.tests.unit.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import lpanek.tdd.payment.Money;
import lpanek.tdd.product.ProductType;

public class ProductTypeTest {

    @Test
    public void should_HaveSpecifiedNameAndPrice_When_SuccessfullyConstructed() {
        // when
        ProductType productType = new ProductType("Banana", new Money(1, 80));

        // then
        assertThat(productType.getPrice()).isEqualTo(new Money(1, 80));
    }
}
