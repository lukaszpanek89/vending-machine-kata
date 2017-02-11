package lpanek.tdd.tests.unit.domain.product;

import static lpanek.tdd.tests.util.ConstructingUtil.price;
import static lpanek.tdd.tests.util.ConstructingUtil.productType;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.domain.product.Product;
import lpanek.tdd.domain.product.ProductType;

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
        return new ProductType[] {
            productType("Banana",        price(1, 80)),
            productType("Cola drink",    price(3, 0)),
            productType("Chocolate bar", price(0, 90))
        };
    }

    @Test
    @Parameters(method = "getTestData_TwoProductObjectsHavingEqualFields")
    public void should_TwoProductObjectsBeEqual_When_HavingEqualFields(Product product1, Product product2) {
        assertThat(product1).isEqualTo(product2);
        assertThat(product1.hashCode()).isEqualTo(product2.hashCode());
    }

    @Test
    @Parameters(method = "getTestData_TwoProductObjectsHavingDifferentFields")
    public void should_TwoProductObjectsNotBeEqual_When_HavingDifferentFields(Product product1, Product product2) {
        assertThat(product1).isNotEqualTo(product2);
    }

    @SuppressWarnings("unused")
    private Product[][] getTestData_TwoProductObjectsHavingEqualFields() {
        return new Product[][] {
                new Product[] {new Product(productType("Sandwich", price(4, 30))),      new Product(productType("Sandwich", price(4, 30)))},
                new Product[] {new Product(productType("Mineral water", price(1, 60))), new Product(productType("Mineral water", price(1, 60)))},
                new Product[] {new Product(productType("Orange juice", price(3, 0))),   new Product(productType("Orange juice", price(3, 0)))},
                new Product[] {new Product(productType("Tissues", price(0, 70))),       new Product(productType("Tissues", price(0, 70)))}
        };
    }

    @SuppressWarnings("unused")
    private Product[][] getTestData_TwoProductObjectsHavingDifferentFields() {
        return new Product[][] {
                new Product[] {new Product(productType("Snacks", price(4, 30))),        new Product(productType("Sandwich", price(4, 30)))},
                new Product[] {new Product(productType("Mineral water", price(2, 70))), new Product(productType("Mineral water", price(1, 50)))},
                new Product[] {new Product(productType("Mineral water", price(2, 70))), new Product(productType("Mineral water", price(2, 50)))},
                new Product[] {new Product(productType("Mineral water", price(2, 70))), new Product(productType("Mineral water", price(1, 70)))}
        };
    }
}
