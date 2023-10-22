package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    
    @ManyToOne
    private Menu menu;
    
    @ManyToOne
    private Product product;
    
    @Embedded
    private long quantity;
    
    public MenuProduct(final Menu menu,
                       final Product product,
                       final long quantity) {
        this(null, menu, product, quantity);
    }
    
    public MenuProduct(final Long seq,
                       final Menu menu,
                       final Product product,
                       final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }
    
    public Long getSeq() {
        return seq;
    }
    
    public Menu getMenu() {
        return menu;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public long getQuantity() {
        return quantity;
    }
}
