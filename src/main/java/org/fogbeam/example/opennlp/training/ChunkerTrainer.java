package org.fogbeam.example.opennlp.training;

import java.io.*;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import opennlp.tools.chunker.*;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @file ChunkerTrainer.java
 * @brief Clase para entrenar un modelo de fragmentación gramatical (chunker) utilizando OpenNLP.
 *
 * Este programa utiliza datos de entrenamiento en formato CoNLL2000 para generar un modelo de
 * fragmentación, que puede ser usado posteriormente para identificar estructuras gramaticales.
 */
public class ChunkerTrainer {
	// Logger para el registro de mensajes
	private static final Logger LOGGER = Logger.getLogger(ChunkerTrainer.class.getName());

	/**
	 * @brief Metodo principal para entrenar un modelo de fragmentación.
	 *
	 * Este metodo lee datos de entrenamiento en formato CoNLL2000, entrena un modelo de fragmentación
	 * utilizando OpenNLP y guarda el modelo generado en un archivo para su posterior uso.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados).
	 * @throws Exception En caso de errores durante la ejecución.
	 */
	public static void main(String[] args) throws Exception {
		Charset charset = Charset.forName("UTF-8"); /**< Codificación de los datos de entrenamiento. */

		// Flujo de texto línea por línea a partir de los datos de entrenamiento.
		ObjectStream<String> lineStream = null;
		ObjectStream<ChunkSample> sampleStream = null;
		ChunkerModel model; /**< Modelo de fragmentación generado. */

		try {
			lineStream = new PlainTextByLineStream(
					new FileInputStream("training_data/conll2000-chunker.train"), charset);

			// Convierte las líneas en objetos ChunkSample para el entrenamiento.
			sampleStream = new ChunkSampleStream(lineStream);

			// Entrena el modelo utilizando los datos de entrada y un generador de contexto predeterminado.
			model = ChunkerME.train(
					"en",                                // Idioma del modelo.
					sampleStream,                        // Flujo de datos de entrenamiento.
					new DefaultChunkerContextGenerator(), // Generador de contexto para los fragmentos.
					TrainingParameters.defaultParams()    // Parámetros de entrenamiento por defecto.
			);

			// Guardar el modelo entrenado.
			saveModel(model, "models/en-chunker.model");

			// Indica que el entrenamiento ha finalizado.
			LOGGER.info(String.format("Entrenamiento completado. Modelo guardado en: %s", "models/en-chunker.model"));
		} finally {
			// Cierra los recursos solo si fueron inicializados.
			if (sampleStream != null) {
				sampleStream.close();
			}
			if (lineStream != null) {
				lineStream.close();
			}
		}
	}

	/**
	 * Guarda el modelo entrenado en un archivo.
	 *
	 * @param model Modelo de fragmentación a guardar.
	 * @param modelFile Ruta del archivo donde se guardará el modelo.
	 * @throws IOException En caso de errores durante la escritura.
	 */
	private static void saveModel(ChunkerModel model, String modelFile) throws IOException {
		OutputStream modelOut = null;

		try {
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
		} finally {
			// Cierra el flujo de salida solo si fue inicializado.
			if (modelOut != null) {
				modelOut.close();
			}
		}
	}
}
