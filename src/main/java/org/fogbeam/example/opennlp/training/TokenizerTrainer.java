package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.fogbeam.example.opennlp.TokenizerMain;

/**
 * @file TokenizerTrainer.java
 * @brief Clase para entrenar un modelo de tokenización usando OpenNLP.
 *
 * Esta clase utiliza un archivo de datos de entrenamiento para generar un modelo
 * que puede dividir textos en tokens, tales como palabras o puntuaciones.
 */
public class TokenizerTrainer {
	// Logger para el registro de mensajes
	private static final Logger LOGGER = Logger.getLogger(TokenizerMain.class.getName());

	/**
	 * @brief Metodo principal para entrenar un modelo de tokenización.
	 *
	 * Este metodo carga datos de entrenamiento, entrena un modelo de tokenización
	 * y lo guarda en un archivo para su uso posterior.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados).
	 * @throws Exception Si ocurre un error durante la lectura, el entrenamiento o la escritura.
	 */
	public static void main(String[] args) throws Exception {
		Charset charset = Charset.forName("UTF-8"); /**< Codificación usada para leer los datos de entrenamiento. */

		// Carga las líneas del archivo de entrenamiento como un flujo de datos de texto.
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new FileInputStream("training_data/en-token.train"), charset);

		// Convierte las líneas en objetos TokenSample para el entrenamiento.
		ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream);

		TokenizerModel model; /**< Modelo de tokenización generado. */

		try {
			// Entrena el modelo usando las muestras de entrenamiento.
			model = TokenizerME.train(
					"en",                          // Idioma del modelo (inglés en este caso).
					sampleStream,                  // Flujo de muestras de entrenamiento.
					true,                          // Usa tokenización avanzada (opción habilitada).
					TrainingParameters.defaultParams() // Parámetros predeterminados de entrenamiento.
			);
		} finally {
			// Asegura que el flujo de muestras se cierre después del entrenamiento.
			sampleStream.close();
		}

		OutputStream modelOut = null; /**< Flujo de salida para guardar el modelo entrenado. */
		try {
			// Especifica el archivo donde se guardará el modelo.
			modelOut = new BufferedOutputStream(new FileOutputStream("models/en-token.model"));

			// Serializa el modelo entrenado y lo guarda en el archivo.
			model.serialize(modelOut);
		} finally {
			// Asegura que el flujo de salida se cierre correctamente.
			if (modelOut != null) {
				modelOut.close();
			}
		}

		// Indica que el entrenamiento ha finalizado correctamente.
		LOGGER.info("Entrenamiento completado correctamente.");
	}
}

