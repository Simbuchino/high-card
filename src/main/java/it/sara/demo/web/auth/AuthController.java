package it.sara.demo.web.auth;

import it.sara.demo.dto.StatusDTO;
import it.sara.demo.exception.GenericException;
import it.sara.demo.service.auth.AuthService;
import it.sara.demo.web.auth.request.LoginRequest;
import it.sara.demo.web.auth.request.SetPasswordRequest;
import it.sara.demo.web.auth.response.LoginResponse;
import it.sara.demo.web.auth.response.SetPasswordResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint per impostare la nuova password di un utente dopo addUser da parte di un admin.
     *
     * @param request oggetto contenente guid, token di reset e nuova password
     * @return StatusDTO con esito dell'operazione
     */
    @PostMapping("/set-password")
    public ResponseEntity<SetPasswordResponse> setPassword(@RequestBody @Valid SetPasswordRequest request) throws GenericException {
        authService.setPassword(request.getGuid(), request.getToken(), request.getNewPassword());
        SetPasswordResponse setPasswordResponse = new SetPasswordResponse();
        setPasswordResponse.setStatus(StatusDTO.success("Password set successfully"));
        return ResponseEntity.ok(setPasswordResponse);
    }
    /**
     * Endpoint per effettuare il login di un utente.
     *
     * @param request oggetto contenente email e password
     * @return LoginResponse contenente il token JWT e lo stato della richiesta
     * @throws GenericException se le credenziali sono errate o l'utente deve resettare la password
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody @Valid LoginRequest request) throws GenericException {
        return ResponseEntity.ok(authService.login(request.getEmail(), request.getPassword()));
    }

}
