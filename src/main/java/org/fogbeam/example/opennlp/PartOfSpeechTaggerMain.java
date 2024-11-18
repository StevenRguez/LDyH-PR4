package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 * @file PartOfSpeechTaggerMain.java
 * @brief Programa principal para el etiquetado gramatical (POS tagging) de oraciones utilizando OpenNLP.
 *
 * Este programa carga un modelo de etiquetado gramatical preentrenado, procesa una oración de ejemplo
 * para asignar etiquetas gramaticales (part-of-speech, POS) a cada palabra y muestra los resultados.
 */
public class PartOfSpeechTaggerMain {
	// Logger para el registro de mensajes
	private static final Logger LOGGER = Logger.getLogger(PartOfSpeechTaggerMain.class.getName());

	/**
	 * @brief Metodo principal del programa.
	 *
	 * Este metodo carga un modelo de etiquetado gramatical, procesa una oración tokenizada y
	 * genera etiquetas gramaticales (POS) para cada token, junto con las probabilidades asociadas.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados en este programa).
	 */
	public static void main(String[] args) {
		InputStream modelIn = null; /**< Flujo de entrada para cargar el modelo de etiquetado gramatical. */

		try {
			// Carga el modelo preentrenado de etiquetado gramatical desde un archivo.
			modelIn = new FileInputStream("models/en-pos-maxent.bin");

			// Inicializa el modelo de etiquetado gramatical.
			POSModel model = new POSModel(modelIn);

			// Inicializa el etiquetador gramatical con el modelo cargado.
			POSTaggerME tagger = new POSTaggerME(model);

			// Tokens de entrada que representan una oración tokenizada.
			String[] sent = new String[]{"Most", "large", "cities", "in", "the", "US", "had",
					"morning", "and", "afternoon", "newspapers", "."};

			// Genera etiquetas gramaticales para los tokens de entrada.
			String[] tags = tagger.tag(sent);

			// Obtiene las probabilidades asociadas a las etiquetas generadas.
			double[] probs = tagger.probs();

			/**
			 * Registra los resultados para cada token.
			 * Muestra:
			 * - El token original.
			 * - Su etiqueta gramatical (POS tag).
			 * - La probabilidad asociada a esa etiqueta.
			 */
			if (LOGGER.isLoggable(Level.INFO)) {
				for (int i = 0; i < sent.length; i++) {
					LOGGER.info(String.format("Token [%s] has POS [%s] with probability = %.4f",
							sent[i], tags[i], probs[i]));
				}
			}

		} catch (IOException e) {
			// En desarrollo: registrar detalles del error para depuración
			LOGGER.log(Level.SEVERE, "Error loading the model: {0}", e.getMessage());
		} finally {
			// Cierra el flujo de entrada del modelo si está abierto.
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Error closing the model stream: {0}", e.getMessage());
				}
			}
		}

		// Indica que el programa ha finalizado.
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("Done");
		}
	}
}
