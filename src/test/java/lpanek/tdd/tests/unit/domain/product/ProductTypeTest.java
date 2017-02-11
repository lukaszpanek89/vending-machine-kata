package lpanek.tdd.tests.unit.domain.product;

import static lpanek.tdd.tests.util.ConstructingUtil.price;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lpanek.tdd.domain.payment.Money;
import lpanek.tdd.domain.product.ProductType;

@RunWith(JUnitParamsRunner.class)
public class ProductTypeTest {

    @Test
    @Parameters(method = "getTestData_ProductNameAndPrice")
    public void should_HaveSpecifiedNameAndPrice_When_Constructed(String productName, Money productPrice) {
        // when
        ProductType productType = new ProductType(productName, productPrice);

        // then
        assertThat(productType.getName()).isEqualTo(productName);
        assertThat(productType.getPrice()).isEqualTo(productPrice);
    }

    @Test
    @Parameters(method = "getTestData_TwoProductTypeObjectsHavingEqualFields")
    public void should_TwoProductTypeObjectsBeEqual_When_HavingEqualFields(ProductType productType1, ProductType productType2) {
        assertThat(productType1).isEqualTo(productType2);
    }

    @Test
    @Parameters(method = "getTestData_TwoProductTypeObjectsHavingDifferentFields")
    public void should_TwoProductTypeObjectsNotBeEqual_When_HavingDifferentFields(ProductType productType1, ProductType productType2) {
        assertThat(productType1).isNotEqualTo(productType2);
    }

    @SuppressWarnings("unused")
    private Object[][] getTestData_ProductNameAndPrice() {
        return new Object[][] {
                new Object[] {"Sandwich",      price(4, 30)},
                new Object[] {"Cola drink",    price(2, 50)},
                new Object[] {"Apple juice",   price(2, 0)},
                new Object[] {"Tissues",       price(0, 60)}
        };
    }

    @SuppressWarnings("unused")
    private ProductType[][] getTestData_TwoProductTypeObjectsHavingEqualFields() {
        return new ProductType[][] {
                new ProductType[] {new ProductType("Sandwich", price(4, 30)),      new ProductType("Sandwich", price(4, 30))},
                new ProductType[] {new ProductType("Mineral water", price(1, 60)), new ProductType("Mineral water", price(1, 60))},
                new ProductType[] {new ProductType("Orange juice", price(3, 0)),   new ProductType("Orange juice", price(3, 0))},
                new ProductType[] {new ProductType("Tissues", price(0, 70)),       new ProductType("Tissues", price(0, 70))}
        };
    }

    @SuppressWarnings("unused")
    private ProductType[][] getTestData_TwoProductTypeObjectsHavingDifferentFields() {
        return new ProductType[][] {
                new ProductType[] {new ProductType("Snacks", price(4, 30)),        new ProductType("Sandwich", price(4, 30))},
                new ProductType[] {new ProductType("Mineral water", price(2, 70)), new ProductType("Mineral water", price(1, 50))},
                new ProductType[] {new ProductType("Mineral water", price(2, 70)), new ProductType("Mineral water", price(2, 50))},
                new ProductType[] {new ProductType("Mineral water", price(2, 70)), new ProductType("Mineral water", price(1, 70))}
        };
    }
}
