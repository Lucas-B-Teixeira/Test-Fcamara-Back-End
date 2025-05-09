package fcamara.user_address_api.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ViaCepClient {
    public ViaCepResponse getAddressFromCep(String cep) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("https://viacep.com.br/ws/" + cep + "/json/", ViaCepResponse.class);
    }

    @Getter
    @Setter
    public static class ViaCepResponse {
        private String logradouro;
        private String bairro;
        private String localidade;
        private String uf;
    }
}
