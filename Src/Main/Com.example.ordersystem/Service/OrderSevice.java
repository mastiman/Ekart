@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        order.getItems().forEach(i -> i.setOrder(order));
        return orderRepository.save(order);
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders(OrderStatus status) {
        return status != null ? orderRepository.findByStatus(status)
                              : orderRepository.findAll();
    }

    public Order cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(null); // or mark as CANCELLED if enum is extended
            return orderRepository.save(order);
        } else {
            throw new IllegalStateException("Cannot cancel non-pending order");
        }
    }

    @Scheduled(fixedRate = 300000)
    public void updatePendingToProcessing() {
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        for (Order order : pendingOrders) {
            order.setStatus(OrderStatus.PROCESSING);
        }
        orderRepository.saveAll(pendingOrders);
    }
}
