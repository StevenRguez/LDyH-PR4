package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file ChunkerMain.java
 * @brief Programa principal para realizar el análisis de fragmentos gramaticales (chunks) en una oración.
 *
 * Este programa utiliza OpenNLP para cargar un modelo de fragmentación,
 * procesar una oración tokenizada con etiquetas POS y determinar las
 * estructuras gramaticales, como frases nominales y verbales.
 */
public class ChunkerMain {
	// Logger para el registro de mensajes
	private static final Logger LOGGER = Logger.getLogger(ChunkerMain.class.getName());

	/**
	 * @brief Metodo principal del programa.
	 *
	 * Este metodo carga un modelo de fragmentación, procesa una oración tokenizada y etiquetada
	 * gramaticalmente (POS tags) y genera etiquetas de fragmentos (chunk tags) para cada token.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados en este programa).
	 * @throws Exception En caso de que ocurra un error inesperado.
	 */
	public static void main( String[] args ) throws Exception {
		InputStream modelIn = null;  /**< Flujo de entrada para cargar el modelo de fragmentación. */
		ChunkerModel model = null;  /**< Modelo de fragmentación cargado desde el archivo. */

		try {
			// Carga el modelo preentrenado de fragmentación desde un archivo.
			modelIn = new FileInputStream("models/en-chunker.model");
			model = new ChunkerModel(modelIn);

			// Inicializa el motor de fragmentación con el modelo cargado.
			ChunkerME chunker = new ChunkerME(model);

			// Tokens de ejemplo de una oración (normalmente generados por un tokenizador).
			String[] sent = new String[]{
					"Rockwell", "International", "Corp.", "'s", "Tulsa", "unit",
					"said", "it", "signed", "a", "tentative", "agreement",
					"extending", "its", "contract", "with", "Boeing", "Co.", "to",
					"provide", "structural", "parts", "for", "Boeing", "'s", "747",
					"jetliners", "."
			};

			// Etiquetas gramaticales (POS tags) correspondientes a los tokens.
			String[] pos = new String[]{
					"NNP", "NNP", "NNP", "POS", "NNP", "NN",
					"VBD", "PRP", "VBD", "DT", "JJ", "NN",
					"VBG", "PRP$", "NN", "IN", "NNP", "NNP", "TO",
					"VB", "JJ", "NNS", "IN", "NNP", "POS", "CD", "NNS",
					"."
			};

			// Realiza el análisis de fragmentos.
			String[] tag = chunker.chunk(sent, pos);  /**< Etiquetas de fragmentos generadas para los tokens. */
			double[] probs = chunker.probs();        /**< Probabilidades asociadas a las etiquetas. */

			/**
			 * Los fragmentos generados contienen etiquetas que representan el tipo de estructura gramatical.
			 * Ejemplo:
			 * - B-NP: Inicio de una frase nominal.
			 * - I-NP: Continuación de una frase nominal.
			 * - I-VP: Continuación de una frase verbal.
			 */

			// Imprime los resultados del análisis para cada token.
			for (int i = 0; i < sent.length; i++) {
				LOGGER.log(Level.INFO, "Token [{0}] has chunk tag [{1}] with probability = {2}", new Object[]{sent[i], tag[i], probs[i]});
			}
		} catch( IOException e ) {
			// En desarrollo: registrar detalles del error para depuración
			LOGGER.log(Level.SEVERE, "Error loading the model: {0}", e.getMessage());
		} finally {
			// Cierra el flujo de entrada del modelo si está abierto.
			if (modelIn != null) {
				try
				{
					modelIn.close();
				}
				catch(IOException e) {
					LOGGER.log(Level.WARNING, "Error closing the model input stream: {0}", e.getMessage());
				}
			}
		}

		// Indica que el programa ha finalizado.
		LOGGER.log(Level.INFO, "done");
	}
}
