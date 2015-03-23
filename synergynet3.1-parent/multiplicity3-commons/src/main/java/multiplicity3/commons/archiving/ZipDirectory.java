package multiplicity3.commons.archiving;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * The Class ZipDirectory.
 */
public class ZipDirectory {

	/**
	 * Unzip directory.
	 *
	 * @param file the file
	 * @param destinationDirectory the destination directory
	 * @return true, if successful
	 * @throws ZipException the zip exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean unzipDirectory(File file, File destinationDirectory)
			throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
		while (zipEntries.hasMoreElements()) {
			ZipEntry entry = zipEntries.nextElement();
			unzipZipEntry(zipFile, destinationDirectory, entry);
		}
		return true;
	}

	/**
	 * Zip directory.
	 *
	 * @param directory the directory
	 * @param zipFileDestination the zip file destination
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean zipDirectory(File directory, File zipFileDestination)
			throws IOException {
		if (!directory.exists()) {
			return false;
		}

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
				zipFileDestination));
		String path = "";
		zipDir(directory, zos, path);
		zos.close();
		return true;
	}

	// private methods

	/**
	 * Adds the path component.
	 *
	 * @param path the path
	 * @param component the component
	 * @return the string
	 */
	private static String addPathComponent(String path, String component) {
		return path + "/" + component;
	}

	/**
	 * Ensure directory exists for file.
	 *
	 * @param fileToWrite the file to write
	 */
	private static void ensureDirectoryExistsForFile(File fileToWrite) {
		File parentDirectory = fileToWrite.getParentFile();
		if (!parentDirectory.exists()) {
			parentDirectory.mkdirs();
		}
	}

	/**
	 * Checks if is directory entry.
	 *
	 * @param entry the entry
	 * @return true, if is directory entry
	 */
	private static boolean isDirectoryEntry(ZipEntry entry) {
		return entry.getName().endsWith("/");
	}

	/**
	 * Make entry name safe.
	 *
	 * @param unsafeEntryName the unsafe entry name
	 * @return the string
	 */
	private static String makeEntryNameSafe(String unsafeEntryName) {
		if (unsafeEntryName.startsWith("/")) {
			return unsafeEntryName.substring(1);
		}
		String safeEntryName = unsafeEntryName;
		return safeEntryName;
	}

	/**
	 * Unzip zip entry.
	 *
	 * @param zipFile the zip file
	 * @param destinationDirectory the destination directory
	 * @param entry the entry
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void unzipZipEntry(ZipFile zipFile,
			File destinationDirectory, ZipEntry entry) throws IOException {
		if (isDirectoryEntry(entry)) {
			return;
		}

		String safeEntryName = makeEntryNameSafe(entry.getName());
		File fileToWrite = new File(destinationDirectory, safeEntryName);
		ensureDirectoryExistsForFile(fileToWrite);
		writeFileForEntry(zipFile, entry, fileToWrite);
	}

	/**
	 * Write file for entry.
	 *
	 * @param zipFile the zip file
	 * @param entry the entry
	 * @param destinationFile the destination file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void writeFileForEntry(ZipFile zipFile, ZipEntry entry,
			File destinationFile) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(
				destinationFile);
		InputStream fileDataInputStream = zipFile.getInputStream(entry);
		int bytesRead = 0;
		byte[] buffer = new byte[4 * 1024];
		while ((bytesRead = fileDataInputStream.read(buffer)) > 0) {
			fileOutputStream.write(buffer, 0, bytesRead);
		}
		fileDataInputStream.close();
		fileOutputStream.close();
	}

	/**
	 * Zip dir.
	 *
	 * @param directoryToZip the directory to zip
	 * @param zipOutputStream the zip output stream
	 * @param path the path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void zipDir(File directoryToZip,
			ZipOutputStream zipOutputStream, String path) throws IOException {
		String[] directoryContentsList = directoryToZip.list();

		for (String currentFileName : directoryContentsList) {
			File currentFile = new File(directoryToZip, currentFileName);
			String currentFileZipPath = addPathComponent(path,
					currentFile.getName());

			if (currentFile.isDirectory()) {
				zipDir(currentFile, zipOutputStream, currentFileZipPath);
			} else {
				ZipEntry zipEntry = new ZipEntry(currentFileZipPath);
				zipFile(currentFile, zipOutputStream, zipEntry);
			}
		}
	}

	/**
	 * Zip file.
	 *
	 * @param fileToAdd the file to add
	 * @param zipOutputStream the zip output stream
	 * @param zipEntry the zip entry
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void zipFile(File fileToAdd,
			ZipOutputStream zipOutputStream, ZipEntry zipEntry)
			throws IOException {
		FileInputStream fis = new FileInputStream(fileToAdd);
		try {
			byte[] readBuffer = new byte[4 * 1024];
			int bytesReadCount = 0;
			zipOutputStream.putNextEntry(zipEntry);
			bytesReadCount = fis.read(readBuffer);
			while (bytesReadCount != -1) {
				zipOutputStream.write(readBuffer, 0, bytesReadCount);
				bytesReadCount = fis.read(readBuffer);
			}
		} finally {
			fis.close();
		}
	}

}
