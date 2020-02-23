package camp.nextstep.edu.kitchenpos.bo;

import camp.nextstep.edu.kitchenpos.dao.MenuDao;
import camp.nextstep.edu.kitchenpos.dao.MenuGroupDao;
import camp.nextstep.edu.kitchenpos.dao.MenuProductDao;
import camp.nextstep.edu.kitchenpos.dao.ProductDao;
import camp.nextstep.edu.kitchenpos.model.Menu;
import camp.nextstep.edu.kitchenpos.model.MenuProduct;
import camp.nextstep.edu.kitchenpos.model.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuBoTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private MenuBo menuBo;

    @DisplayName("메뉴를 정상적으로 생성한다.")
    @Test
    void create() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(2_000L));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2L);
        List<MenuProduct> menuProducts = Lists.list(menuProduct);

        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1_000L));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(menuProducts);

        given(menuGroupDao.existsById(any(Long.class)))
                .willReturn(true);

        given(productDao.findById(any(Long.class)))
                .willReturn(Optional.of(product));

        given(menuDao.save(any(Menu.class)))
                .willAnswer((Answer<Menu>) invocation -> {
                    menu.setId(1L);
                    return menu;
                });

        given(menuProductDao.save(any(MenuProduct.class)))
                .willReturn(menuProduct);

        // when
        Menu result = menuBo.create(menu);

        // then
        assertThat(result).isEqualTo(menu);
    }
}
