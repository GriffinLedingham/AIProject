import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;


public class FertilityFormat {

	public static void main(String[] args) throws IOException
	{
		String filetext = "";
		FileInputStream stream = new FileInputStream(new File("fertility_Diagnosis.txt"));
		  try {
		    FileChannel fc = stream.getChannel();
		    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		    /* Instead of using default, pass in a decoder. */
		    filetext =  Charset.defaultCharset().decode(bb).toString();
		  }
		  finally {
		    stream.close();
		  }
		  
		  
		  filetext.replaceAll("N", "1");
		  filetext.replaceAll("0\n", "-1");
		  filetext.replaceAll(",", " ");
		  
		  PrintWriter out = new PrintWriter("fertility_Diagnosis_formatted.txt");
		  out.println("9 1 100");
		  out.println(filetext);
	}
}
