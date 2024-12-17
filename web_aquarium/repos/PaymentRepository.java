package web_aquarium.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import web_aquarium.data.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findPaymentByOrder_Id(Integer orderId);
}