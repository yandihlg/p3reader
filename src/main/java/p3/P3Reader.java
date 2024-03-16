package p3;

import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class P3Reader {
	private static String removeText = "FLORIDA LOTTERY\r\n"
			+ "11-MAR-2024\r\n"
			+ "Please note every effort has been made to\r\n"
			+ "numbers in the official record of the Florid";

	public static void main(String[] args) {
		List<String> allElement = new ArrayList<String>();
		String rutaPDF = "C:\\Users\\yandi\\OneDrive\\Escritorio\\p3.pdf";

		Rectangle[] columnas = { new Rectangle(0, 0, 180, 600), new Rectangle(180, 180, 180, 600),
				new Rectangle(360, 180, 180, 600) }; //

		// Leer las columnas del PDF
		List<String> columnasTexto;
		try {
			columnasTexto = leerColumnasPDF(rutaPDF, columnas);
			if (columnas != null) {
				for (int i = 0; i < columnasTexto.size(); i++) {
					columnasTexto.set(i, columnasTexto.get(i).replace(removeText, ""));
					allElement.addAll(Arrays.asList(columnasTexto.get(i).split("\r\n")));
				}
			}
			writeTXT(allElement);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<String> leerColumnasPDF(String filePath, Rectangle[] columnas) throws IOException {
		List<String> columnasTexto = new ArrayList<String>();

		try (PDDocument document = PDDocument.load(new File(filePath))) {
			PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);

			for (int i = 0; i < columnas.length; i++) {
				Rectangle columna = columnas[i];
				stripper.addRegion(Integer.toString(i), columna);
			}

			for (int pagina = 0; pagina < document.getNumberOfPages(); pagina++) {
				stripper.extractRegions(document.getPage(pagina));

				for (int i = 0; i < columnas.length; i++) {
					columnasTexto.add(stripper.getTextForRegion(Integer.toString(i)));
				}
			}
		}

		return columnasTexto;
	}
	
	public static void writeTXT(List<String> lista) {
		 

	        // Nombre del archivo de texto
	        String nombreArchivo = "archivo.txt";

	        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))) {
	            // Escribir cada elemento de la lista en el archivo
	            for (String elemento : lista) {
	                escritor.write(elemento);
	                escritor.newLine(); // Agrega una nueva línea después de cada elemento
	            }
	            System.out.println("Elementos de la lista escritos en el archivo exitosamente.");
	        } catch (IOException e) {
	            System.out.println("Error al escribir en el archivo: " + e.getMessage());
	        }
	}

}
