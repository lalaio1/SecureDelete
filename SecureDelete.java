// By: lalaio1
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class SecureDelete {
    private static final int OVERWRITE_PASSES = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("[?] file or directory path: ");
        String filePath = scanner.nextLine();

        File file = new File(filePath);

        if (file.exists()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                deleteFile(file);
            }
        } else {
            System.out.println("[!] the file or directory does not exist.");
        }

        scanner.close();
    }

    private static void deleteDirectory(File dir) {
        System.out.println("[*] starting deletion of directory: " + dir.getAbsolutePath());
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    deleteFile(file);
                }
            }
        }
        if (dir.delete()) {
            System.out.println("[+] directory deleted: " + dir.getAbsolutePath());
        } else {
            System.out.println("[-] could not delete directory: " + dir.getAbsolutePath());
        }
    }

    private static void deleteFile(File file) {
        displayFileInfo(file);

        long startTime = System.currentTimeMillis();

        try {
            overwriteFile(file);
            if (file.delete()) {
                System.out.println("[+] file permanently deleted: " + file.getAbsolutePath());
            } else {
                System.out.println("[-] could not delete file: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("[e] error overwriting file: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("[*] total execution time for file: " + duration + " ms");
    }

    private static void displayFileInfo(File file) {
        System.out.println("[*] file information:");
        System.out.println(" - path: " + file.getAbsolutePath());
        System.out.println(" - size: " + file.length() + " bytes");
        System.out.println(" - last modification: " + new java.util.Date(file.lastModified()));
    }

    private static void overwriteFile(File file) throws IOException {
        Random random = new Random();
        long length = file.length();

        for (int i = 0; i < OVERWRITE_PASSES; i++) {
            System.out.println("[*] overwriting file (pass " + (i + 1) + ")...");

            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] randomData = new byte[(int) length];

                if (i % 2 == 0) {
                    random.nextBytes(randomData);
                    System.out.println("[*] overwriting with random data.");
                } else {
                    for (int j = 0; j < randomData.length; j++) {
                        randomData[j] = 0;
                    }
                    System.out.println("[*] overwriting with zeros.");
                }

                fos.write(randomData);
                fos.flush();
            }
        }

        System.out.println("[*] overwriting file with fixed pattern (0xFF)...");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] fixedPattern = new byte[(int) length];
            for (int j = 0; j < fixedPattern.length; j++) {
                fixedPattern[j] = (byte) 0xFF;
            }
            fos.write(fixedPattern);
            fos.flush();
        }

        System.out.println("[+] overwriting completed for file: " + file.getAbsolutePath());
    }
}
// By: lalaio1
