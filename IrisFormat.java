import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;


public class IrisFormat {

	public static void main(String[] args) throws IOException
	{
		String filetext = "";
		FileInputStream stream = new FileInputStream(new File("iris.data"));
		  try {
		    FileChannel fc = stream.getChannel();
		    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		    /* Instead of using default, pass in a decoder. */
		    filetext =  Charset.defaultCharset().decode(bb).toString();
		  }
		  finally {
		    stream.close();
		  }
		  
		  
		  filetext.replaceAll("Iris-setosa", "-1 -1");
		  filetext.replaceAll("Iris-versicolor", "-1 1");
		  filetext.replaceAll("Iris-virginica", "1 -1");
		  filetext.replaceAll(",", " ");
		  
		  PrintWriter out = new PrintWriter("iris.txt");
		  out.println(filetext);
	}
}
