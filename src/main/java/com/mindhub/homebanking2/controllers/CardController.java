package com.mindhub.homebanking2.controllers;

import com.mindhub.homebanking2.Models.Card;
import com.mindhub.homebanking2.Models.CardColor;
import com.mindhub.homebanking2.Models.CardType;
import com.mindhub.homebanking2.Models.Client;
import com.mindhub.homebanking2.Services.CardService;
import com.mindhub.homebanking2.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController //restricciones de rest.
public class CardController {

	@Autowired // generamos una instancia del repositorio (el concepto se llama: inyeccion de dependencia).
	private ClientService clientService;
	@Autowired
	private CardService cardService;

	//servlet: son clases Java diseñadas para responder a solicitudes HTTP.
	@PostMapping("/api/clients/current/cards") //asociamos la ruta a la peticion de tipo post.
	public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){
	//@RequestParam para extraer parámetros de consulta.

		Client client = clientService.findClientByEmail(authentication.getName());

		int cardCvv = getRandomNumber(100,999);

		String cardNumber = getRandomNumber(1000,9999) + "-" + getRandomNumber(1000,9999) + "-" + getRandomNumber(1000,9999) + "-" + getRandomNumber(1000,9999);

		if(cardType == null || cardColor == null) {
			return new ResponseEntity<>("Does not select card color or type", HttpStatus.FORBIDDEN);
		}
		if(client.getCards().stream().filter(card -> card.getType().equals(cardType)).count() >= 3){
			return new ResponseEntity<>("You already own three cards per type", HttpStatus.FORBIDDEN);
		}

		if(client.getCards().stream().anyMatch(card -> card.getColor().equals(cardColor) && card.getType().equals(cardType))){
			return new ResponseEntity<>("for each card you can only select one color", HttpStatus.FORBIDDEN);
		}

		cardService.saveCard(new Card(client.toString(), cardColor, cardType, cardNumber, cardCvv, LocalDate.now().plusYears(5), LocalDate.now(), client));

		return new ResponseEntity<>("Your card was created successfully",HttpStatus.CREATED);
	}

	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
}
