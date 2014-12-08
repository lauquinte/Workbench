package com.dpc.workbench.util;

import java.io.File;

public class ExecuteBatchProcess {

	private static File batchFile = null;
	private static int exitCode = -1;
	
	public int executeBatchFile(String batchFilePath, String argument) throws Exception {
		
		try {
			// Setup Batch file to be executed
			batchFile = new File(batchFilePath);
	        System.out.println("Batch file Path: " + batchFile.getAbsolutePath() + " - AUDIT_SID :" + argument);

	        // Setup OS command to be executed
	        // (Batch file with with an input parameter [the passed in 'argument' param])
	        ProcessBuilder pb = new ProcessBuilder(batchFile.getAbsolutePath(), argument);

	        // Set working directory for Batch file execution
	        pb.directory(new File("E:\\HSDH"));

	        // Execute Cmd and Wait for its return code
	        Process p = pb.start();
	        exitCode = p.waitFor();
	        
	        System.out.println("Process finished with Exit Code: " + exitCode);
	        
	        // Free up memory
	        p.destroy();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return exitCode;
	}
}
