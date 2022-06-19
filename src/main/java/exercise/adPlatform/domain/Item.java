package exercise.adPlatform.domain;

import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
public class Item {
    private static AtomicLong key = new AtomicLong(1000000000);

    @Id
    @Column(name="item_id")
    private Long id;

    private String itemName;
    private int price;
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "item")
    List<Advertisement> advertisements = new ArrayList<>();

    protected Item() {}

    private Item(String itemName, int price, int stock) {
        this.id = key.getAndIncrement();
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
    }

    public static Item getInstance(String itemName, int price, int stock) {
        return new Item(itemName, price, stock);
    }

    public void setCompany(Company company) {
        this.company = company;
        company.getItems().add(this);
    }

    public void update(Item newItem) {
        this.itemName = newItem.getItemName();
        this.price = newItem.getPrice();
        this.stock = newItem.getStock();
    }
}
