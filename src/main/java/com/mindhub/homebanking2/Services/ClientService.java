package com.mindhub.homebanking2.Services;
import com.mindhub.homebanking2.Models.Client;
import java.util.List;

public interface ClientService {
    List<Client> getAllClients();
    Client getClientById(Long id);
	Client findClientByEmail(String email);
	void saveClient(Client client);
}
