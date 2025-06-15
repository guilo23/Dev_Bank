package com.bia.dev_bank.RepositoryTest;

import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldSaveAndFindCustomer() {
        Customer customer = new Customer(
                null,
                "João",
                "joao@email.com",
                "1234",
                "1990-05-20",
                "987.654.321-00",
                "11911112222",
                List.of()
        );

        Customer saved = customerRepository.save(customer);
        assertNotNull(saved.getId());

        Customer found = customerRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("João", found.getName());
    }

    @Test
    void shouldDeleteCustomer() {
        Customer customer = new Customer(
                null, "Ana", "ana@email.com", "1234", "1990-05-20", "321.654.987-00", "11933334444", List.of()
        );
        Customer saved = customerRepository.save(customer);
        Long id = saved.getId();

        customerRepository.deleteById(id);

        assertTrue(customerRepository.findById(id).isEmpty());
    }
}
