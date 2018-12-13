package com.munsiji.customthread;

import java.io.File;

public class DocCleanThread implements Runnable{
	private File file;
	public DocCleanThread(File file){
		this.file = file;
	}

	@Override
	public void run() {
		System.out.println("deleting file after downloaded by user. File is:"+file);
		try{
			Thread.sleep(2000);
			if(file.delete()){
				System.out.println("file :"+file+"   has been deleted");
			}
			else{
				System.out.println("file is not available");
			}
		}
		catch(Exception e){
			System.out.println("Exception occur while deleting the file:"+file+"\n exception:"+e);
		}
		
	}

}
