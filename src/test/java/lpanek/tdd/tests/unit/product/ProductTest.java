package lpanek.tdd.tests.unit.product;

import static lpanek.tdd.tests.util.ConstructingUtil.money;
import static lpanek.tdd.tests.util.ConstructingUtil.productType;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.product.Product;
import lpanek.tdd.product.ProductType;

@RunWith(JUnitParamsRunner.class)
public class ProductTest {

    @Test
    @Parameters(method = "getTestData_ProductType")
    public void should_HaveSpecifiedType_When_Constructed(ProductType productType) {
        // when
        Product product = new Product(productType);

        // then
        assertThat(product.getType()).isEqualTo(productType);
    }

    @SuppressWarnings("unused")
    private ProductType[] getTestData_ProductType() {
        return new ProductType[]{
            productType("Banana",        money(1, 80)),
            productType("Cola drink",    money(3, 0)),
            productType("Chocolate bar", money(0, 90))
        };
    }
}