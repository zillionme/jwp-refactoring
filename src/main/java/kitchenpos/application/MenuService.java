package kitchenpos.application;

import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.exception.NotExistMenuGroupException;
import kitchenpos.domain.exception.NotExistProductException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    
    private final MenuGroupRepository menuGroupRepository;
    
    private final ProductRepository productRepository;
    
    private final MenuRepository menuRepository;
    
    public MenuService(final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository,
                       final MenuRepository menuRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
    }
    
    @Transactional
    public Menu create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = getMenuGroup(request);
        final List<MenuProduct> menuProducts = getMenuProducts(request.getMenuProductCreateRequests());
        final Menu menu = Menu.of(request.getMenuName(),
                request.getMenuPrice(),
                menuGroup,
                menuProducts);
        
        return menuRepository.save(menu);
    }
    
    private MenuGroup getMenuGroup(final MenuCreateRequest request) {
        return menuGroupRepository.findById(request.getMenuGroupId())
                                  .orElseThrow(() -> new NotExistMenuGroupException("존재하지 않는 메뉴 그룹입니다"));
    }
    
    private List<MenuProduct> getMenuProducts(final List<MenuProductCreateRequest> request) {
        return request.stream()
                      .map(menuProductCreateRequest ->
                              new MenuProduct(null,
                                      getProduct(menuProductCreateRequest.getProductId()),
                                      menuProductCreateRequest.getQuantity()
                              ))
                      .collect(Collectors.toList());
    }
    
    private Product getProduct(final Long productId) {
        return productRepository.findById(productId)
                                .orElseThrow(() -> new NotExistProductException("존재하지 않는 상품입니다"));
    }
    
    
    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
