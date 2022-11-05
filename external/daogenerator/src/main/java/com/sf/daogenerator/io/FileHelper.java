package com.sf.daogenerator.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FileHelper {

  private static final int DEFAULT_BUFFER_SIZE = 8192;

  private HashMap<String, String> importMap = new HashMap<>();
  private File srcDir;
  private File destDir;

  public FileHelper(File srcDir, File destDir) {
    importMap.clear();
    this.srcDir = srcDir;
    this.destDir = destDir;
  }

  public void addImportKeyValue(String key, String value) {
    importMap.put(key, value);
  }

  public void start() {
    try {
      copyDirectory(srcDir, destDir);
    } catch (Exception e) {

    }
  }

  /**
   * 拷贝文件夹
   */
  private void copyDirectory(File srcDir, File destDir) throws IOException {
    requireExists(srcDir, "srcDir");
    requireDirectory(srcDir, "srcDir");

    // Cater for destination being directory within the source directory (see IO-141)
    List<String> exclusionList = null;
    final String srcDirCanonicalPath = srcDir.getCanonicalPath();
    final String destDirCanonicalPath = destDir.getCanonicalPath();
    if (destDirCanonicalPath.startsWith(srcDirCanonicalPath)) {
      final File[] srcFiles = srcDir.listFiles();
      if (srcFiles != null && srcFiles.length > 0) {
        exclusionList = new ArrayList<>(srcFiles.length);
        for (final File srcFile : srcFiles) {
          final File copiedFile = new File(destDir, srcFile.getName());
          exclusionList.add(copiedFile.getCanonicalPath());
        }
      }
    }

    doCopyDirectory(srcDir, destDir, exclusionList);
  }

  /**
   * 拷贝文件
   */
  private void copyFile(File srcFile, File destFile) throws IOException {
    requireExists(srcFile, "srcFile");
    requireFile(srcFile, "srcFile");
    createParentDirectories(destFile);
    requireFileIfExists(destFile, "destFile");
    if (destFile.exists()) {
      requireCanWrite(destFile, "destFile");
    }
    String srcPath = srcFile.getAbsolutePath();
    String destPath = destFile.getAbsolutePath();
    if (srcPath.endsWith(".java")) {
      doModify(srcPath, destPath);
    } else {
      doCopy(srcPath, destPath);
    }
    // requireEqualSizes(srcFile, destFile, srcFile.length(), destFile.length());
  }

  private static void doCopy(String srcPath, String destPath) {
    // 获取文件通道
    try (FileInputStream fis = new FileInputStream(srcPath);
         FileOutputStream fos = new FileOutputStream(destPath);
         FileChannel finC = fis.getChannel();
         FileChannel foutC = fos.getChannel()) {
      //申请缓存数组
      ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
      //读取文件数据到buffer中
      while (finC.read(buffer) != -1) {
        //将buffer中的数据写入文件
        buffer.flip();
        foutC.write(buffer);
        //缓存区数据清空
        buffer.clear();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void doModify(String srcPath, String destPath) {
    try (FileReader reader = new FileReader(srcPath);
         BufferedReader br = new BufferedReader(reader);
         FileWriter writer = new FileWriter(destPath)) {
      String line;
      while ((line = br.readLine()) != null) {
        if (importMap.containsKey(line)) {
          line = importMap.get(line);
          System.out.println("modify line=" + line);
        }
        writer.write(line);
        writer.write("\n");
        writer.flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void doCopyDirectory(final File srcDir, final File destDir,
    final List<String> exclusionList) throws IOException {
    // recurse dirs, copy files.
    final File[] srcFiles = srcDir.listFiles();
    requireDirectoryIfExists(destDir, "destDir");
    mkdirs(destDir);
    requireCanWrite(destDir, "destDir");
    for (final File srcFile : srcFiles) {
      final File dstFile = new File(destDir, srcFile.getName());
      if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
        if (srcFile.isDirectory()) {
          doCopyDirectory(srcFile, dstFile, exclusionList);
        } else {
          copyFile(srcFile, dstFile);
        }
      }
    }
  }

  public static boolean deleteQuietly(final File file) {
    if (file == null) {
      return false;
    }
    try {
      if (file.isDirectory()) {
        cleanDirectory(file);
      }
    } catch (final Exception ignored) {
      // ignore
    }

    try {
      return file.delete();
    } catch (final Exception ignored) {
      return false;
    }
  }

  public static void deleteDirectory(final File directory) throws IOException {
    Objects.requireNonNull(directory, "directory");
    if (!directory.exists()) {
      return;
    }
    cleanDirectory(directory);
    directory.delete();
  }

  public static void cleanDirectory(final File directory) throws IOException {
    final File[] files = directory.listFiles();

    final List<Exception> causeList = new ArrayList<>();
    for (final File file : files) {
      try {
        forceDelete(file);
      } catch (final IOException ioe) {
        causeList.add(ioe);
      }
    }

    if (!causeList.isEmpty()) {
      throw new IOException(causeList.toString());
    }
  }

  public static void forceDelete(File file) throws IOException {
    if (file.isDirectory()) {
      deleteDirectory(file);
    } else {
      boolean filePresent = file.exists();
      if (!file.delete()) {
        if (!filePresent) {
          throw new FileNotFoundException("File does not exist: " + file);
        }
        String message = "Unable to delete file: " + file;
        throw new IOException(message);
      }
    }
  }

  public static void forceMkdir(final File directory) throws IOException {
    mkdirs(directory);
  }

  public static File createParentDirectories(final File file) throws IOException {
    return mkdirs(getParentFile(file));
  }

  private static File mkdirs(final File directory) throws IOException {
    if ((directory != null) && (!directory.mkdirs() && !directory.isDirectory())) {
      throw new IOException("Cannot create directory '" + directory + "'.");
    }
    return directory;
  }

  private static File getParentFile(final File file) {
    return file == null ? null : file.getParentFile();
  }

  private static File requireFileIfExists(final File file, final String name) {
    Objects.requireNonNull(file, name);
    return file.exists() ? requireFile(file, name) : file;
  }

  private static File requireDirectoryIfExists(final File directory, final String name) {
    Objects.requireNonNull(directory, name);
    if (directory.exists()) {
      requireDirectory(directory, name);
    }
    return directory;
  }

  private static File requireExists(final File file, final String fileParamName) {
    Objects.requireNonNull(file, fileParamName);
    if (!file.exists()) {
      throw new IllegalArgumentException(
        "File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
    }
    return file;
  }

  private static File requireFile(final File file, final String name) {
    Objects.requireNonNull(file, name);
    if (!file.isFile()) {
      throw new IllegalArgumentException("Parameter '" + name + "' is not a file: " + file);
    }
    return file;
  }


  private static File requireDirectory(final File directory, final String name) {
    Objects.requireNonNull(directory, name);
    if (!directory.isDirectory()) {
      throw new IllegalArgumentException(
        "Parameter '" + name + "' is not a directory: '" + directory + "'");
    }
    return directory;
  }

  private static void requireCanWrite(final File file, final String name) {
    Objects.requireNonNull(file, "file");
    if (!file.canWrite()) {
      throw new IllegalArgumentException(
        "File parameter '" + name + " is not writable: '" + file + "'");
    }
  }

  private static void requireEqualSizes(final File srcFile, final File destFile, final long srcLen,
    final long dstLen) throws IOException {
    if (srcLen != dstLen) {
      throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile
        + "' Expected length: " + srcLen + " Actual: " + dstLen);
    }
  }

}
