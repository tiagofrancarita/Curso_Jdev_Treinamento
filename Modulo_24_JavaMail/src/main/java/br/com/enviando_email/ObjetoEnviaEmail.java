package br.com.enviando_email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ObjetoEnviaEmail {

	private String email = "jdevteste@gmail.com";
	private String senha = "P}-=h07S4Vp!";
	
	private String listaDestinatarios = "";
	private String nomeRemetente ="";
	private String assuntoEmail  ="";
	private String msgEmail ="";
	
	public ObjetoEnviaEmail(String listaDestinatario, String nomeRemetente, String assuntoEmail, String msgEmail) {
		
		this.listaDestinatarios = listaDestinatario;
		this.nomeRemetente = nomeRemetente;
		this.assuntoEmail = assuntoEmail;
		this.msgEmail = msgEmail;
		
		
	}

	public void enviar(boolean envioHtml)throws Exception {
		/*
		 * Configurações SMTP do e-mail:
		 * 
		 * Descrição SMTP Gmail: um nome generico (ex. “SMTP”) Nome do servidor SMTP
		 * Gmail: smtp.gmail.com Usuario SMTP Gmail: o seu endereço Gmail Password SMTP
		 * Gmail: a sua password Porta SMTP Gmail: 465
		 */

			Properties properties = new Properties();

				properties.put("mail.smtp.ssl.trust", "*");/* Autorização */
				properties.put("mail.smtp.auth", "true");/* Autorização */
				properties.put("mail.smtp.starttls", "true");/* Autenticação */
				properties.put("mail.smtp.host", "smtp.gmail.com");/* Servidor Gmail "Google" */
				properties.put("mail.smtp.port", "465");/* Porta do servidor */
				properties.put("mail.smtp.socketFactory.port", "465");/* Especificação da porta a ser conectada pelo socket */
				properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");/*Classe socket de conexão ao SMTP*/
				

				Session session = Session.getInstance(properties, new Authenticator() {

					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(email, senha);
					}});

			Address[] toUser = InternetAddress.parse(listaDestinatarios);/* Lista destino */
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email, nomeRemetente));/* Remetente e-mail e nome */
			message.setRecipients(Message.RecipientType.TO, toUser);/* e-mail destino */
			message.setSubject(assuntoEmail);/*Assunto e-mail*/
			
			if (envioHtml) {
				message.setContent(msgEmail, "text/html; charset=utf-8"); /*Personalizando envio de e-mail com html.*/
			}else {
				message.setText(msgEmail);/* Corpo do e-mail */
			}
			
			/*
			 * Caso o e-mail não esteja sendo enviado, então colocar um tempo de espera.
			 * OBS: Isso só pode ser usado para versão em teste.
			 *
			 * Thread.sleep(10000);
			 */
			
			Transport.send(message);
	}
	
	public void enviarEmailAnexo(boolean envioHtml)throws Exception {
		/*
		 * Configurações SMTP do e-mail:
		 * 
		 * Descrição SMTP Gmail: um nome generico (ex. “SMTP”) Nome do servidor SMTP
		 * Gmail: smtp.gmail.com Usuario SMTP Gmail: o seu endereço Gmail Password SMTP
		 * Gmail: a sua password Porta SMTP Gmail: 465
		 */

			Properties properties = new Properties();

				properties.put("mail.smtp.ssl.trust", "*");/* Autorização */
				properties.put("mail.smtp.auth", "true");/* Autorização */
				properties.put("mail.smtp.starttls", "true");/* Autenticação */
				properties.put("mail.smtp.host", "smtp.gmail.com");/* Servidor Gmail "Google" */
				properties.put("mail.smtp.port", "465");/* Porta do servidor */
				properties.put("mail.smtp.socketFactory.port", "465");/* Especificação da porta a ser conectada pelo socket */
				properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");/*Classe socket de conexão ao SMTP*/
				

				Session session = Session.getInstance(properties, new Authenticator() {

					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(email, senha);
					}});

			Address[] toUser = InternetAddress.parse(listaDestinatarios);/* Lista destino */
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email, nomeRemetente));/* Remetente e-mail e nome */
			message.setRecipients(Message.RecipientType.TO, toUser);/* e-mail destino */
			message.setSubject(assuntoEmail);/*Assunto e-mail*/
			
			/*Parte 1 do e-mail que é o texto e a descrição do e-mail*/
			
			MimeBodyPart corpoEmail = new MimeBodyPart();
			
			if (envioHtml) {
				corpoEmail.setContent(msgEmail, "text/html; charset=utf-8"); /*Personalizando envio de e-mail com html.*/
			}else {
				corpoEmail.setText(msgEmail);/* Corpo do e-mail */
			}
			
			List<FileInputStream> arquivos = new ArrayList<FileInputStream>();
				arquivos.add(simulaPDF());
				arquivos.add(simulaPDF());
				arquivos.add(simulaPDF());
				arquivos.add(simulaPDF());
				
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(corpoEmail);
				
				
				int index = 1;
				for (FileInputStream fileInputStream : arquivos) {
				
					/*Parte 2 do e-mail que são os anexos em pdf*/
					MimeBodyPart anexoEmail = new MimeBodyPart();
					anexoEmail.setDataHandler(new DataHandler(new ByteArrayDataSource(fileInputStream, "application/pdf")));
					anexoEmail.setFileName("anexo"+index+".pdf");
					multipart.addBodyPart(anexoEmail);
					index++;
				}
				
				
			message.setContent(multipart);
			Transport.send(message);
			
			/*
			 * Caso o e-mail não esteja sendo enviado, então colocar um tempo de espera.
			 * OBS: Isso só pode ser usado para versão em teste.
			 *
			 * Thread.sleep(10000);
			 */
	}
	
	/**
	 * O método abaixo simula um arquivo pdf ou qualquer arquivo que possa ser enviado
	 * por anexo no e-mail. Pode estar no BD ou em uma pasta localmente.
	 * Retorna um arquivo .PDF em branco com o texto do paragrafo.
	 **/
	
	private FileInputStream simulaPDF() throws Exception{
		
		Document pdf = new Document();
		File file = new File("fileanexo.pdf");
		file.createNewFile();
		PdfWriter.getInstance(pdf, new FileOutputStream(file));
		pdf.open();
		pdf.add(new Paragraph("Conteudo do pdf anexo"));
		pdf.close();
		
		return new FileInputStream(file);
	}
}
