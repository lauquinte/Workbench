package com.dpc.workbench.config;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.BackgroundProcess;
import com.dpc.workbench.action.FileUploadAction;
import com.opensymphony.xwork2.ActionInvocation;

public class CustomExecuteAndWaitInterceptor extends org.apache.struts2.interceptor.ExecuteAndWaitInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
	protected BackgroundProcess getNewBackgroundProcess(String name,
								  ActionInvocation actionInvocation, 
								  				 int threadPriority) {

		BackgroundProcess bgProcess;
		if (actionInvocation.getAction() instanceof FileUploadAction) {
			FileUploadAction uploadAction = (FileUploadAction) actionInvocation.getAction();
			try {
				File origFile = uploadAction.getUploadDoc();
				if (origFile != null) {
					File altFile = new File(origFile.getParentFile(), origFile.getName() + ".tmp");
					FileUtils.copyFile(origFile, altFile);
//					altFile.deleteOnExit();
					uploadAction.setUploadDoc(altFile);
				}
			} catch (Exception ex) {
				throw new RuntimeException("Error copying uploaded file", ex);
			}
			bgProcess = new UploadBackgroundProcess(name + "BackgroundThread",
													actionInvocation, threadPriority);
		} else {
			bgProcess = super.getNewBackgroundProcess(name, actionInvocation,
														threadPriority);
		}
		return bgProcess;
	}

	/**
	 * Wraps the standard {@link BackgroundProcess} to clean up alternate file
	 * created above.
	 */
	private class UploadBackgroundProcess extends BackgroundProcess {

		private static final long serialVersionUID = 1L;

		public UploadBackgroundProcess(String threadName,
							 ActionInvocation invocation, 
							 		  int threadPriority) {
			super(threadName, invocation, threadPriority);
		}

		@Override
		protected void afterInvocation() throws Exception {
			super.afterInvocation();
			FileUtils.deleteQuietly(((FileUploadAction) getAction()).getUploadDoc());
		}
	}

}
