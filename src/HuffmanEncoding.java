import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HuffmanEncoding implements Serializable {
    LinkedHashMap<String, String>
            saved_codes; // Declares a LinkedHashMap of binary codes mapped to characters
    LinkedHashMap<String, Integer>
            frequencyDictionary; // Declares a LinkedHashMap that the frequencies are placed
    // into
    PriorityQueue<Node> pQueue; // Declares a priority queue

    /**
     * This method is initializes the priority queue declared earlier as well as creating a frequency
     * dictionary
     *
     * @throws IOException This is thrown if there is an issue reading the file to be compressed
     */
    public HuffmanEncoding() throws IOException {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getFrequency));
        LinkedHashMap<String, String> saved_codes = new LinkedHashMap<>();
        FreqDict fd = new FreqDict();
        this.frequencyDictionary = fd.makeFrequencyDictionary(Files.readString(Path.of("bible.txt")));
        this.pQueue = pq;
        this.saved_codes = saved_codes;
    }

    /**
     * This method's job is to convert each block of eight binary digits to bytes.
     *
     * @param binaryString This string is the entire text converted to binary codes
     * @return Returns a byte array containing the values for the text file
     */
    public static byte[] getByteByString(String binaryString) {
        Iterable<String> iterable = Splitter.fixedLength(8).split(binaryString);
        byte[] ret = new byte[Iterables.size(iterable)];
        Iterator<String> iterator = iterable.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            int byteAsInt = Integer.parseInt(iterator.next(), 2);
            ret[i] = (byte) byteAsInt;
            i++;
        }
        return ret;
    }

    /**
     * @param userFile this is the filename that the user wants to compress
     * @throws IOException Throws this exception when there is an issue reading the file the user
     *     wants to compress
     */
    public void HuffEncode(String userFile) throws IOException {
        HuffmanEncoding he = new HuffmanEncoding();
        FreqDict fd = new FreqDict();
        fd.orderByValue(he.frequencyDictionary, Integer::compareTo);
        he.heap(he.frequencyDictionary, he.invertDict(he.frequencyDictionary));
        he.encodingMain();
        String line2 = Files.readString(Path.of(userFile + ".txt"));
        String encodedData = he.concatenateOne(line2);
        String encodedDataWithPadding = he.addPadding(encodedData);
        byte[] bha = getByteByString(encodedDataWithPadding);
        File bcbin = new File(userFile + "_compressed.bin");
        String filename = userFile + ".ser";
        FileOutputStream file = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(he.saved_codes);
        out.close();
        file.close();

        FileUtils.writeByteArrayToFile(bcbin, bha);
    }

    /**
     * This function is responsible for initializing the encoding function below with the base
     * variables
     */
    public void encodingMain() {
        String currCode = "";
        Node popped = pQueue.poll();
        encoding(popped, currCode);
    }

    /**
     * This method recursively calls itself until all characters have been worked on
     *
     * @param root this is the root node that is being worked on
     * @param curr_code this is the current code for the letter being compressed
     */
    public void encoding(Node root, String curr_code) {
        if (root == null) {
            return;
        }
        if (!(root.character == null)) {
            saved_codes.put(root.character, curr_code);
            return;
        }
        String code_left = curr_code + "0";
        encoding(root.left, code_left);
        String code_right = curr_code + "1";
        encoding(root.right, code_right);
    }

    /**
     * @param frequencyDictionary this is the LinkedHashMap containing all of the character occurances
     * @return Returns an ArrayList of the keys of the frequency dictionary
     */
    public ArrayList<String> invertDict(LinkedHashMap<String, Integer> frequencyDictionary) {
        return new ArrayList<>(frequencyDictionary.keySet());
    }

    /**
     * This method adds each node to the priority queue. It then has another for loop that iterates
     * over the priority queue, pairing up the smallest nodes in the queue and adding them back to it
     * until the length is equal to 1, ending with one very large root node containing the codes for
     * every character.
     *
     * @param frequencyDic This is the LinkedHashMap of the character occurrences
     * @param chars This is the ArrayList created by the invertDict method
     */
    public void heap(LinkedHashMap<String, Integer> frequencyDic, ArrayList<String> chars) {
        ArrayList<Integer> vals = new ArrayList<>(frequencyDic.values());
        for (int i = 0; i < frequencyDic.size(); i++) {
            Node base = new Node(chars.get(i), vals.get(i));
            pQueue.add(base);
        }

        for (int i = pQueue.size(); i > 1; i--) {
            Node x = pQueue.poll();
            Node y = pQueue.poll();
            int f = x.frequency + y.frequency;

            Node c = new Node(null, f);

            c.left = x;
            c.right = y;

            pQueue.add(c);
        }
    }

    /**
     * @param data This is the uncompressed, and unencoded text data
     * @return Returns a long string of binary digits that represent the coded text
     */
    public String concatenateOne(String data) {
        StringBuilder encodedData = new StringBuilder();
        char[] k = data.toCharArray();
        for (char element : k) {
            encodedData.append(saved_codes.get(String.valueOf(element)));
        }
        return encodedData.toString();
    }

    /**
     * @param encodedData This is the encoded data created by the Concatenate method.
     * @return Returns the string but with between 0 and 8 digits of padding at the start and end
     */
    public String addPadding(String encodedData) {
        int addedPadding = 8 - encodedData.length() % 8;
        for (int i = 0; i < addedPadding; i++) {
            encodedData += "0";
        }
        String paddedInfo =
                String.format("%8s", Integer.toBinaryString(addedPadding)).replace(" ", "0");
        return paddedInfo + encodedData;
    }
}
