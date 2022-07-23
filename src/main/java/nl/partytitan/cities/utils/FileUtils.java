package nl.partytitan.cities.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class FileUtils {

    private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static final Lock readLock = readWriteLock.readLock();
    private static final Lock writeLock = readWriteLock.writeLock();

    private static Class<?> fileUtilsClass = FileUtils.class;

    /**
     * Checks a folderPath to see if it exists, if it doesn't it will attempt
     * to create the folder at the designated path.
     *
     * @param folder {@link String} containing a path to a folder.
     * @return True if the folder exists or if it was successfully created.
     */
    public static boolean checkOrCreateFolder(File folder) {

        if (folder.exists() || folder.isDirectory()) {
            return true;
        }

        return newDir(folder);
    }


    /**
     * Checks a filePath to see if it exists, if it doesn't it will attempt
     * to create the file at the designated path.
     *
     * @param file {@link String} containing a path to a file.
     * @return True if the folder exists or if it was successfully created.
     */
    public static boolean checkOrCreateFile(File file) {
        if (!checkOrCreateFolder(file.getParentFile())) {
            return false;
        }

        if (file.exists()) {
            return true;
        }

        return newFile(file);
    }

    private static boolean newDir(File dir) {
        try {
            writeLock.lock();
            return dir.mkdirs();
        } finally {
            writeLock.unlock();
        }
    }

    private static boolean newFile(File file) {
        try {
            writeLock.lock();
            return file.createNewFile();
        } catch (IOException e) {
            return false;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Writes the contents of a string to a file.
     *
     * @param source String to write.
     * @param file   File to write to.
     */
    public static void stringToFile(String source, File file) {
        try {
            writeLock.lock();
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
                 BufferedWriter bufferedWriter = new BufferedWriter(osw)) {

                bufferedWriter.write(source);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Pass a file and it will return it's contents as a string.
     *
     * @param file File to read.
     *
     * @return Contents of file. String will be empty in case of any errors.
     */
    public static String convertFileToString(File file) {
        try {
            readLock.lock();
            if (file != null && file.exists() && file.canRead() && !file.isDirectory()) {
                Writer writer = new StringWriter();

                char[] buffer = new char[1024];
                try (InputStream is = new FileInputStream(file)) {
                    Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return writer.toString();
            } else {
                return "";
            }
        } finally {
            readLock.unlock();
        }
    }
    /**
     * Pass the resource name and it will copy the file to the desination.
     *
     * @param destination Destination file.
     * @param resourceName The name of the resource to be copied
     */
    public static void copyResourceToFile(File destination, String resourceName){
        InputStream istr = null;
        OutputStream ostr = null;
        try {
            istr = fileUtilsClass.getResourceAsStream(resourceName);
            if (istr == null) {
                return;
            }
            ostr = new FileOutputStream(destination);
            final byte[] buffer = new byte[1024];
            int length = 0;
            length = istr.read(buffer);
            while (length > 0) {
                ostr.write(buffer, 0, length);
                length = istr.read(buffer);
            }
        } catch (final IOException ex) {
        } finally {
            try {
                if (istr != null) {
                    istr.close();
                }
            } catch (final IOException ex) {
            }
            try {
                if (ostr != null) {
                    ostr.close();
                }
            } catch (final IOException ex) {
            }
        }
    }
}
