/**
 * 
 */
package guru.springframework.spring5webfluxrest.controllers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author bobbyfeng
 *
 */
public class VendorControllerTest {

	WebTestClient webTestClient;
	VendorRepository vendorRepository;
	VendorController vendorController;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		vendorRepository = Mockito.mock(VendorRepository.class);
		vendorController = new VendorController(vendorRepository);
		webTestClient = WebTestClient.bindToController(vendorController).build();
	}

	@Test
	public void list() {
		BDDMockito.given(vendorRepository.findAll())
				.willReturn(Flux.just(Vendor.builder().firstName("Michael").lastName("Tan").build(),
						Vendor.builder().firstName("Bo").lastName("Feng").build()));

		webTestClient.get().uri("/api/v1/vendors/").exchange().expectBodyList(Category.class).hasSize(2);
	}

	@Test
	public void getById() {

		BDDMockito.given(vendorRepository.findById("someid"))
				.willReturn(Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build()));

		webTestClient.get().uri("/api/v1/vendors/someid").exchange().expectBody(Vendor.class);
	}

}
