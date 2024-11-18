
package org.fogbeam.example.opennlp.training;

import java.io.*;
import java.nio.charset.Charset;
import java.util.logging.Level;
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
public class ChunkerTrainer
{
	// Logger para el registro de mensajes
	private static final Logger LOGGER = Logger.getLogger(ChunkerTrainer.class.getName());

	/**
	 * @brief Método principal para entrenar un modelo de fragmentación.
	 *
	 * Este método lee datos de entrenamiento en formato CoNLL2000, entrena un modelo de fragmentación
	 * utilizando OpenNLP y guarda el modelo generado en un archivo para su posterior uso.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados).
	 * @throws Exception En caso de errores durante la ejecución.
	 */
	public static void main(String[] args) throws Exception
	{
		Charset charset = Charset.forName("UTF-8"); /**< Codificación de los datos de entrenamiento. */

		// Flujo de texto línea por línea a partir de los datos de entrenamiento.
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new FileInputStream("training_data/conll2000-chunker.train"), charset);

		// Convierte las líneas en objetos ChunkSample para el entrenamiento.
		ObjectStream<ChunkSample> sampleStream = new ChunkSampleStream(lineStream);

		ChunkerModel model; /**< Modelo de fragmentación generado. */

		try
		{
			// Entrena el modelo utilizando los datos de entrada y un generador de contexto predeterminado.
			model = ChunkerME.train(
					"en",                                // Idioma del modelo.
					sampleStream,                        // Flujo de datos de entrenamiento.
					new DefaultChunkerContextGenerator(), // Generador de contexto para los fragmentos.
					TrainingParameters.defaultParams()    // Parámetros de entrenamiento por defecto.
			);
		}
		finally
		{
			// Cierra el flujo de datos de entrenamiento.
			sampleStream.close();
		}

		OutputStream modelOut = null; /**< Flujo de salida para guardar el modelo entrenado. */
		String modelFile = "models/en-chunker.model"; /**< Ruta del archivo donde se guardará el modelo. */

		try
		{
			// Guarda el modelo entrenado en un archivo.
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
		}
		finally
		{
			// Cierra el flujo de salida si está abierto.
			if (modelOut != null)
			{
				modelOut.close();
			}
		}

		// Indica que el entrenamiento ha finalizado.
		LOGGER.info("Entrenamiento completado. Modelo guardado en: " + modelFile);
	}
}
