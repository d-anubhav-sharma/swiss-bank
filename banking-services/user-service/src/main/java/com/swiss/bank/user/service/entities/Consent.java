package com.swiss.bank.user.service.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Consent {

	@Id
	private String id;
	@Nonnull
	@Indexed(unique = true)
	private String username;
	private boolean canSendBankingOffers;
	private boolean canSendOtherServiceOffers;
	private boolean canUseDataForTrainingPurpose;
	private boolean canShareDataWithThirdParty;
	private boolean canSendPromotionalAdds;
	private boolean canKeepDataForMoreThan180Days;
	
}
