
package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file NameFinderMain.java
 * @brief Programa principal para la detección de nombres propios en texto utilizando OpenNLP.
 *
 * Este programa carga un modelo de detección de entidades nombradas, procesa un conjunto
 * de tokens para identificar nombres propios y muestra los resultados.
 */
public class NameFinderMain
{

	// Logger para el registro de mensajes
	private static final Logger LOGGER = Logger.getLogger(NameFinderMain.class.getName());

	/**
	 * @brief Metodo principal del programa.
	 *
	 * Este metodo carga un modelo de detección de entidades nombradas (NER, por sus siglas en inglés),
	 * identifica nombres propios en un conjunto de tokens y muestra las entidades detectadas.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados en este programa).
	 * @throws Exception En caso de errores durante la ejecución.
	 */
	public static void main(String[] args) throws Exception
	{
		InputStream modelIn = null; /**< Flujo de entrada para cargar el modelo NER. */

		try
		{
			// Carga el modelo preentrenado de detección de entidades nombradas desde un archivo.
			modelIn = new FileInputStream("models/en-ner-person.model");
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);

			// Inicializa el motor de detección de nombres con el modelo cargado.
			NameFinderME nameFinder = new NameFinderME(model);

			// Tokens de entrada que representan una oración tokenizada.
			String[] tokens = {
					"Phillip",
					"Rhodes",
					"is",
					"presenting",
					"at",
					"some",
					"meeting",
					"."
			};

			// Realiza la detección de nombres propios en los tokens de entrada.
			Span[] names = nameFinder.find(tokens);

			/**
			 * Itera sobre las entidades detectadas (nombres propios).
			 * Cada objeto Span contiene:
			 * - Índice de inicio y fin de la entidad en el array de tokens.
			 * - Tipo de entidad (por ejemplo, "person").
			 */
			for (Span ns : names)
			{
				System.out.println("ns: " + ns.toString());

				// Ejemplo para extraer y mostrar el texto correspondiente a la entidad.
				StringBuilder sb = new StringBuilder();
				for (int i = ns.getStart(); i < ns.getEnd(); i++)
				{
					sb.append(tokens[i]).append(" ");
				}
				System.out.println("Detected name: " + sb.toString().trim());
			}

			// Limpia los datos adaptativos del modelo.
			nameFinder.clearAdaptiveData();

		}
		catch (IOException e)
		{
			// En desarrollo: registrar detalles del error para depuración
			LOGGER.log(Level.SEVERE, "Error loading the model: {0}", e.getMessage());

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
				}
			}
		}

		// Indica que el programa ha finalizado.
		System.out.println("done");
	}
}