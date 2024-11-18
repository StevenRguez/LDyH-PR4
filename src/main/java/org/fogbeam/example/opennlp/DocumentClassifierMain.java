package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file DocumentClassifierMain.java
 * @brief Programa principal para clasificar texto en categorías usando un modelo preentrenado de OpenNLP.
 *
 * Este programa carga un modelo de categorización de documentos, clasifica un texto de ejemplo
 * en una categoría específica y muestra el resultado.
 */
public class DocumentClassifierMain
{
	// Logger para el registro de mensajes
	private static final Logger LOGGER = Logger.getLogger(DocumentClassifierMain.class.getName());

	/**
	 * @brief Metodo principal del programa.
	 *
	 * Este metodo carga un modelo de categorización de documentos, clasifica un texto de entrada
	 * y determina la categoría más adecuada.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados en este programa).
	 * @throws Exception En caso de errores durante la ejecución.
	 */
	public static void main(String[] args) throws Exception
	{
		InputStream is = null; /**< Flujo de entrada para cargar el modelo de categorización. */
		try
		{
			// Carga el modelo preentrenado de categorización de documentos desde un archivo.
			is = new FileInputStream("models/en-doccat.model");
			DoccatModel m = new DoccatModel(is);

			// Texto de entrada para clasificar.
			String inputText = "What happens if we have declining bottom-line revenue?";

			// Inicializa el clasificador de documentos con el modelo cargado.
			DocumentCategorizerME myCategorizer = new DocumentCategorizerME(m);

			// Clasifica el texto de entrada y obtiene las probabilidades de cada categoría.
			double[] outcomes = myCategorizer.categorize(inputText);

			// Determina la categoría con mayor probabilidad.
			String category = myCategorizer.getBestCategory(outcomes);

			// Reemplaza el uso de System.out con el logger.
			LOGGER.log(Level.INFO, "Input classified as: {0}", category);
		}
		catch (Exception e)
		{
			// En desarrollo: registrar detalles del error para depuración
			LOGGER.log(Level.SEVERE, "Error loading the model: {0}", e.getMessage());
		}
		finally
		{
			// Cierra el flujo de entrada del modelo si está abierto.
			if (is != null)
			{
				is.close();
			}
		}
		// Indica que el programa ha finalizado.
		LOGGER.log(Level.INFO, "done");
	}
}

