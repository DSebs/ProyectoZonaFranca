import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) 
import com.tayronadev.dominio.usuario.servicios.ValidadorUsuario;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ValidadoUsuarioTest - Servicio de Validación de Usuario")
public class ValidadoUsuarioTest {
    @Mock
    private ValidadorUsuario validadorUsuario;

    @BeforeEach
    void setUp() {
        validadorUsuario = new ValidadorUsuario();
    }
}

@Nested
@DisplayName("Valida un correo electrónico válido")
class ValidaCorreoElectronicoValido {
    @Test
    @DisplayName("Valida un correo electrónico válido")
    void validaCorreoElectronicoValido() {
        // Arrange
        String correo = "erickdiazsaavedra@gmail.com";
        
        // Act
        boolean resultado = validadorUsuario.validaCorreoElectronico(correo);
        
        // Assert
        assertTrue(resultado);
    }
}