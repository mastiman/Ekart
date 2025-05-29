@Entity
public class OrderItem {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
