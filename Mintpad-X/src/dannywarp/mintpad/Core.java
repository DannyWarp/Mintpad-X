package dannywarp.mintpad;

import java.io.File;


import java.io.FileReader;
import java.io.FileWriter;

public class Core {
	
	File file_processor;
	FileWriter file_writer;
	FileReader file_reader;
	String file_content = "";
	String projects[];
	String genfolders[];
	String subfolders[];
	String clearsubs[];
	String home, path = "";
	
	public void load_configurations() {
		home = System.getProperty("user.home") + "/Mintpad/";
		file_processor = new File(home +"config.ini");
		
			if (file_processor.exists()) {
				System.out.println("-- out: read configurations.. --");
			}
			
			else {
				
				try {
					file_writer = new FileWriter(home + "config.ini");
					file_writer.write(home);
					file_writer.close();
					System.out.println("executed");
					
				} catch(Exception e) {
					System.out.println("-- error: can't save content --");
				}
				
			}
			
			path = "";
			
		try {
			file_reader = new FileReader(home + "config.ini");
			int i;
			while ((i = file_reader.read()) != -1) {
				path += (char) i;
			}
			
			file_reader.close();
			
			} catch (Exception e) {
				System.out.println("-- error: can't read content --");
			}
		
		path = path.trim();
		

	}

	public void load_projects() {
		file_processor = new File(path);
		projects = file_processor.list();	
	}
	
	public void new_project(String name) {
		file_processor = new File(path +name);
		if (file_processor.exists()) {
			System.out.println("-- error: project folder [" + name + "] already exists in filepath --");
			return;
		}
		
		boolean value = file_processor.mkdir();
		
			if (value == false) {
				System.out.println("-- error: could not find main folder --");
				file_processor = new File(path);
				file_processor.mkdir();
				System.out.println("-- out: main folder created: (" +path+ ") --");
				System.out.println("-- out: restarting operation..");
				new_project(name);
			}
			
			else {
				System.out.println("-- out: project folder [" + name + "] created --");
			}
			
	}
	
	
	public void new_category(String name, String category) {
		file_processor = new File(path + name +"/" +category); 
		if (file_processor.exists()) {
			System.out.println("-- error: category folder [" + category + "] already exists in filepath --");
			return;
		}
		
		boolean value = file_processor.mkdir();
		
			if (value == false) {
			}
			
			else {
				System.out.println("-- out: category [" + category + "] created --");
			}
	}
	
	public void new_subcategory(String name, String category, String subcategory) {
		file_processor = new File(path + name + "/" +category + "/" + subcategory); 
		if (file_processor.exists()) {
			System.out.println("-- error: subcategory folder [" + subcategory + "] already exists in filepath --");
			return;
		}
		
		boolean value = file_processor.mkdir();
		
			if (value == false) {
				System.out.println("-- error: can't create subcategory --");
			}
			
			else {
				System.out.println("-- out: subcategory [" + subcategory + "] created in [" + category + "] --");
			}
	}
	
	public void gen_content(String name, String category, String content) {
			try {
				file_writer = new FileWriter(path + name + "/" + category + "/" + category);
				file_writer.write(content);
				file_writer.close();
				
			} catch(Exception e) {
				System.out.println("-- error: can't save content --");
			}
			
	}
	
	public void sub_content(String name, String category, String subcategory, String content) {
			try {
				file_writer = new FileWriter(path + name + "/" + category + "/" + subcategory + "/" +subcategory);
				file_writer.write(content);
				file_writer.close();
			} catch(Exception e) {
				System.out.println("-- error: can't save content --");			
		}
		
	}
	
	public void content_loader(String name, String category, String subcategory, int value) {
		if (value == 0) {
			try {
			file_reader = new FileReader(path + name + "/"+ category+ "/"+ category);
			int i;
			while ((i = file_reader.read()) != -1) {
				file_content += (char) i;
			}
			
			file_reader.close();
			
			} catch (Exception e) {
				System.out.println("-- error: can't read content --");
			}
		}
		
		else {
			try {
			file_reader = new FileReader(path + name + "/"+ category +"/"+ subcategory +"/"+ subcategory);
			int i;
			while ((i = file_reader.read()) != -1) {
				file_content += (char) i;
			}
			
			file_reader.close();
			
			} catch (Exception e) {
				System.out.println("-- error: can't read content --");
			}
			
		}
	}
	
	public void genloader(String name) {
		file_processor = new File(path +name +"/");
		  genfolders = file_processor.list();
	}
	
	public void subloader(String name, String category) {
		file_processor = new File(path +name +"/"+ category +"/");
		  subfolders = file_processor.list();
		  
		 }
	
	public void remove_content(File file) {
		 for (File subFile : file.listFiles()) {
	         if(subFile.isDirectory()) {
	            remove_content(subFile);
	         } else {
	            subFile.delete();
	         }
	      }
	      file.delete();		
	}
	
	public File get_folder(String name, String category) {
		File file = new File(path +name+"/"+category);
		return file;
	}
	
	public File get_folder(String name, String category, String subcategory) {
		File file = new File(path +name+"/"+category+"/"+subcategory);
		return file;
	}
	
	public File get_project(String name) {
		File file = new File(path +name);
		return file;
	}
	
	public void set_workspace(String path) {
		
		try {
			file_writer = new FileWriter(home + "config.ini");
			file_writer.write(path);
			file_writer.close();
			System.out.println("executed");
			
		} catch(Exception e) {
			System.out.println("-- error: can't save content --");
		}
		
		load_configurations();
		
	}
	
}


















