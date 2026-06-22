package BookPoint.sucursal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SucursalApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainClassExist(){
		assertNotNull(SucursalApplication.class);
	}

}
