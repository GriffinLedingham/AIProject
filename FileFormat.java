import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.io.*;


public class FileFormat {

	public void format()
	{
        try{
            
    		File file = new File("iris.txt");
            
    		file.delete();
            
    	}catch(Exception e){
            
    		e.printStackTrace();
            
    	}
        
        try{
            
    		File file = new File("fertility.txt");
            
    		file.delete();
            
    	}catch(Exception e){
            
    		e.printStackTrace();
            
    	}
        
        
        
        
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
		  
          File f = new File("iris.txt");
		  
		  filetext = filetext.replaceAll("Iris-setosa", "-1 -1");
		  filetext = filetext.replaceAll("Iris-versicolor", "-1 1");
		  filetext = filetext.replaceAll("Iris-virginica", "1 -1");
		  filetext = filetext.replaceAll(",", " ");
		  
		  BufferedWriter out = new BufferedWriter(new FileWriter(f));
		  out.write("4 2 150\n");
		  out.write(filetext);
          out.close();
        //==============================================================
          filetext = "";
        stream = new FileInputStream(new File("fertility_Diagnosis.txt"));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            filetext =  Charset.defaultCharset().decode(bb).toString();
        }
        finally {
		    stream.close();
        }
        
        f = new File("fertility.txt");
        
        filetext = filetext.replaceAll("N", "-1.0");
        filetext = filetext.replaceAll("O", "1.0");
        filetext = filetext.replaceAll(",", " ");
        
        out = new BufferedWriter(new FileWriter(f));
        out.write("9 100\n");
        out.write(filetext);
        out.close();
	}
}
