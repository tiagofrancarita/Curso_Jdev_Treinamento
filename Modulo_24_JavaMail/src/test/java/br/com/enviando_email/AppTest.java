package br.com.enviando_email;



import org.junit.Test;

public class AppTest {
	
	
	
	@Test
	public void testeEmail() throws Exception {
		
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append("Testando HTML....<br/><br/>");
		strBuilder.append("<h1>E-mail com o formato HTML</h1> <br/><br/>");
		
		ObjetoEnviaEmail enviaEmail = new ObjetoEnviaEmail("tiagofranca.rita@gmail.com, tiago.rita@totvs.com.br",
															"França Desenvolvimento",
															"Testando e-mail com java", 
															strBuilder.toString());
		
		enviaEmail.enviarEmailAnexo(true);

		/*
		enviaEmail.enviar(true);
		enviaEmail.enviar(false);
		enviaEmail.enviarEmailAnexo(false);
	*============================================================
		/*
		 * 	Caso o e-mail não esteja sendo enviado, então
		 * colocar um tempo de espera. 
		 * OBS: Isso só pode ser usado para versão em teste.
		 *
		 *
		 * Thread.sleep(50000);
		 */
		Thread.sleep(5000);
		
	}
}