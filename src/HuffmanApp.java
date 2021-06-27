import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Scanner;

public class HuffmanApp {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println(
                "Please input either compress or decompress, "
                        + "followed by the filename without the extension. Only txt files supported.");
        Scanner userInp = new Scanner(System.in);
        String compOrDecomp = userInp.nextLine();
        String p1 = compOrDecomp.split(" ")[0].toLowerCase(Locale.ROOT);
        String p2 = compOrDecomp.split(" ")[1];
        if (p1.equals("compress")) {
            long startTime = System.nanoTime();
            HuffmanEncoding he = new HuffmanEncoding();
            he.HuffEncode(p2);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            StringBuilder print = new StringBuilder();
            print.append("Compression took: ");
            print.append((duration / 1000000) / 1000);
            if (((duration / 1000000) / 1000) == 1) {
                print.append(" second");
            } else {
                print.append(" seconds");
            }
            System.out.println(print);
            File k = new File(p2 + ".txt");
            File k1 = new File(p2 + "_compressed.bin");
            File k2 = new File(p2 + ".ser");
            long old_size = k.length();
            long new_size = k1.length() + k2.length();
            long percent_change = ((old_size - new_size));
            double perc = (double) percent_change / old_size;
            System.out.println("File reduced by: " + (perc) * 100 + "%");
        } else if (p1.equals("decompress")) {
            HuffmanDecoding hd = new HuffmanDecoding();
            long startTime = System.nanoTime();
            LinkedHashMap<String, String> savedCodess = hd.deserialize(p2);
            hd.decomp(savedCodess, p2 + "_compressed.bin");
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            StringBuilder print = new StringBuilder();
            print.append("Decompression took: ");
            print.append((duration / 1000000) / 1000);
            if (((duration / 1000000) / 1000) == 1) {
                print.append(" second");
            } else {
                print.append(" seconds");
            }
            System.out.println(print);
        }
    }
}
