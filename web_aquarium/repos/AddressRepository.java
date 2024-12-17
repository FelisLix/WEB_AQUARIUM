package web_aquarium.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import web_aquarium.data.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    Address findAddressByCustomer_Id(Integer customerId);

}