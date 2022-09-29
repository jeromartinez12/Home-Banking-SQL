package com.mindhub.homebanking2.dtos;
import com.mindhub.homebanking2.Models.Loan;

//DTO: es la forma en la que se transfieren los datos del objeto.
public class LoanApplicationDTO {
		private long id;
		private Double amount;
		private Integer payments;
		private String destinyAccount;

		public LoanApplicationDTO() {
		}

		public LoanApplicationDTO(Loan loan, Double amount, Integer payments, String destinyAccount) {
			this.id = loan.getId();
			this.amount = amount;
			this.payments = payments;
			this.destinyAccount = destinyAccount;
		}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setPayments(Integer payments) {
		this.payments = payments;
	}

	public void setDestinyAccount(String destinyAccount) {
		this.destinyAccount = destinyAccount;
	}

	public long getId() {
			return id;
		}

		public Double getAmount() {
			return amount;
		}

		public Integer getPayments() {
			return payments;
		}

		public String getDestinyAccount() {
			return destinyAccount;
		}

	}