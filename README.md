# huffman-encoding
This is my attempt to port my previous Huffman program from Python to Java

### Note: Do not use file extensions when specifying which file to compress or decompress
## To use the program do as follows:
### Run the following command in the src folder 
    java HuffmanApp
## To compress:
    compress filename
## To decompress:
    decompress filename
    
## A current limitation is when compressing repetitive files such as the human genome, Java can run out of memory; but its use in books and non repeating texts is quite efficient.
   It reduced the bible by 44% in about a second, and decompressed it in about 2 or 3 seconds
