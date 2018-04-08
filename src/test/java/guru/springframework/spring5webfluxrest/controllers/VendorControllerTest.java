/**
 * 
 */
package guru.springframework.spring5webfluxrest.controllers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
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
	
	@Test
	public void testCreateVendor() {
		BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
				.willReturn(Flux.just(Vendor.builder().firstName("Bo").lastName("Feng").build()));

		Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build());

		webTestClient.post().uri("/api/v1/vendors").body(vendorToSaveMono, Vendor.class).exchange().expectStatus()
				.isCreated();
	}
	
	@Test
	public void TestUpdateVendor() {
		BDDMockito.given(vendorRepository.save(any(Vendor.class)))
		.willReturn(Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build()));

		Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build());

		webTestClient.put().uri("api/v1/vendors/1").body(vendorToUpdateMono, Vendor.class).exchange().expectStatus()
		.isOk();
	}

}
