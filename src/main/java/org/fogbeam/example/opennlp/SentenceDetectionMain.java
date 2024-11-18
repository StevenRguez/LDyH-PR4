package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 * @file SentenceDetectionMain.java
 * @brief Programa principal para la detección de oraciones utilizando OpenNLP.
 *
 * Este programa carga un modelo preentrenado de detección de oraciones, procesa un texto
 * de entrada y divide el contenido en oraciones individuales.
 */
public class SentenceDetectionMain {

	// Logger para el registro de mensajes
	private static final Logger LOGGER = Logger.getLogger(SentenceDetectionMain.class.getName());

	/**
	 * @brief Metodo principal del programa.
	 *
	 * Este metodo carga un modelo de detección de oraciones, procesa un texto de entrada
	 * desde un archivo, detecta oraciones individuales en el texto y las muestra en la salida.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados en este programa).
	 * @throws Exception En caso de errores durante la ejecución.
	 */
	public static void main(String[] args) throws Exception {
		InputStream modelIn = null;     /**< Flujo de entrada para cargar el modelo de detección de oraciones. */
		InputStream demoDataIn = null; /**< Flujo de entrada para leer los datos de demostración. */

		try {
			// Carga el modelo preentrenado de detección de oraciones desde un archivo.
			modelIn = new FileInputStream("models/en-sent.model");
			SentenceModel model = new SentenceModel(modelIn);

			// Inicializa el detector de oraciones con el modelo cargado.
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

			// Lee el texto de entrada para la demostración desde un archivo.
			demoDataIn = new FileInputStream("demo_data/en-sent1.demo");
			String demoData = convertStreamToString(demoDataIn);

			// Detecta oraciones en el texto de entrada.
			String[] sentences = sentenceDetector.sentDetect(demoData);

			// Registra cada oración detectada.
			for (String sentence : sentences) {
				LOGGER.info(sentence);
			}

		} catch (IOException e) {
			// En desarrollo: registrar detalles del error para depuración
			LOGGER.log(Level.SEVERE, "Error loading the model or reading input data: {0}", e.getMessage());
		} finally {
			// Cierra los flujos de entrada si están abiertos.
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Error closing the model stream: {0}", e.getMessage());
				}
			}

			if (demoDataIn != null) {
				try {
					demoDataIn.close();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Error closing the demo data stream: {0}", e.getMessage());
				}
			}
		}

		// Indica que el programa ha finalizado.
		LOGGER.info("Program completed successfully.");
	}

	/**
	 * @brief Convierte un flujo de entrada (InputStream) en una cadena (String).
	 *
	 * Este metodo utiliza un `Scanner` para leer el contenido completo de un flujo
	 * de entrada y devolverlo como una cadena.
	 *
	 * @param is Flujo de entrada a convertir.
	 * @return El contenido del flujo de entrada como una cadena.
	 */
	static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}

