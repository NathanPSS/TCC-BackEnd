package com.hujb.app.usuarios.repositories;


import static org.assertj.core.api.Assertions.assertThat;
import com.hujb.app.config.ContainersEnviroment;
import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.estagiarios.repositories.UsuariosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuariosRepositoryTest extends ContainersEnviroment {


    @Autowired
    private UsuariosRepository usuariosRepository;

    @Test
    void test_find_by_id(){
        //Given
        Usuario usuario = new Usuario(1L,"Irineu");
        usuariosRepository.save(usuario);

        //When
        Usuario found = usuariosRepository.findById(1L).orElse(null);

        //Then
        assertThat(found).isNot(null);

}
}
