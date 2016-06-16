package component.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StringHandler {
	private static String address = "content/system.txt";
	
	public StringHandler(){}
	
	public String StringRead(String str){
		try{
			File file = new File(address);
			if(!file.exists() || file == null){
				return null;
			}
			FileReader reader = new FileReader(address);
			BufferedReader br = new BufferedReader(reader);
			
			String tmp = null;
			
			while((tmp = br.readLine()) != null){
				//System.out.println(tmp);
				if(tmp.indexOf(str) >= 0){
					String[] strArg = tmp.split(": ");
					br.close();
					reader.close();
					if(strArg.length != 1)
						return strArg[1];
					else
						return "";
				}
			}
		
			br.close();
			reader.close();
			
			return null;
		}catch(FileNotFoundException e){
            System.out.println(e.getMessage());
		}catch(IOException e){
            System.out.println(e.getMessage());
		}
		return null;
	}
	
	public boolean StringWrite(String str, String value){
		try{
			if(StringRead(str) == null){
				FileWriter writer = new FileWriter(address, true);
				BufferedWriter bw = new BufferedWriter(writer);
				//System.out.println(str + ": " + value);
				File file = new File(address);
				if(file!=null)
					bw.newLine();
				
				bw.write(str + ": " + value + "\r\n");
				
				bw.flush();
				writer.close();
				bw.close();
			}else{
				StringDelete(str);
				
				FileWriter writer = new FileWriter(address, true);
				BufferedWriter bw = new BufferedWriter(writer);
				bw.write(str + ": " + value);
				
				bw.flush();
				writer.close();
				bw.close();
			}
			return true;
		}catch(FileNotFoundException e){
            System.out.println(e.getMessage());
		}catch(IOException e){
            System.out.println(e.getMessage());
		}
		return false;
	}
	
	public boolean StringDelete(String str){
		ArrayList<String> strArg = new ArrayList<String>();
		ArrayList<String> valueArg = new ArrayList<String>();
		int length = 0;
		try{		
			FileReader reader = new FileReader(address);
			BufferedReader br = new BufferedReader(reader);
			
			String tmp = null;

			while((tmp = br.readLine()) != null){
				String[] tmpArg = tmp.split(": ");
				if(!tmpArg[0].equals(str)){
					strArg.add(tmpArg[0]);
					if(tmpArg.length != 1)
						valueArg.add(tmpArg[1]);
					else
						valueArg.add("");
					length++;
				}
			}
			
//			for(int i = 0;i < strArg.size();i++){
//				System.out.println(strArg.get(i));
//				System.out.println(valueArg.get(i));
//			}
			
			
			br.close();
			reader.close();
			
			File file = new File(address);
			if(file.exists()){
				file.delete();
			}

			FileWriter writer = new FileWriter(address, true);
			BufferedWriter bw = new BufferedWriter(writer);

			if(file!=null)
				bw.newLine();
			
			for(int i = 0;i < length;i++){
				if(strArg.get(i).equals("") && valueArg.get(i).equals(""))
					continue;
				bw.write(strArg.get(i) + ": " + valueArg.get(i));
				bw.newLine();
			}
			
			bw.close();
			writer.close();
			
			return true;
		}catch(FileNotFoundException e){
            System.out.println(e.getMessage());
		}catch(IOException e){
            System.out.println(e.getMessage());
		}
		
		return false;
	}
	
}
