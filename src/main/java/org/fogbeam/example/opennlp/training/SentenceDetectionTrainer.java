package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @file SentenceDetectionTrainer.java
 * @brief Clase para entrenar un modelo de detección de oraciones usando OpenNLP.
 *
 * Este programa entrena un modelo de detección de oraciones a partir de un conjunto
 * de datos de entrenamiento. Los modelos generados permiten dividir textos en
 * oraciones individuales.
 */
public class SentenceDetectionTrainer {

	/**
	 * @brief Método principal para entrenar un modelo de detección de oraciones.
	 *
	 * Este método procesa un archivo de datos de entrenamiento, entrena un modelo
	 * de detección de oraciones y guarda el modelo resultante en un archivo para
	 * uso futuro.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados).
	 * @throws Exception Si ocurre un error durante el entrenamiento o la serialización.
	 */
	public static void main(String[] args) throws Exception {
		Charset charset = Charset.forName("UTF-8"); /**< Codificación utilizada para leer los datos de entrenamiento. */

		// Carga los datos de entrenamiento desde un archivo y los convierte en muestras para el entrenamiento.
		ObjectStream<String> lineStream =
				new PlainTextByLineStream(new FileInputStream("training_data/en-sent.train"), charset);
		ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);

		SentenceModel model; /**< Modelo de detección de oraciones generado. */

		try {
			// Entrena el modelo utilizando los datos de entrenamiento.
			model = SentenceDetectorME.train(
					"en",                         // Idioma del modelo (inglés en este caso).
					sampleStream,                 // Flujo de datos de entrenamiento.
					true,                         // Usa la funcionalidad de palabras múltiples (abstracción).
					null,                         // Diccionario adicional (nulo en este caso).
					TrainingParameters.defaultParams() // Parámetros de entrenamiento predeterminados.
			);
		} finally {
			// Cierra el flujo de datos de entrenamiento.
			sampleStream.close();
		}

		OutputStream modelOut = null; /**< Flujo de salida para guardar el modelo entrenado. */
		File modelFile = new File("models/en-sent.model"); /**< Archivo donde se almacenará el modelo. */

		try {
			// Guarda el modelo entrenado en el archivo especificado.
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut); // Serializa y guarda el modelo.
		} finally {
			// Cierra el flujo de salida si está abierto.
			if (modelOut != null)
				modelOut.close();
		}

		// Indica que el entrenamiento y guardado del modelo han finalizado con éxito.
		System.out.println("done");
	}
}

