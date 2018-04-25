/**
 * 
 */
package guru.springframework.spring5webfluxrest.controllers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.BDDMockito.given;
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
		given(vendorRepository.findAll())
				.willReturn(Flux.just(Vendor.builder().firstName("Michael").lastName("Tan").build(),
						Vendor.builder().firstName("Bo").lastName("Feng").build()));

		webTestClient.get().uri("/api/v1/vendors/").exchange().expectBodyList(Category.class).hasSize(2);
	}

	@Test
	public void getById() {

		given(vendorRepository.findById("someid"))
				.willReturn(Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build()));

		webTestClient.get().uri("/api/v1/vendors/someid").exchange().expectBody(Vendor.class);
	}
	
	@Test
	public void testCreateVendor() {
		given(vendorRepository.saveAll(any(Publisher.class)))
				.willReturn(Flux.just(Vendor.builder().firstName("Bo").lastName("Feng").build()));

		Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build());

		webTestClient.post().uri("/api/v1/vendors").body(vendorToSaveMono, Vendor.class).exchange().expectStatus()
				.isCreated();
	}
	
	@Test
	public void TestUpdateVendor() {
		given(vendorRepository.save(any(Vendor.class)))
		.willReturn(Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build()));

		Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build());

		webTestClient.put().uri("api/v1/vendors/1").body(vendorToUpdateMono, Vendor.class).exchange().expectStatus()
		.isOk();
	}
	
	@Test
	public void TestPatchWithChanges() {
		given(vendorRepository.findById(anyString()))
		.willReturn(Mono.just(Vendor.builder().firstName("Bob").lastName("Feng").build()));
		
		given(vendorRepository.save(any(Vendor.class)))
				.willReturn(Mono.just(Vendor.builder().build()));

		Mono<Vendor> carToUpdateMono = Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build());

		webTestClient.patch().uri("api/v1/vendors/1").body(carToUpdateMono, Vendor.class).exchange().expectStatus()
				.isOk();
		
		verify(vendorRepository).save(any());
	}
	
	@Test
	public void TestPatchNoChanges() {
		given(vendorRepository.findById(anyString()))
		.willReturn(Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build()));
		
		given(vendorRepository.save(any(Vendor.class)))
				.willReturn(Mono.just(Vendor.builder().build()));

		Mono<Vendor> carToUpdateMono = Mono.just(Vendor.builder().firstName("Bo").lastName("Feng").build());

		webTestClient.patch().uri("api/v1/vendors/1").body(carToUpdateMono, Vendor.class).exchange().expectStatus()
				.isOk();
		
		verify(vendorRepository, never()).save(any());
	}

}
