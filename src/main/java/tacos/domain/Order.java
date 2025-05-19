package tacos.domain;

import lombok.Data;

@Data
public class Order {
	private String deliveryName;
	private String deliveryStreet;
	private String deliveryCity;
	private String deliveryZip;
	private String ccNumber;	// credit card
	private String ccExpiration;
	private String ccCvv;
}
