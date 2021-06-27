import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class HuffmanDecoding {

    LinkedHashMap<String, String>
            invertedDictionary; // Declares a LinkedHashMap that will contain binary string that code for
    // each character

    public HuffmanDecoding() {
        this.invertedDictionary = new LinkedHashMap<>();
    }

    /**
     * @param byteArray This is the byte array that comes from decompressing the binary file and
     *     converting it to byte array
     * @return Returns the bit string containing the binary coded values of each character
     */
    public static String getStringByByte(byte[] byteArray) {
        int k = byteArray.length;
        int z = 0;
        int i = 0;
        StringBuilder ret = new StringBuilder();
        while (z < k) {
            Integer result = byteArray[z] & 0xFF;
            ret.append(String.format("%8s", Integer.toBinaryString(result)).replace(" ", "0"));
            i++;
            z++;
        }
        return ret.toString();
    }

    /**
     * @return This function returns the serialized LinkedHashMap that contains the characters and
     *     their mapped values
     * @throws IOException Throws this when the file reading has an error
     * @throws ClassNotFoundException Throws this if the class that it is read into is not found
     */
    public LinkedHashMap<String, String> deserialize(String file2) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(file2 + ".ser");
        ObjectInputStream in = new ObjectInputStream(file);

        return (LinkedHashMap<String, String>) in.readObject();
    }

    /**
     * @param codedAndPaddedData This is the bit string from the previous function, that still
     *     contains the padding
     * @return Returns the unpadded data
     */
    public String stripPadding(String codedAndPaddedData) {
        // System.out.println(codedAndPaddedData);
        String storedPaddingInformation = codedAndPaddedData.substring(0, 8);
        int redundantPadding = Integer.parseInt(storedPaddingInformation);
        codedAndPaddedData = codedAndPaddedData.substring(8);
        return codedAndPaddedData.substring(
                0, codedAndPaddedData.length() - String.valueOf(redundantPadding).length());
    }

    /**
     * @param huffmanHeader This are the saved codes that we have deserialized to decode the data
     * @param filename This is the filename of the file the user wants to decompress
     * @throws IOException Thrown when there is an error reading the file
     */
    public void decomp(LinkedHashMap<String, String> huffmanHeader, String filename)
            throws IOException {
        byte[] huffmanCodedDataArr = Files.toByteArray(new File(filename));

        ArrayList<String> keys = new ArrayList<>(huffmanHeader.keySet());
        ArrayList<String> values = new ArrayList<>(huffmanHeader.values());
        for (int i = 0; i < keys.size(); i++) {
            invertedDictionary.put(values.get(i), keys.get(i));
        }

        String decodedBitString = getStringByByte(huffmanCodedDataArr);

        String codedData = stripPadding(decodedBitString);

        String codeInUse = "";
        StringBuilder decodedData = new StringBuilder();

        for (String bit : codedData.split("")) {
            codeInUse += bit;
            if (invertedDictionary.containsKey(codeInUse)) {
                decodedData.append(invertedDictionary.get(codeInUse));
                codeInUse = "";
            }
        }
        FileUtils.writeStringToFile(
                new File(filename.split("_")[0] + "_decompressed.txt"),
                String.valueOf(decodedData),
                StandardCharsets.UTF_8);
    }
}
