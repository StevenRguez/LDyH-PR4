package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * @file TokenizerMain.java
 * @brief Programa principal para la tokenización de texto utilizando OpenNLP.
 *
 * Este programa carga un modelo preentrenado o personalizado de tokenización y divide un texto
 * de entrada en tokens individuales (como palabras o signos de puntuación).
 */
public class TokenizerMain
{
	/**
	 * @brief Metodo principal del programa.
	 *
	 * Este metodo carga un modelo de tokenización, procesa un texto de entrada
	 * y divide el texto en tokens individuales, que se muestran en la salida.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados en este programa).
	 * @throws Exception En caso de errores durante la ejecución.
	 */
	public static void main(String[] args) throws Exception
	{
		InputStream modelIn = null; /**< Flujo de entrada para cargar el modelo de tokenización. */

		try
		{
			// Carga el modelo preentrenado o personalizado de tokenización desde un archivo.
			modelIn = new FileInputStream("models/en-token.model");
			TokenizerModel model = new TokenizerModel(modelIn);

			// Inicializa el tokenizador con el modelo cargado.
			Tokenizer tokenizer = new TokenizerME(model);

			/**
			 * Texto de ejemplo para la tokenización.
			 * Observa cómo se manejan diferentes casos dependiendo del modelo utilizado.
			 */
			String[] tokens = tokenizer.tokenize(
					"A ranger journeying with Oglethorpe, founder of the Georgia Colony, " +
							"mentions \"three Mounts raised by the Indians over three of their Great Kings " +
							"who were killed in the Wars.\""
			);

			// Imprime cada token generado.
			for (String token : tokens)
			{
				System.out.println(token);
			}

		}
		catch (IOException e)
		{
			// Maneja errores durante la carga del modelo.
			e.printStackTrace();
		}
		finally
		{
			// Cierra el flujo de entrada del modelo si está abierto.
			if (modelIn != null)
			{
				try
				{
					modelIn.close();
				}
				catch (IOException e)
				{
					// Maneja errores al cerrar el flujo de entrada.
				}
			}
		}

		// Indica que el programa ha finalizado.
		System.out.println("\n-----\ndone");
	}
}
