/**
 * 
 */
package guru.springframework.spring5webfluxrest.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

/**
 * @author bobbyfeng
 *
 */
@Data
@Document
@Builder
public class Vendor {

	@Id
	private String id;
	
	private String firstName;
	
	private String lastName;
}
